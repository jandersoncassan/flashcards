package br.com.dio.reactive.flashcard.domain.service;

import br.com.dio.reactive.flashcard.domain.document.UserDocument;
import br.com.dio.reactive.flashcard.domain.repository.UserRepository;
import br.com.dio.reactive.flashcard.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public Mono<UserDocument> save(final UserDocument document){
        return userRepository.save(document)
                .doFirst(() -> log.info(" === try to save a follow user {}", document));
    }

    public Mono<UserDocument> update(final UserDocument document){
        return userQueryService.findById(document.id())
                .map(user -> document.toBuilder()
                        .createdAt(user.createdAt())
                        .updateAt(OffsetDateTime.now())
                        .build())
                .flatMap(userRepository::save)
                .doFirst(() -> log.info( " ==== try to update user with follow info {}", document));
    }

    public Mono<Void> delete(final String id){
        return userQueryService.findById(id)
                .flatMap(userRepository::delete)
                .doFirst(() -> log.info( " ==== try to delete user with id {}", id))
                .doOnSuccess(d -> log.info("delete user with id {} success", id));


    }

    public Flux<UserDocument> findAll(){
        return userRepository.findAll()
                .doFirst(() -> log.info( " ==== get all users"))
                .doOnError(e -> log.error("=== an error occurred in get all users"));
    }
}
