package ai.fassto.messageapi.persistence;

import ai.fassto.messageapi.entity.Sample;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends ReactiveCrudRepository<Sample, String> {

}
