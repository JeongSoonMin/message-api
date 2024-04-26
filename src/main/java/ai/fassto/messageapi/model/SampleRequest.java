package ai.fassto.messageapi.model;

import ai.fassto.messageapi.entity.Sample;

public record SampleRequest(

) {
    public record SampleAddRequest(
            String name,
            String desc
    ) {
        public Sample toSampleEntity() {
            return Sample.builder()
                    .name(this.name)
                    .desc(this.desc)
                    .build();
        }
    }

    public record SampleModifyRequest(
            String sampleId,
            String sampleName,
            String sampleDescription
    ) {
    }
}
