package svobodavlad.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Photo.
 */
@Entity
@Table(name = "photo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
@NamedEntityGraph(
    name = "Photo.detail",
    attributeNodes = { @NamedAttributeNode("comments"), @NamedAttributeNode("owner"), @NamedAttributeNode("albumPhotoRels") }
)
@NamedEntityGraph(name = "Photo.noDetail", attributeNodes = { @NamedAttributeNode("owner") })
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @NotNull
    @Column(name = "file_size", precision = 21, scale = 2, nullable = false)
    private BigDecimal fileSize;

    @NotNull
    @Column(name = "width", nullable = false)
    private Integer width;

    @NotNull
    @Column(name = "height", nullable = false)
    private Integer height;

    @NotNull
    @Column(name = "creation_date_time", nullable = false)
    private Instant creationDateTime;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "photo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "author", "photo" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "photo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "album", "photo" }, allowSetters = true)
    private Set<AlbumPhotoRel> albumPhotoRels = new HashSet<>();

    @ManyToOne
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Photo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Photo fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BigDecimal getFileSize() {
        return this.fileSize;
    }

    public Photo fileSize(BigDecimal fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Photo width(Integer width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Photo height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Instant getCreationDateTime() {
        return this.creationDateTime;
    }

    public Photo creationDateTime(Instant creationDateTime) {
        this.setCreationDateTime(creationDateTime);
        return this;
    }

    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getDescription() {
        return this.description;
    }

    public Photo description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setPhoto(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setPhoto(this));
        }
        this.comments = comments;
    }

    public Photo comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Photo addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPhoto(this);
        return this;
    }

    public Photo removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setPhoto(null);
        return this;
    }

    public Set<AlbumPhotoRel> getAlbumPhotoRels() {
        return this.albumPhotoRels;
    }

    public void setAlbumPhotoRels(Set<AlbumPhotoRel> albumPhotoRels) {
        if (this.albumPhotoRels != null) {
            this.albumPhotoRels.forEach(i -> i.setPhoto(null));
        }
        if (albumPhotoRels != null) {
            albumPhotoRels.forEach(i -> i.setPhoto(this));
        }
        this.albumPhotoRels = albumPhotoRels;
    }

    public Photo albumPhotoRels(Set<AlbumPhotoRel> albumPhotoRels) {
        this.setAlbumPhotoRels(albumPhotoRels);
        return this;
    }

    public Photo addAlbumPhotoRel(AlbumPhotoRel albumPhotoRel) {
        this.albumPhotoRels.add(albumPhotoRel);
        albumPhotoRel.setPhoto(this);
        return this;
    }

    public Photo removeAlbumPhotoRel(AlbumPhotoRel albumPhotoRel) {
        this.albumPhotoRels.remove(albumPhotoRel);
        albumPhotoRel.setPhoto(null);
        return this;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Photo owner(User user) {
        this.setOwner(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Photo)) {
            return false;
        }
        return id != null && id.equals(((Photo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Photo{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileSize=" + getFileSize() +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", creationDateTime='" + getCreationDateTime() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
