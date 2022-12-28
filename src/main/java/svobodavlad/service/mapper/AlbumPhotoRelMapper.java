package svobodavlad.service.mapper;

import org.mapstruct.*;
import svobodavlad.domain.Album;
import svobodavlad.domain.AlbumPhotoRel;
import svobodavlad.domain.Photo;
import svobodavlad.service.dto.AlbumDTO;
import svobodavlad.service.dto.AlbumPhotoRelDTO;
import svobodavlad.service.dto.PhotoDTO;

/**
 * Mapper for the entity {@link AlbumPhotoRel} and its DTO {@link AlbumPhotoRelDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlbumPhotoRelMapper extends EntityMapper<AlbumPhotoRelDTO, AlbumPhotoRel> {
    @Mapping(target = "album", source = "album", qualifiedByName = "albumId")
    @Mapping(target = "photo", source = "photo", qualifiedByName = "photoId")
    AlbumPhotoRelDTO toDto(AlbumPhotoRel s);

    @Named("albumId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlbumDTO toDtoAlbumId(Album album);

    @Named("photoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PhotoDTO toDtoPhotoId(Photo photo);
}
