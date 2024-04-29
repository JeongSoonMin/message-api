package ai.fassto.messageapi.global.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import ai.fassto.messageapi.handler.EmailHandler;
import ai.fassto.messageapi.handler.SampleHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> sampleRouterFunction(SampleHandler handler) {
        return RouterFunctions.route()
                .GET("/sample", accept(MediaType.APPLICATION_JSON), handler::sampleList)
                .POST("/sample", accept(MediaType.APPLICATION_JSON), handler::sampleAdd)
                .PUT("/sample/{sampleId}", accept(MediaType.APPLICATION_JSON), handler::sampleModify)
                .DELETE("/sample/{sampleId}", accept(MediaType.APPLICATION_JSON), handler::sampleRemove)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> sampleRouter(EmailHandler handler) {
        return RouterFunctions.route()
                .GET("/email/send", accept(MediaType.APPLICATION_JSON), handler::emailSendRequestList)
                .POST("/email/send", accept(MediaType.APPLICATION_JSON), handler::emailSendRequest)
                .build();
    }
}
