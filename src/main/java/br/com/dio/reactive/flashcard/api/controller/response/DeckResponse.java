package br.com.dio.reactive.flashcard.api.controller.response;

import br.com.dio.reactive.flashcard.domain.document.Card;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Set;

public record DeckResponse(@JsonProperty("name") String name,
                           @JsonProperty("description") String description,
                           @JsonProperty("cards") Set<Card> cards,
                           @JsonProperty("id") String id){

    @Builder(toBuilder = true)
    public DeckResponse {}
}
