package svobodavlad.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import svobodavlad.domain.AlbumPhotoRel;

/**
 * A DTO for the {@link svobodavlad.domain.Album} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlbumDetailDTO extends AlbumDTO implements Serializable {

    private Set<AlbumPhotoRelDTO> albumPhotoRels = new HashSet<>();

    public Set<AlbumPhotoRelDTO> getAlbumPhotoRels() {
        return albumPhotoRels;
    }

    public void setAlbumPhotoRels(Set<AlbumPhotoRelDTO> albumPhotoRels) {
        this.albumPhotoRels = albumPhotoRels;
    }
}
