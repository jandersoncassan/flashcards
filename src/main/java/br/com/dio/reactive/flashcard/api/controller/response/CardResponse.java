package br.com.dio.reactive.flashcard.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record CardResponse(@JsonProperty("front") String front,
                           @JsonProperty("back") String back) {

    @Builder(toBuilder = true)
    public CardResponse {}
}
