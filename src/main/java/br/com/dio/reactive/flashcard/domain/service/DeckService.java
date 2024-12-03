package br.com.dio.reactive.flashcard.domain.service;

import br.com.dio.reactive.flashcard.domain.document.DeckDocument;
import br.com.dio.reactive.flashcard.domain.repository.DeckRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;

    public Mono<DeckDocument> save(final DeckDocument document){
        return deckRepository.save(document)
                .doFirst(() -> log.info(" === try to save a follow deck {}", document));
    }

    public Flux<DeckDocument> findAll(){
        return deckRepository.findAll()
                .doFirst(() -> log.info( " ==== get all decks"))
                .doOnError(e -> log.error("=== an error occurred in get all decks"));
    }
}
