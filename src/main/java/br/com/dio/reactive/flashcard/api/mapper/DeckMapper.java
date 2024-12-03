package br.com.dio.reactive.flashcard.api.mapper;

import br.com.dio.reactive.flashcard.api.controller.request.DeckRequest;
import br.com.dio.reactive.flashcard.api.controller.request.UserRequest;
import br.com.dio.reactive.flashcard.api.controller.response.DeckResponse;
import br.com.dio.reactive.flashcard.api.controller.response.UserResponse;
import br.com.dio.reactive.flashcard.domain.document.DeckDocument;
import br.com.dio.reactive.flashcard.domain.document.UserDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeckMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    DeckDocument toDocument(final DeckRequest request);

    DeckResponse toResponse(final DeckDocument response);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    DeckDocument toDocument(final DeckRequest request, final String id);
}
