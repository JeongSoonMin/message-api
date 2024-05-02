package ai.fassto.messageapi.handler;

import ai.fassto.messageapi.entity.EmailSend;
import ai.fassto.messageapi.global.configuration.properties.AmazonSQSProperties;
import ai.fassto.messageapi.global.exception.BaseException;
import ai.fassto.messageapi.global.exception.handler.ErrorCode;
import ai.fassto.messageapi.model.CommonResponse;
import ai.fassto.messageapi.model.EmailRequest.EmailSendRequest;
import ai.fassto.messageapi.model.queue.EmailSendQueue;
import ai.fassto.messageapi.persistence.EmailSendRequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailHandler {

    private final AmazonSQSProperties amazonSQSProperties;

    ObjectMapper om = new ObjectMapper();

    private final EmailSendRequestRepository emailSendRequestRepository;
    private final SqsTemplate sqsTemplate;

    @Transactional(readOnly = true)
    public Mono<ServerResponse> emailSendRequestList(ServerRequest request) {
        int page = request.queryParam("page").isPresent() ? Integer.parseInt(request.queryParam("page").get()) - 1 : 0;
        int size = request.queryParam("pagesize").isPresent() ? Integer.parseInt(request.queryParam("pagesize").get()) : 20;

        Pageable pageable = PageRequest.of(page, size);
        return CommonResponse.ok(
                emailSendRequestRepository.findAllBy(pageable)
                        .collectList()
                        .zipWith(emailSendRequestRepository.count())
                        .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()))
        );
    }

    @Transactional
    public Mono<ServerResponse> emailSendRequest(ServerRequest request) {
        return request.bodyToMono(EmailSendRequest.class)
                        .map(EmailSend::fromSendRequest)
                        .flatMap(emailSendRequestRepository::save) // DB 저장
                        .doOnNext((emailSend) -> {
                            // 이메일 발송 큐 전달
                            EmailSendQueue emailSendQueuePayload = emailSend.toQueuePayload();

                            String payload;
                            try {
                                payload = om.writeValueAsString(emailSendQueuePayload);
                            } catch (JsonProcessingException e) {
                                throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, null, e.getMessage());
                            }
                            log.info("queueing email send request {}", payload);
                            sqsTemplate.send(to -> to.queue(amazonSQSProperties.getEmailSend())
                                    .payload(payload));
                        })
                        .then(CommonResponse.ok());
    }
}
