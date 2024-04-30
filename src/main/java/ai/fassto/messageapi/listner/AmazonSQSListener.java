package ai.fassto.messageapi.listner;

import ai.fassto.messageapi.model.queue.EmailSendQueue;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AmazonSQSListener {

    private final ObjectMapper om = new ObjectMapper();

    @SqsListener(value = "${spring.cloud.aws.sqs.email-bounce}")
    public void emailSending(String message, Acknowledgement acknowledgement) {
        log.info("[Queue 수신 처리] message : {}", message);

        EmailSendQueue emailSendQueue = new EmailSendQueue();

        try {
            emailSendQueue = om.readValue(message, EmailSendQueue.class);
        } catch (Exception e) {
            log.error("[Exception] ", e);
        }

        acknowledgement.acknowledge(); // 수신 된 메시지 ack 처리.
        log.info("[Queue 수신 변환] emailSendQueue : {}", emailSendQueue);
    }
}
