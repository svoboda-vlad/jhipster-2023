package svobodavlad.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import svobodavlad.repository.AlbumPhotoRelRepository;
import svobodavlad.service.AlbumPhotoRelService;
import svobodavlad.service.dto.AlbumPhotoRelDTO;
import svobodavlad.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link svobodavlad.domain.AlbumPhotoRel}.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AlbumPhotoRelResource {

    private final Logger log = LoggerFactory.getLogger(AlbumPhotoRelResource.class);

    private static final String ENTITY_NAME = "albumPhotoRel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlbumPhotoRelService albumPhotoRelService;

    private final AlbumPhotoRelRepository albumPhotoRelRepository;

    /**
     * {@code POST  /album-photo-rels} : Create a new albumPhotoRel.
     *
     * @param albumPhotoRelDTO the albumPhotoRelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new albumPhotoRelDTO, or with status {@code 400 (Bad Request)} if the albumPhotoRel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/album-photo-rels")
    public ResponseEntity<AlbumPhotoRelDTO> createAlbumPhotoRel(@RequestBody AlbumPhotoRelDTO albumPhotoRelDTO) throws URISyntaxException {
        log.debug("REST request to save AlbumPhotoRel : {}", albumPhotoRelDTO);
        if (albumPhotoRelDTO.getId() != null) {
            throw new BadRequestAlertException("A new albumPhotoRel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlbumPhotoRelDTO result = albumPhotoRelService.save(albumPhotoRelDTO);
        return ResponseEntity
            .created(new URI("/api/album-photo-rels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /album-photo-rels/:id} : Updates an existing albumPhotoRel.
     *
     * @param id the id of the albumPhotoRelDTO to save.
     * @param albumPhotoRelDTO the albumPhotoRelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated albumPhotoRelDTO,
     * or with status {@code 400 (Bad Request)} if the albumPhotoRelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the albumPhotoRelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/album-photo-rels/{id}")
    public ResponseEntity<AlbumPhotoRelDTO> updateAlbumPhotoRel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlbumPhotoRelDTO albumPhotoRelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AlbumPhotoRel : {}, {}", id, albumPhotoRelDTO);
        if (albumPhotoRelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, albumPhotoRelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!albumPhotoRelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AlbumPhotoRelDTO result = albumPhotoRelService.update(albumPhotoRelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, albumPhotoRelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /album-photo-rels/:id} : Partial updates given fields of an existing albumPhotoRel, field will ignore if it is null
     *
     * @param id the id of the albumPhotoRelDTO to save.
     * @param albumPhotoRelDTO the albumPhotoRelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated albumPhotoRelDTO,
     * or with status {@code 400 (Bad Request)} if the albumPhotoRelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the albumPhotoRelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the albumPhotoRelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/album-photo-rels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlbumPhotoRelDTO> partialUpdateAlbumPhotoRel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlbumPhotoRelDTO albumPhotoRelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AlbumPhotoRel partially : {}, {}", id, albumPhotoRelDTO);
        if (albumPhotoRelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, albumPhotoRelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!albumPhotoRelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlbumPhotoRelDTO> result = albumPhotoRelService.partialUpdate(albumPhotoRelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, albumPhotoRelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /album-photo-rels} : get all the albumPhotoRels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of albumPhotoRels in body.
     */
    @GetMapping("/album-photo-rels")
    public ResponseEntity<List<AlbumPhotoRelDTO>> getAllAlbumPhotoRels(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AlbumPhotoRels");
        Page<AlbumPhotoRelDTO> page = albumPhotoRelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /album-photo-rels/:id} : get the "id" albumPhotoRel.
     *
     * @param id the id of the albumPhotoRelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the albumPhotoRelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/album-photo-rels/{id}")
    public ResponseEntity<AlbumPhotoRelDTO> getAlbumPhotoRel(@PathVariable Long id) {
        log.debug("REST request to get AlbumPhotoRel : {}", id);
        Optional<AlbumPhotoRelDTO> albumPhotoRelDTO = albumPhotoRelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(albumPhotoRelDTO);
    }

    /**
     * {@code DELETE  /album-photo-rels/:id} : delete the "id" albumPhotoRel.
     *
     * @param id the id of the albumPhotoRelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/album-photo-rels/{id}")
    public ResponseEntity<Void> deleteAlbumPhotoRel(@PathVariable Long id) {
        log.debug("REST request to delete AlbumPhotoRel : {}", id);
        albumPhotoRelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
