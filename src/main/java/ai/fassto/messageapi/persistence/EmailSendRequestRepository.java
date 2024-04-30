package ai.fassto.messageapi.persistence;

import ai.fassto.messageapi.entity.EmailSend;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EmailSendRequestRepository extends ReactiveCrudRepository<EmailSend, String> {

    Flux<EmailSend> findAllBy(Pageable pageable);
}
