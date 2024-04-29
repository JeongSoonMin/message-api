package ai.fassto.messageapi.model;

public record EmailRequest() {

    public record EmailSendRequest(
            String templateName,
            String senderEmail,
            String senderName,
            String receiverEmail,
            String receiverName,
            String attachFilePath,
            String reqUserName
    ) {
    }

}
