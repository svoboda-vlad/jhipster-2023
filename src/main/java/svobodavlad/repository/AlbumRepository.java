package svobodavlad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @EntityGraph(value = "Album.detail", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    Optional<Album> findById(Long id);

    @EntityGraph(value = "Album.noDetail", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    Page<Album> findAll(Pageable pageable);
}
