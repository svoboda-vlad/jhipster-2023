package svobodavlad.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import svobodavlad.domain.AlbumPhotoRel;
import svobodavlad.repository.AlbumPhotoRelRepository;
import svobodavlad.service.dto.AlbumPhotoRelDTO;
import svobodavlad.service.mapper.AlbumPhotoRelMapper;

/**
 * Service Implementation for managing {@link AlbumPhotoRel}.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AlbumPhotoRelService {

    private final Logger log = LoggerFactory.getLogger(AlbumPhotoRelService.class);

    private final AlbumPhotoRelRepository albumPhotoRelRepository;

    private final AlbumPhotoRelMapper albumPhotoRelMapper;

    /**
     * Save a albumPhotoRel.
     *
     * @param albumPhotoRelDTO the entity to save.
     * @return the persisted entity.
     */
    public AlbumPhotoRelDTO save(AlbumPhotoRelDTO albumPhotoRelDTO) {
        log.debug("Request to save AlbumPhotoRel : {}", albumPhotoRelDTO);
        AlbumPhotoRel albumPhotoRel = albumPhotoRelMapper.toEntity(albumPhotoRelDTO);
        albumPhotoRel = albumPhotoRelRepository.save(albumPhotoRel);
        return albumPhotoRelMapper.toDto(albumPhotoRel);
    }

    /**
     * Update a albumPhotoRel.
     *
     * @param albumPhotoRelDTO the entity to save.
     * @return the persisted entity.
     */
    public AlbumPhotoRelDTO update(AlbumPhotoRelDTO albumPhotoRelDTO) {
        log.debug("Request to update AlbumPhotoRel : {}", albumPhotoRelDTO);
        AlbumPhotoRel albumPhotoRel = albumPhotoRelMapper.toEntity(albumPhotoRelDTO);
        albumPhotoRel = albumPhotoRelRepository.save(albumPhotoRel);
        return albumPhotoRelMapper.toDto(albumPhotoRel);
    }

    /**
     * Partially update a albumPhotoRel.
     *
     * @param albumPhotoRelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlbumPhotoRelDTO> partialUpdate(AlbumPhotoRelDTO albumPhotoRelDTO) {
        log.debug("Request to partially update AlbumPhotoRel : {}", albumPhotoRelDTO);

        return albumPhotoRelRepository
            .findById(albumPhotoRelDTO.getId())
            .map(existingAlbumPhotoRel -> {
                albumPhotoRelMapper.partialUpdate(existingAlbumPhotoRel, albumPhotoRelDTO);

                return existingAlbumPhotoRel;
            })
            .map(albumPhotoRelRepository::save)
            .map(albumPhotoRelMapper::toDto);
    }

    /**
     * Get all the albumPhotoRels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AlbumPhotoRelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AlbumPhotoRels");
        return albumPhotoRelRepository.findAll(pageable).map(albumPhotoRelMapper::toDto);
    }

    /**
     * Get one albumPhotoRel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlbumPhotoRelDTO> findOne(Long id) {
        log.debug("Request to get AlbumPhotoRel : {}", id);
        return albumPhotoRelRepository.findById(id).map(albumPhotoRelMapper::toDto);
    }

    /**
     * Delete the albumPhotoRel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AlbumPhotoRel : {}", id);
        albumPhotoRelRepository.deleteById(id);
    }
}
