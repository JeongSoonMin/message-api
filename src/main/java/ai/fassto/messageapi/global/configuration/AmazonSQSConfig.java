package ai.fassto.messageapi.global.configuration;


import io.awspring.cloud.autoconfigure.sqs.SqsProperties.Listener;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.ListenerMode;
import io.awspring.cloud.sqs.listener.QueueNotFoundStrategy;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class AmazonSQSConfig {

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
        return SqsMessageListenerContainerFactory
                .builder()
                .configure(options -> options
                        .acknowledgementMode(AcknowledgementMode.MANUAL)
                        .listenerMode(ListenerMode.SINGLE_MESSAGE)
                        .maxConcurrentMessages(100)
                        .maxMessagesPerPoll(100)
                        .listenerShutdownTimeout(Duration.of(25L, ChronoUnit.SECONDS))
                        .acknowledgementShutdownTimeout(Duration.of(20L, ChronoUnit.SECONDS))
                        .acknowledgementThreshold(5)
                        .acknowledgementInterval(Duration.of(50, ChronoUnit.MILLIS))
                        .queueNotFoundStrategy(QueueNotFoundStrategy.FAIL))
                .sqsAsyncClient(sqsAsyncClient())
                .build();
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder().build();
    }

    @Bean
    public Listener listener() {
        return new Listener();
    }

}
