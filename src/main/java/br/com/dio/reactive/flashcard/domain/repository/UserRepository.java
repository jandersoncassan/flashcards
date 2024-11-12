package br.com.dio.reactive.flashcard.domain.repository;

import br.com.dio.reactive.flashcard.domain.document.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserDocument, String> {
}
