package ai.fassto.messageapi.global.exception.handler;

import ai.fassto.messageapi.global.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    /*@Override
    public Map<String, Object> getErrorAttributes(ServerRequest request,
            ErrorAttributeOptions options) {
        String errorLevel = "WARN";
        Map<String, Object> map = super.getErrorAttributes(request, options);

        Throwable throwable = getError(request);

        map.put("message", throwable.getMessage());

        switch (throwable) {
            case BaseException ex -> {
                map.put("data", ex.getData());

                if (!Objects.isNull(ex.getErrorCode())) {
                    map.put("status", ex.getErrorCode().getStatus());
                    map.put("errorCode", ex.getErrorCode().name());
                    errorLevel = ex.getErrorCode().getErrorLevel();
                }

                if ("WARN".equals(errorLevel)) {
                    log.warn("[BaseException] {}", ex.getMessage());
                } else if ("ERROR".equals(errorLevel)) {
                    log.error("[BaseException] {}", ex.getMessage());
                }
            }
            default -> {
                map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                map.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.name());

                log.error("[Exception] {}", throwable.getMessage());
            }
        }

        return map;
    }*/
}
