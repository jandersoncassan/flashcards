package br.com.dio.reactive.flashcard.domain.service;

import br.com.dio.reactive.flashcard.domain.document.UserDocument;
import br.com.dio.reactive.flashcard.domain.exception.EmailAlreadyUsedException;
import br.com.dio.reactive.flashcard.domain.exception.NotFoundException;
import br.com.dio.reactive.flashcard.domain.repository.UserRepository;
import br.com.dio.reactive.flashcard.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Objects;

import static br.com.dio.reactive.flashcard.domain.exception.BaseErrorMessage.EMAIL_ALREADY_USED;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public Mono<UserDocument> save(final UserDocument document){
        return userQueryService.findByEmail(document.email())
                .doFirst(() -> log.info("==== Try to save a follow user {}", document))
                .filter(Objects::isNull)
                .switchIfEmpty(Mono.defer(() ->Mono.error(new EmailAlreadyUsedException(EMAIL_ALREADY_USED
                        .params(document.email()).getMessage()))))
                .onErrorResume(NotFoundException.class, e -> userRepository.save(document));
    }

    public Mono<UserDocument> update(final UserDocument document){
        return getDataByEmail(document)
                .flatMap(userData -> Mono.just(document.toBuilder()
                                .createdAt(userData.createdAt())
                                .updateAt(OffsetDateTime.now())
                                .build()))
                .flatMap(userRepository::save)
                .doFirst(() -> log.info("==== Try to update a user with follow info {}", document));
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

    private Mono<UserDocument> getDataByEmail(final UserDocument document){
        return userQueryService.findByEmail(document.email())
                .filter(stored -> stored.id().equals(document.id()))
                .switchIfEmpty(Mono.defer(() ->Mono.error(new EmailAlreadyUsedException(EMAIL_ALREADY_USED
                        .params(document.email()).getMessage()))))
                .onErrorResume(Mono::error);
    }
}
