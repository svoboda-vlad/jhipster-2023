package svobodavlad.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlbumPhotoRel.
 */
@Entity
@Table(name = "album_photo_rel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlbumPhotoRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "albumPhotoRels", "owner" }, allowSetters = true)
    private Album album;

    @ManyToOne
    @JsonIgnoreProperties(value = { "comments", "albumPhotoRels", "owner" }, allowSetters = true)
    private Photo photo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlbumPhotoRel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Album getAlbum() {
        return this.album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public AlbumPhotoRel album(Album album) {
        this.setAlbum(album);
        return this;
    }

    public Photo getPhoto() {
        return this.photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public AlbumPhotoRel photo(Photo photo) {
        this.setPhoto(photo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlbumPhotoRel)) {
            return false;
        }
        return id != null && id.equals(((AlbumPhotoRel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlbumPhotoRel{" +
            "id=" + getId() +
            "}";
    }
}
