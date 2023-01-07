package svobodavlad.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link svobodavlad.domain.Photo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PhotoDetailDTO extends PhotoDTO implements Serializable {

    private Set<CommentDTO> comments = new HashSet<>();

    public Set<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(Set<CommentDTO> comments) {
        this.comments = comments;
    }

    private Set<AlbumPhotoRelDTO> albumPhotoRels = new HashSet<>();

    public Set<AlbumPhotoRelDTO> getAlbumPhotoRels() {
        return albumPhotoRels;
    }

    public void setAlbumPhotoRels(Set<AlbumPhotoRelDTO> albumPhotoRels) {
        this.albumPhotoRels = albumPhotoRels;
    }
}
