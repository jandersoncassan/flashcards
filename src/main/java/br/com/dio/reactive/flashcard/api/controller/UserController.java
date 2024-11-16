package br.com.dio.reactive.flashcard.api.controller;

import br.com.dio.reactive.flashcard.api.controller.request.UserRequest;
import br.com.dio.reactive.flashcard.api.controller.response.UserResponse;
import br.com.dio.reactive.flashcard.api.mapper.UserMapper;
import br.com.dio.reactive.flashcard.domain.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<UserResponse> save(@Valid @RequestBody final UserRequest request){
        return userService.save(userMapper.toDocument(request))
                .doFirst(() -> log.info("=== saving a user with follow data {}", request))
                .map(userMapper::toResponse);
    }
}
