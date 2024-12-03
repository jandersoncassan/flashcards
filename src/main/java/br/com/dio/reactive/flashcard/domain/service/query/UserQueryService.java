package br.com.dio.reactive.flashcard.domain.service.query;
import br.com.dio.reactive.flashcard.domain.document.UserDocument;
import br.com.dio.reactive.flashcard.domain.exception.BaseErrorMessage;
import br.com.dio.reactive.flashcard.domain.exception.NotFoundException;
import br.com.dio.reactive.flashcard.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static br.com.dio.reactive.flashcard.domain.exception.BaseErrorMessage.USER_NOT_FOUND;


@Service
@Slf4j
@AllArgsConstructor
public class UserQueryService {

  private final UserRepository userRepository;

  public Mono<UserDocument> findById(final String id){
      return userRepository.findById(id)
              .filter(Objects::nonNull)
              .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(USER_NOT_FOUND.params("id", id).getMessage()))))
              .doFirst(() -> log.info(" ==== try to find use with id {}", id));

  }

    public Mono<UserDocument> findByEmail(final String email){
        return userRepository.findByEmail(email)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(USER_NOT_FOUND.params("email", email).getMessage()))))
                .doFirst(() -> log.info(" ==== try to find user with email {}", email));

    }

}
