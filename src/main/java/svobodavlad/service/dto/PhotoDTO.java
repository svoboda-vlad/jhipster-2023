package svobodavlad.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A DTO for the {@link svobodavlad.domain.Photo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PhotoDTO implements Serializable {

    private Long id;

    @NotNull
    private String fileName;

    @NotNull
    private BigDecimal fileSize;

    @NotNull
    private Integer width;

    @NotNull
    private Integer height;

    @NotNull
    private Instant creationDateTime;

    private String description;

    private UserDTO owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhotoDTO)) {
            return false;
        }

        PhotoDTO photoDTO = (PhotoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, photoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    // prettier-ignore
}
