package svobodavlad.service.mapper;

import org.mapstruct.*;
import svobodavlad.domain.Album;
import svobodavlad.domain.User;
import svobodavlad.service.dto.AlbumDTO;
import svobodavlad.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Album} and its DTO {@link AlbumDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlbumMapper extends EntityMapper<AlbumDTO, Album> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    AlbumDTO toDto(Album s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
