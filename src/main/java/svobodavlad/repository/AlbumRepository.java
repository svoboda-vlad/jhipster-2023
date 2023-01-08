package svobodavlad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import svobodavlad.domain.Album;

/**
 * Spring Data JPA repository for the Album entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    @Query("select album from Album album where album.owner.login = ?#{principal.username}")
    List<Album> findByOwnerIsCurrentUser();
}
