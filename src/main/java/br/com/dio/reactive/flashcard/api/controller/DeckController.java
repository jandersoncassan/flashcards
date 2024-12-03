package br.com.dio.reactive.flashcard.api.controller;

import br.com.dio.reactive.flashcard.api.controller.request.DeckRequest;
import br.com.dio.reactive.flashcard.api.controller.response.DeckResponse;
import br.com.dio.reactive.flashcard.api.mapper.DeckMapper;
import br.com.dio.reactive.flashcard.core.validation.MongoId;
import br.com.dio.reactive.flashcard.domain.service.DeckService;
import br.com.dio.reactive.flashcard.domain.service.query.DeckQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("decks")
@Slf4j
@AllArgsConstructor
public class DeckController {

    private final DeckService deckService;
    private final DeckQueryService deckQueryService;
    private final DeckMapper deckMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<DeckResponse> save(@Valid @RequestBody DeckRequest request){
        return deckService.save(deckMapper.toDocument(request))
                .doFirst(() -> log.info("=== saving a deck with follow data {}", request))
                .map(deckMapper::toResponse);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Mono<DeckResponse> findById(@Valid @PathVariable @MongoId(message = "{userController.id}") final String id){
        return deckQueryService.findById(id)
                .doFirst(() -> log.info(" ==== finding a deck with follow id {}", id))
                .map(deckMapper::toResponse);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Flux<DeckResponse> findAll(){
        return deckService.findAll()
                .doFirst(() -> log.info("=== finding all decks "))
                .map(deckMapper::toResponse);
    }


}
