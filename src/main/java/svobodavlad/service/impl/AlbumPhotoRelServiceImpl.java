package svobodavlad.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import svobodavlad.domain.AlbumPhotoRel;
import svobodavlad.repository.AlbumPhotoRelRepository;
import svobodavlad.service.AlbumPhotoRelService;
import svobodavlad.service.dto.AlbumPhotoRelDTO;
import svobodavlad.service.mapper.AlbumPhotoRelMapper;

/**
 * Service Implementation for managing {@link AlbumPhotoRel}.
 */
@Service
@Transactional
public class AlbumPhotoRelServiceImpl implements AlbumPhotoRelService {

    private final Logger log = LoggerFactory.getLogger(AlbumPhotoRelServiceImpl.class);

    private final AlbumPhotoRelRepository albumPhotoRelRepository;

    private final AlbumPhotoRelMapper albumPhotoRelMapper;

    public AlbumPhotoRelServiceImpl(AlbumPhotoRelRepository albumPhotoRelRepository, AlbumPhotoRelMapper albumPhotoRelMapper) {
        this.albumPhotoRelRepository = albumPhotoRelRepository;
        this.albumPhotoRelMapper = albumPhotoRelMapper;
    }

    @Override
    public AlbumPhotoRelDTO save(AlbumPhotoRelDTO albumPhotoRelDTO) {
        log.debug("Request to save AlbumPhotoRel : {}", albumPhotoRelDTO);
        AlbumPhotoRel albumPhotoRel = albumPhotoRelMapper.toEntity(albumPhotoRelDTO);
        albumPhotoRel = albumPhotoRelRepository.save(albumPhotoRel);
        return albumPhotoRelMapper.toDto(albumPhotoRel);
    }

    @Override
    public AlbumPhotoRelDTO update(AlbumPhotoRelDTO albumPhotoRelDTO) {
        log.debug("Request to update AlbumPhotoRel : {}", albumPhotoRelDTO);
        AlbumPhotoRel albumPhotoRel = albumPhotoRelMapper.toEntity(albumPhotoRelDTO);
        albumPhotoRel = albumPhotoRelRepository.save(albumPhotoRel);
        return albumPhotoRelMapper.toDto(albumPhotoRel);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public Page<AlbumPhotoRelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AlbumPhotoRels");
        return albumPhotoRelRepository.findAll(pageable).map(albumPhotoRelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlbumPhotoRelDTO> findOne(Long id) {
        log.debug("Request to get AlbumPhotoRel : {}", id);
        return albumPhotoRelRepository.findById(id).map(albumPhotoRelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AlbumPhotoRel : {}", id);
        albumPhotoRelRepository.deleteById(id);
    }
}
