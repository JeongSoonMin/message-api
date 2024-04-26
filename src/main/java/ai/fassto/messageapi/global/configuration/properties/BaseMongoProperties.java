package ai.fassto.messageapi.global.configuration.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fassto.mongodb.base-cluster")
@Data
public class BaseMongoProperties {
    private MongoProperties write;
    private MongoProperties readOnly;
    private String database;
    private String username;
    private String password;
}
