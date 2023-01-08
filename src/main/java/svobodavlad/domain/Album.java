package svobodavlad.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Album.
 */
@Entity
@Table(name = "album")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "album_name", nullable = false)
    private String albumName;

    @OneToMany(mappedBy = "album")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "album", "photo" }, allowSetters = true)
    private Set<AlbumPhotoRel> albumPhotoRels = new HashSet<>();

    @ManyToOne
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Album id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public Album albumName(String albumName) {
        this.setAlbumName(albumName);
        return this;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Set<AlbumPhotoRel> getAlbumPhotoRels() {
        return this.albumPhotoRels;
    }

    public void setAlbumPhotoRels(Set<AlbumPhotoRel> albumPhotoRels) {
        if (this.albumPhotoRels != null) {
            this.albumPhotoRels.forEach(i -> i.setAlbum(null));
        }
        if (albumPhotoRels != null) {
            albumPhotoRels.forEach(i -> i.setAlbum(this));
        }
        this.albumPhotoRels = albumPhotoRels;
    }

    public Album albumPhotoRels(Set<AlbumPhotoRel> albumPhotoRels) {
        this.setAlbumPhotoRels(albumPhotoRels);
        return this;
    }

    public Album addAlbumPhotoRel(AlbumPhotoRel albumPhotoRel) {
        this.albumPhotoRels.add(albumPhotoRel);
        albumPhotoRel.setAlbum(this);
        return this;
    }

    public Album removeAlbumPhotoRel(AlbumPhotoRel albumPhotoRel) {
        this.albumPhotoRels.remove(albumPhotoRel);
        albumPhotoRel.setAlbum(null);
        return this;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Album owner(User user) {
        this.setOwner(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Album)) {
            return false;
        }
        return id != null && id.equals(((Album) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Album{" +
            "id=" + getId() +
            ", albumName='" + getAlbumName() + "'" +
            "}";
    }
}
