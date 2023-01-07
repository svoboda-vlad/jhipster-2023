package svobodavlad.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import svobodavlad.domain.Album;
import svobodavlad.domain.AlbumPhotoRel;
import svobodavlad.domain.User;
import svobodavlad.service.dto.*;

/**
 * Mapper for the entity {@link Album} and its DTO {@link AlbumDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlbumDetailMapper extends EntityMapper<AlbumDetailDTO, Album> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    @Mapping(target = "albumPhotoRels", source = "albumPhotoRels", qualifiedByName = "albumPhotoRelsSet")
    AlbumDetailDTO toDto(Album s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("albumPhotoRelsSet")
    @IterableMapping(qualifiedByName = "albumPhotoRelsIgnoreAlbum")
    Set<AlbumPhotoRelDTO> toSetDtoAlbumPhotoRel(Set<AlbumPhotoRel> albumPhotoRels);

    @Named("albumPhotoRelsIgnoreAlbum")
    @Mapping(target = "album", ignore = true)
    AlbumPhotoRelDTO toDtoAlbumPhotoRelIgnoreAlbum(AlbumPhotoRel albumPhotoRel);
}
