package svobodavlad.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import svobodavlad.domain.AlbumPhotoRel;

/**
 * Spring Data JPA repository for the AlbumPhotoRel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlbumPhotoRelRepository extends JpaRepository<AlbumPhotoRel, Long> {}
