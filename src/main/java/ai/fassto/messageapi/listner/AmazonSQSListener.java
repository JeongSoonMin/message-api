package ai.fassto.messageapi.listner;

import ai.fassto.messageapi.model.queue.EmailSendQueue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AmazonSQSListener {

    private final ObjectMapper om = new ObjectMapper();

    /**
     * 반송 이메일 큐 처리1
     * @param message
     * @param acknowledgement
     *
     * Acknowledgement 기본값이 ON_SUCCESS 로 설정이었으나, 오래걸리는 작업의 경우 ack를 우선 실행하여 메시지 완료 처리 하는 경우를 위해 MongoClient 기본값 MANUAL 로 변경.
     * 로직 처리 후에 acknowledgement.acknowledge(); 수동 적용 필요.
     */
    @SqsListener(value = "${spring.cloud.aws.sqs.email-bounce}")
    public void emailBounceProcess(String message, Acknowledgement acknowledgement) {
        log.info("[Queue 수신 처리] message : {}", message);

        EmailSendQueue emailSendQueue = new EmailSendQueue();

        try {
            emailSendQueue = om.readValue(message, EmailSendQueue.class);
        } catch (JsonProcessingException e) {
            log.error("[Exception] ", e);
        }

        log.info("[Queue 수신 변환] emailSendQueue : {}", emailSendQueue);

        acknowledgement.acknowledge(); // 수신 된 메시지 ack 처리.
    }

    /**
     * 반송 이메일 큐 처리 방식2
     * @param message
     *
     * Acknowledgement ON_SUCCESS로 메소드가 Exception 없이 정상 완료시 LISTEN 상태의 큐 메시지를 ACK 처리함.
     */
    /*@SqsListener(value = "${spring.cloud.aws.sqs.email-bounce}", acknowledgementMode = SqsListenerAcknowledgementMode.ON_SUCCESS)
    public void emailBounceProcess(String message) {
        log.info("[Queue 수신 처리] message : {}", message);

        EmailSendQueue emailSendQueue = new EmailSendQueue();

        try {
            emailSendQueue = om.readValue(message, EmailSendQueue.class);
        } catch (JsonProcessingException e) {
            log.error("[Exception] ", e);
        }

        log.info("[Queue 수신 변환] emailSendQueue : {}", emailSendQueue);
    }*/
}
