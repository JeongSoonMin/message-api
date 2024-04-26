package ai.fassto.messageapi.handler;

import ai.fassto.messageapi.entity.Sample;
import ai.fassto.messageapi.model.CommonResponse;
import ai.fassto.messageapi.model.SampleRequest.SampleAddRequest;
import ai.fassto.messageapi.model.SampleRequest.SampleModifyRequest;
import ai.fassto.messageapi.model.SampleResponse.SampleAddResponse;
import ai.fassto.messageapi.model.SampleResponse.SampleModifyResponse;
import ai.fassto.messageapi.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class SampleHandler {

    private final SampleRepository sampleRepository;

    public Mono<ServerResponse> sampleList(ServerRequest request) {
        return CommonResponse.ok(sampleRepository.findAll());
    }

    public Mono<ServerResponse> sampleAdd(ServerRequest request) {
        return CommonResponse.ok(
                request.bodyToMono(SampleAddRequest.class)
                        .map(SampleAddRequest::toSampleEntity)
                        .flatMap(sampleRepository::save)
                        .map(SampleAddResponse::of)
        );
    }

    public Mono<ServerResponse> sampleModify(ServerRequest request) {
        Mono<Sample> sampleMono = sampleRepository.findById(request.pathVariable("sampleId"))
                .switchIfEmpty(Mono.error(new NotFoundException()));
        Mono<SampleModifyRequest> sampleModifyRequestMono = request.bodyToMono(SampleModifyRequest.class);

        return CommonResponse.ok(Mono.zip(
                        (data) -> {
                            Sample db = (Sample) data[0];
                            SampleModifyRequest param = (SampleModifyRequest) data[1];
                            db.setName(param.sampleName());
                            db.setDesc(param.sampleDescription());
                            return db;
                        },
                        sampleMono,
                        sampleModifyRequestMono
                )
                .cast(Sample.class)
                .flatMap(this.sampleRepository::save)
                .map(SampleModifyResponse::of)
        );
    }

    public Mono<ServerResponse> sampleRemove(ServerRequest request) {
        String id = request.pathVariable("sampleId");
        return CommonResponse.okVoid(sampleRepository.deleteById(id)
                .switchIfEmpty(Mono.error(new NotFoundException())));
    }

    public Mono<ServerResponse> sampleRemove2(ServerRequest request) {
        sampleRepository.findById(request.pathVariable("sampleId"))
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .map(sampleRepository::delete);
        return CommonResponse.ok();
    }

}
