package ai.fassto.messageapi.handler;

import ai.fassto.messageapi.entity.EmailSend;
import ai.fassto.messageapi.model.CommonResponse;
import ai.fassto.messageapi.model.EmailRequest.EmailSendRequest;
import ai.fassto.messageapi.repository.EmailSendRequestRepository;
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

    private final EmailSendRequestRepository emailSendRequestRepository;

    @Transactional(readOnly = true)
    public Mono<ServerResponse> emailSendRequestList(ServerRequest request) {
        return CommonResponse.ok(emailSendRequestRepository.findAll());
    }

    @Transactional
    public Mono<ServerResponse> emailSendRequest(ServerRequest request) {
        return CommonResponse.ok(
                request.bodyToMono(EmailSendRequest.class)
                        .map(EmailSend::fromSendRequest)
                        .flatMap(emailSendRequestRepository::save) // DB 저장
                        .map(EmailSend::getId)
                // TODO Queue 요청 내용 추가 필요
        );
    }
}
