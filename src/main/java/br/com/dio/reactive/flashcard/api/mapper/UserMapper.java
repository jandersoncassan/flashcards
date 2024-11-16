package br.com.dio.reactive.flashcard.api.mapper;

import br.com.dio.reactive.flashcard.api.controller.request.UserRequest;
import br.com.dio.reactive.flashcard.api.controller.response.UserResponse;
import br.com.dio.reactive.flashcard.domain.document.UserDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true) 
    UserDocument toDocument(final UserRequest request);

    UserResponse toResponse(final UserDocument response);
}
