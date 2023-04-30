package svobodavlad.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import svobodavlad.service.dto.AlbumPhotoRelDTO;

/**
 * Service Interface for managing {@link svobodavlad.domain.AlbumPhotoRel}.
 */
public interface AlbumPhotoRelService {
    /**
     * Save a albumPhotoRel.
     *
     * @param albumPhotoRelDTO the entity to save.
     * @return the persisted entity.
     */
    AlbumPhotoRelDTO save(AlbumPhotoRelDTO albumPhotoRelDTO);

    /**
     * Updates a albumPhotoRel.
     *
     * @param albumPhotoRelDTO the entity to update.
     * @return the persisted entity.
     */
    AlbumPhotoRelDTO update(AlbumPhotoRelDTO albumPhotoRelDTO);

    /**
     * Partially updates a albumPhotoRel.
     *
     * @param albumPhotoRelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlbumPhotoRelDTO> partialUpdate(AlbumPhotoRelDTO albumPhotoRelDTO);

    /**
     * Get all the albumPhotoRels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlbumPhotoRelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" albumPhotoRel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlbumPhotoRelDTO> findOne(Long id);

    /**
     * Delete the "id" albumPhotoRel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
