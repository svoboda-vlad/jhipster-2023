package svobodavlad.service.mapper;

import org.mapstruct.*;
import svobodavlad.domain.Comment;
import svobodavlad.domain.Photo;
import svobodavlad.domain.User;
import svobodavlad.service.dto.CommentDTO;
import svobodavlad.service.dto.PhotoDTO;
import svobodavlad.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    @Mapping(target = "author", source = "author", qualifiedByName = "userId")
    @Mapping(target = "photo", source = "photo", qualifiedByName = "photoId")
    CommentDTO toDto(Comment s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("photoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PhotoDTO toDtoPhotoId(Photo photo);
}
