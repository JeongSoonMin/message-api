package ai.fassto.messageapi.model.queue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailSendQueue implements Serializable {
    private String sendId;
    private String templateName;
    private String senderEmail;
    private String senderName;
    private String receiverEmail;
    private String receiverName;
    private String attachFilePath;
}
