package svobodavlad.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import svobodavlad.domain.Photo;
import svobodavlad.repository.PhotoRepository;
import svobodavlad.service.dto.PhotoDTO;
import svobodavlad.service.dto.PhotoDetailDTO;
import svobodavlad.service.mapper.PhotoDetailMapper;
import svobodavlad.service.mapper.PhotoMapper;

/**
 * Service Implementation for managing {@link Photo}.
 */
@Service
@Transactional
public class PhotoService {

    private final Logger log = LoggerFactory.getLogger(PhotoService.class);

    private final PhotoRepository photoRepository;

    private final PhotoMapper photoMapper;
    private final PhotoDetailMapper photoDetailMapper;

    public PhotoService(PhotoRepository photoRepository, PhotoMapper photoMapper, PhotoDetailMapper photoDetailMapper) {
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
        this.photoDetailMapper = photoDetailMapper;
    }

    /**
     * Save a photo.
     *
     * @param photoDTO the entity to save.
     * @return the persisted entity.
     */
    public PhotoDTO save(PhotoDTO photoDTO) {
        log.debug("Request to save Photo : {}", photoDTO);
        Photo photo = photoMapper.toEntity(photoDTO);
        photo = photoRepository.save(photo);
        return photoMapper.toDto(photo);
    }

    /**
     * Update a photo.
     *
     * @param photoDTO the entity to save.
     * @return the persisted entity.
     */
    public PhotoDTO update(PhotoDTO photoDTO) {
        log.debug("Request to update Photo : {}", photoDTO);
        Photo photo = photoMapper.toEntity(photoDTO);
        photo = photoRepository.save(photo);
        return photoMapper.toDto(photo);
    }

    /**
     * Partially update a photo.
     *
     * @param photoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PhotoDTO> partialUpdate(PhotoDTO photoDTO) {
        log.debug("Request to partially update Photo : {}", photoDTO);

        return photoRepository
            .findById(photoDTO.getId())
            .map(existingPhoto -> {
                photoMapper.partialUpdate(existingPhoto, photoDTO);

                return existingPhoto;
            })
            .map(photoRepository::save)
            .map(photoMapper::toDto);
    }

    /**
     * Get all the photos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PhotoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Photos");
        return photoRepository.findAll(pageable).map(photoMapper::toDto);
    }

    /**
     * Get one photo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PhotoDetailDTO> findOne(Long id) {
        log.debug("Request to get Photo : {}", id);
        return photoRepository.findById(id).map(photoDetailMapper::toDto);
    }

    /**
     * Delete the photo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Photo : {}", id);
        photoRepository.deleteById(id);
    }
}
