package br.com.dio.reactive.flashcard.domain.service.query;

import br.com.dio.reactive.flashcard.domain.document.DeckDocument;
import br.com.dio.reactive.flashcard.domain.document.UserDocument;
import br.com.dio.reactive.flashcard.domain.exception.BaseErrorMessage;
import br.com.dio.reactive.flashcard.domain.exception.NotFoundException;
import br.com.dio.reactive.flashcard.domain.repository.DeckRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class DeckQueryService {

    private final DeckRepository deckRepository;

    public Mono<DeckDocument> findById(final String id){
        return deckRepository.findById(id)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(BaseErrorMessage.DECK_NOT_FOUND.params("id", id).getMessage()))))
                .doFirst(() -> log.info(" ==== try to find deck with id {}", id));

    }

}
