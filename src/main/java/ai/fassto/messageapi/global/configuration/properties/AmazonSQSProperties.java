package ai.fassto.messageapi.global.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.cloud.aws.sqs")
@Data
public class AmazonSQSProperties {

    private String emailSend;
}
