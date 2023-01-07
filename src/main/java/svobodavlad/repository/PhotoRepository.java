package svobodavlad.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @EntityGraph(value = "Photo.detail", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    Optional<Photo> findById(Long id);

    @EntityGraph(value = "Photo.noDetail", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    Page<Photo> findAll(Pageable pageable);
}
