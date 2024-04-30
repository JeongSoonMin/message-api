package ai.fassto.messageapi.global.configuration;

import ai.fassto.messageapi.global.configuration.properties.BaseMongoProperties;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "ai.fassto.messageapi.persistence")
@RequiredArgsConstructor
@EnableReactiveMongoAuditing
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    private final BaseMongoProperties baseMongoProperties;

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(createMongoClientSettings());
    }

    private MongoClientSettings createMongoClientSettings() {
        MongoCredential credential = MongoCredential.createCredential(
                baseMongoProperties.getUsername(), baseMongoProperties.getDatabase(),
                baseMongoProperties.getPassword().toCharArray());

        return MongoClientSettings.builder()
                .readConcern(ReadConcern.DEFAULT)
                .writeConcern(WriteConcern.MAJORITY)
                .readPreference(ReadPreference.primary())
                .credential(credential)
                .applyToSslSettings(builder ->
                        builder.enabled(false)
                                .invalidHostNameAllowed(true)
                )
                .applyToClusterSettings(builder ->
                        builder.hosts(
                                Arrays.asList(
                                        new ServerAddress(baseMongoProperties.getPrimary().getHost(),
                                                baseMongoProperties.getPrimary().getPort())
                                        , new ServerAddress(
                                                baseMongoProperties.getSecondary().getHost(),
                                                baseMongoProperties.getSecondary().getPort())
                                )
                        ))
                .build();
    }

    @Bean
    public ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {
        return new ReactiveMongoTransactionManager(factory);
    }

    @Bean
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }

    @Override
    protected String getDatabaseName() {
        return baseMongoProperties.getDatabase();
    }

}
