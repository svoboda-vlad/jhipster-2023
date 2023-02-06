package svobodavlad.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A DTO for the {@link svobodavlad.domain.Album} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AlbumDTO implements Serializable {

    private Long id;

    @NotNull
    private String albumName;

    private UserDTO owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlbumDTO)) {
            return false;
        }

        AlbumDTO albumDTO = (AlbumDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, albumDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    // prettier-ignore
}
