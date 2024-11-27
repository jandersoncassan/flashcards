package br.com.dio.reactive.flashcard.api.controller;

import br.com.dio.reactive.flashcard.api.controller.request.UserRequest;
import br.com.dio.reactive.flashcard.api.controller.response.UserResponse;
import br.com.dio.reactive.flashcard.api.mapper.UserMapper;
import br.com.dio.reactive.flashcard.core.validation.MongoId;
import br.com.dio.reactive.flashcard.domain.service.UserService;
import br.com.dio.reactive.flashcard.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserMapper userMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<UserResponse> save(@Valid @RequestBody final UserRequest request){
        return userService.save(userMapper.toDocument(request))
                .doFirst(() -> log.info("=== saving a user with follow data {}", request))
                .map(userMapper::toResponse);
    }
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Mono<UserResponse> findById( @Valid @PathVariable @MongoId(message = "{userController.id}") final String id){
        return userQueryService.findById(id)
                .doFirst(() -> log.info("=== find user with id {}", id))
                .map(userMapper::toResponse);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Mono<UserResponse> update(@Valid @PathVariable @MongoId(message = "{userController.id}") final String id,
                                     @Valid @RequestBody final UserRequest request){
        return userService.update(userMapper.toDocument(request, id))
                .doFirst(() -> log.info("=== update a user with follow [data: {}, id: {}", request, id))
                .map(userMapper::toResponse);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> delete(@Valid @PathVariable @MongoId(message = "{userController.id}") final String id){
        return userService.delete(id)
                .doFirst(() -> log.info("=== delete a user with id: {}", id));
    }

    @GetMapping(value = "/email/{email}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Mono<UserResponse> findByEmail(@Valid @PathVariable @Email final String email){
        return userQueryService.findByEmail(email)
                .doFirst(() -> log.info("=== find user with email {}", email))
                .map(userMapper::toResponse);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Flux<UserResponse> findAll(){
        return userService.findAll()
                .doFirst(() -> log.info("=== find ALL user"))
                .map(userMapper::toResponse);
    }

}
