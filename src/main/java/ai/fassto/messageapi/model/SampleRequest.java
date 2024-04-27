package ai.fassto.messageapi.model;

import ai.fassto.messageapi.entity.Sample;

public record SampleRequest(

) {
    public record SampleAddRequest(
            String sampleName,
            String sampleDescription
    ) {
        public Sample toSampleEntity() {
            return Sample.builder()
                    .name(this.sampleName)
                    .desc(this.sampleDescription)
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
