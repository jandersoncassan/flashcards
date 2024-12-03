package br.com.dio.reactive.flashcard.domain.exception;

public class EmailAlreadyUsedException extends ReactiveFlashcardsException{

    public EmailAlreadyUsedException(final String message) {
        super(message);
    }

}
