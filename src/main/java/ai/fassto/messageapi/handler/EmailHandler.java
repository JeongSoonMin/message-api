package ai.fassto.messageapi.handler;

import ai.fassto.messageapi.entity.EmailSend;
import ai.fassto.messageapi.global.configuration.properties.AmazonSQSProperties;
import ai.fassto.messageapi.model.CommonResponse;
import ai.fassto.messageapi.model.EmailRequest.EmailSendRequest;
import ai.fassto.messageapi.model.EmailSendQueue;
import ai.fassto.messageapi.repository.EmailSendRequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        return CommonResponse.ok(emailSendRequestRepository.findAll());
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
                                throw new RuntimeException(e);
                            }
                            log.info("queueing email send request {}", payload);
                            sqsTemplate.send(to -> to.queue(amazonSQSProperties.getEmailSend())
                                    .payload(payload));
                        })
                        .then(CommonResponse.ok());
    }
}
