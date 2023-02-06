package svobodavlad.service.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A DTO for the {@link svobodavlad.domain.AlbumPhotoRel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AlbumPhotoRelDTO implements Serializable {

    private Long id;

    private AlbumDTO album;

    private PhotoDTO photo;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlbumPhotoRelDTO)) {
            return false;
        }

        AlbumPhotoRelDTO albumPhotoRelDTO = (AlbumPhotoRelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, albumPhotoRelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    // prettier-ignore
}
