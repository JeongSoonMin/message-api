package ai.fassto.messageapi.model;

import ai.fassto.messageapi.entity.Sample;

public record SampleResponse() {

    public record SampleAddResponse(
            String sampleName,
            String sampleDesc
    ) {
        public static SampleAddResponse of(Sample sample) {
            return new SampleAddResponse(sample.getName(), sample.getDesc());
        }
    }

    public record SampleModifyResponse(
            String sampleId
    ) {
        public static SampleModifyResponse of(Sample sample) {
            return new SampleModifyResponse(sample.getId());
        }
    }
}
