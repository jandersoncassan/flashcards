package br.com.dio.reactive.flashcard.api.controller.handler;

import br.com.dio.reactive.flashcard.domain.exception.EmailAlreadyUsedException;
import br.com.dio.reactive.flashcard.domain.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Component
public class EmailAlreadyUsedHandler extends AbstractHandleException<EmailAlreadyUsedException>{

    public EmailAlreadyUsedHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handlerException(final ServerWebExchange exchange, final EmailAlreadyUsedException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, BAD_REQUEST);
                    return ex.getMessage();
                }).map(message -> buildError(BAD_REQUEST, message, exchange))
                .doFirst(() -> log.error("==== NotFoundException", ex))
                .flatMap(response -> writeResponse(exchange, response));
    }
}
