package br.com.dio.reactive.flashcard.api.controller.handler;

import br.com.dio.reactive.flashcard.api.controller.response.ProblemResponse;
import br.com.dio.reactive.flashcard.domain.exception.BaseErrorMessage;
import br.com.dio.reactive.flashcard.domain.exception.NotFoundException;
import br.com.dio.reactive.flashcard.domain.exception.ReactiveFlashcardsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import static br.com.dio.reactive.flashcard.domain.exception.BaseErrorMessage.*;
import static org.springframework.http.HttpStatus.*;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class ApiExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper mapper;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable ex) {
        return Mono.error(ex)
                .onErrorResume(MethodNotAllowedException.class, exception -> handleException(exchange, exception, METHOD_NOT_ALLOWED, GENERIC_METHOD_NOT_ALLOW))
                .onErrorResume(NotFoundException.class, exception -> handleException(exchange, exception, NOT_FOUND, GENERIC_NOT_FOUND))
                .onErrorResume(ReactiveFlashcardsException.class, exception -> handleException(exchange, exception, INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION))
                .onErrorResume(Exception.class, exception -> handleException(exchange, exception, INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION))
                .onErrorResume(JsonProcessingException.class, exception -> handleException(exchange, exception, INTERNAL_SERVER_ERROR, GENERIC_EXCEPTION))
                .then();
    }

    private Mono<Void> handleException(final ServerWebExchange exchange, final Exception exception,
                                       final HttpStatus status, BaseErrorMessage errorMessage){
        return Mono.fromCallable(() -> {
            log.info("=== exception occurred");
                    prepareExchange(exchange, status);
                    return errorMessage.getMessage();
                }).map(message -> buildError(status, message))
                .doFirst(() -> log.error("=== Error details method: [{}] path: [{}] httpStatus[{}] ",
                        exchange.getRequest().getMethod(), exchange.getRequest().getPath().value(), status.name(), exception))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange, ProblemResponse problemResponse) {
        return exchange.getResponse()
                .writeWith(Mono.fromCallable(() -> new DefaultDataBufferFactory().wrap(mapper.writeValueAsBytes(problemResponse))));
    }

    private void prepareExchange(final ServerWebExchange exchange, final HttpStatus statusCode){
        exchange.getResponse().setStatusCode(statusCode);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
    }

    private ProblemResponse buildError(final HttpStatus status, final String errorDescription){
        return ProblemResponse.builder()
                .status(status.value())
                .errorDescription(errorDescription)
                .build();
    }
}
