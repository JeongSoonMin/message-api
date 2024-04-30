package ai.fassto.messageapi.global.exception.handler;

import ai.fassto.messageapi.global.exception.BaseException;
import ai.fassto.messageapi.model.CommonResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
@Slf4j
public class ReactiveExceptionHandler extends AbstractErrorWebExceptionHandler {

    public ReactiveExceptionHandler(ErrorAttributes errorAttributes,
                                    Resources resources,
                                    ApplicationContext applicationContext,
                                    ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageReaders(configurer.getReaders());
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {

        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorProperties = getErrorAttributes(request, ErrorAttributeOptions.defaults());

        Throwable throwable = getError(request);

        Object data = null;
        String message = throwable.getMessage();
        int status = 500;
        String logLevel = "ERROR";
        String resultCode = HttpStatus.INTERNAL_SERVER_ERROR.name();

        switch (throwable) {
            case BaseException ex -> {
                data = ex.getData();

                if (!Objects.isNull(ex.getErrorCode())) {
                    status = ex.getErrorCode().getStatus();
                    resultCode = ex.getErrorCode().name();
                    logLevel = ex.getErrorCode().getLogLevel();
                }

                if ("WARN".equals(logLevel)) {
                    log.warn("[BaseException] {}", message);
                } else if ("ERROR".equals(logLevel)) {
                    log.error("[BaseException] {}", message);
                }
            }
            default -> {
                HttpStatus httpStatus = Arrays.stream(HttpStatus.values())
                        .filter(e -> e.value() == Integer.parseInt(String.valueOf(errorProperties.get("status"))))
                        .findFirst()
                        .orElse(HttpStatus.INTERNAL_SERVER_ERROR);

                status = httpStatus.value();
                resultCode = httpStatus.name();

                log.error("[Exception] {}", message);
                message = ErrorCode.INTERNAL_SERVER_ERROR.getMessage();
            }
        }

        return CommonResponse.build(
                status,
                resultCode,
                message,
                data
        );
    }
}
