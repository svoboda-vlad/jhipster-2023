package svobodavlad.service.mapper;

import org.mapstruct.*;
import svobodavlad.domain.Photo;
import svobodavlad.domain.User;
import svobodavlad.service.dto.PhotoDTO;
import svobodavlad.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Photo} and its DTO {@link PhotoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PhotoMapper extends EntityMapper<PhotoDTO, Photo> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    PhotoDTO toDto(Photo s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
