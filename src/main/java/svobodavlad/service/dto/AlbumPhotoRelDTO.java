package svobodavlad.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link svobodavlad.domain.AlbumPhotoRel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlbumPhotoRelDTO implements Serializable {

    private Long id;

    private AlbumDTO album;

    private PhotoDTO photo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlbumDTO getAlbum() {
        return album;
    }

    public void setAlbum(AlbumDTO album) {
        this.album = album;
    }

    public PhotoDTO getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoDTO photo) {
        this.photo = photo;
    }

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
    @Override
    public String toString() {
        return "AlbumPhotoRelDTO{" +
            "id=" + getId() +
            ", album=" + getAlbum() +
            ", photo=" + getPhoto() +
            "}";
    }
}
