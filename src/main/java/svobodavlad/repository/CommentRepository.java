package svobodavlad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import svobodavlad.domain.Comment;

/**
 * Spring Data JPA repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select comment from Comment comment where comment.author.login = ?#{principal.username}")
    List<Comment> findByAuthorIsCurrentUser();

    @EntityGraph(value = "Comment.detail", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    Optional<Comment> findById(Long id);
}
