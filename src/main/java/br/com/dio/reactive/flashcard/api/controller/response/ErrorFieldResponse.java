package br.com.dio.reactive.flashcard.api.controller.response;

import lombok.Builder;

public record ErrorFieldResponse(String name,
                                 String message) {

    @Builder(toBuilder = true)
    public ErrorFieldResponse { }
}
