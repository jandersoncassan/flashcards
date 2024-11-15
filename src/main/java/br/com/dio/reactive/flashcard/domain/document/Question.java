package br.com.dio.reactive.flashcard.domain.document;

import lombok.Builder;

public record Question(String asked,
                       String answered,
                       String expected) {

    @Builder(toBuilder = true)
    public Question{}
}
