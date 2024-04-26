package ai.fassto.messageapi.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Builder
@Data
@Slf4j
public class CommonResponse<T> {

    private Integer resultCode;
    private String message;
    private T data;

    public static Mono<ServerResponse> ok() {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(success(null)), CommonResponse.class);
    }

    public static Mono<ServerResponse> ok(Flux<?> flux) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(toSuccessCommonResponse(flux), flux.getClass());
    }

    public static Mono<ServerResponse> okVoid(Mono<Void> mono) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(toSuccessCommonResponseVoid(mono), mono.getClass());
    }

    public static Mono<ServerResponse> ok(Mono<?> mono) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(toSuccessCommonResponse(mono), mono.getClass());
    }

    public static Mono<CommonResponse<Object>> toSuccessCommonResponse(Flux<?> tFlux) {
        return Flux.merge(tFlux)
                .collectList()
                .flatMap(CommonResponse::successFluxToMono);
    }

    public static Mono<CommonResponse<Object>> toSuccessCommonResponse(Mono<?> tMono) {
        return tMono.map(CommonResponse::success);
    }

    public static Mono<CommonResponse<Object>> toSuccessCommonResponseVoid(Mono<Void> tMono) {
        return tMono.then(Mono.fromCallable(() -> CommonResponse.success(null)));
    }

    private static Mono<? extends CommonResponse<Object>> successFluxToMono(Object data) {
        return Mono.just(CommonResponse.success(data));
    }

    private static CommonResponse<Object> success(Object data) {
        return CommonResponse.builder()
                .resultCode(200)
                .message("SUCCESS")
                .data(data)
                .build();
    }

}
