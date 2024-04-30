package ai.fassto.messageapi.entity;

import ai.fassto.messageapi.model.EmailRequest.EmailSendRequest;
import ai.fassto.messageapi.model.EmailSendQueue;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@Builder
public class EmailSend {

        @Id
        @Generated
        private String id;
        private String templateName;
        private String senderEmail;
        private String senderName;
        private String receiverEmail;
        private String receiverName;
        private String attachFilePath;
        @CreatedDate
        private LocalDateTime regTime;
        @CreatedBy
        private String regUserName;
        @LastModifiedDate
        private LocalDateTime updTime;
        @LastModifiedBy
        private String updUserName;

        public static EmailSend fromSendRequest(EmailSendRequest emailSendRequest) {
                return EmailSend.builder()
                        .templateName(emailSendRequest.templateName())
                        .senderEmail(emailSendRequest.senderEmail())
                        .senderName(emailSendRequest.senderName())
                        .receiverEmail(emailSendRequest.receiverEmail())
                        .receiverName(emailSendRequest.receiverName())
                        .attachFilePath(emailSendRequest.attachFilePath())
                        .regUserName(emailSendRequest.reqUserName())
                        .build();
        }

        public EmailSendQueue toQueuePayload() {
                return new EmailSendQueue(
                        this.id,
                        this.templateName,
                        this.senderEmail,
                        this.senderName,
                        this.receiverEmail,
                        this.receiverName,
                        this.attachFilePath
                );
        }
}
