package br.com.dio.reactive.flashcard.domain.exception;

public class NotFoundException extends ReactiveFlashcardsException{

    public NotFoundException(String message) {
        super(message);
    }
}
