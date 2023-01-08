package svobodavlad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import svobodavlad.domain.Photo;

/**
 * Spring Data JPA repository for the Photo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query("select photo from Photo photo where photo.owner.login = ?#{principal.username}")
    List<Photo> findByOwnerIsCurrentUser();
}
