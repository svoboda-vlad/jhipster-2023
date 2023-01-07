package svobodavlad.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import svobodavlad.domain.AlbumPhotoRel;
import svobodavlad.domain.Comment;
import svobodavlad.domain.Photo;
import svobodavlad.domain.User;
import svobodavlad.service.dto.AlbumPhotoRelDTO;
import svobodavlad.service.dto.CommentDTO;
import svobodavlad.service.dto.PhotoDetailDTO;
import svobodavlad.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Photo} and its DTO {@link PhotoDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface PhotoDetailMapper extends EntityMapper<PhotoDetailDTO, Photo> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    @Mapping(target = "comments", source = "comments", qualifiedByName = "commentsSet")
    @Mapping(target = "albumPhotoRels", source = "albumPhotoRels", qualifiedByName = "albumPhotoRelsSet")
    PhotoDetailDTO toDto(Photo s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("commentsSet")
    @IterableMapping(qualifiedByName = "commentIgnorePhoto")
    Set<CommentDTO> toSetDtoComments(Set<Comment> comments);

    @Named("commentIgnorePhoto")
    @Mapping(target = "photo", ignore = true)
    CommentDTO toDtoCommentIgnorePhoto(Comment comment);

    @Named("albumPhotoRelsSet")
    @IterableMapping(qualifiedByName = "albumPhotoRelsIgnorePhoto")
    Set<AlbumPhotoRelDTO> toSetDtoAlbumPhotoRel(Set<AlbumPhotoRel> albumPhotoRels);

    @Named("albumPhotoRelsIgnorePhoto")
    @Mapping(target = "photo", ignore = true)
    AlbumPhotoRelDTO toDtoAlbumPhotoRelIgnoreAlbum(AlbumPhotoRel albumPhotoRel);
}
