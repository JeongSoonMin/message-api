package ai.fassto.messageapi.repository;

import ai.fassto.messageapi.entity.EmailSend;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailSendRequestRepository extends ReactiveCrudRepository<EmailSend, String> {

}
