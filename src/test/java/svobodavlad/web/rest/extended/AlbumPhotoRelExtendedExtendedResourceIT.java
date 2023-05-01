package svobodavlad.web.rest.extended;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import svobodavlad.IntegrationTest;
import svobodavlad.domain.AlbumPhotoRel;
import svobodavlad.repository.AlbumPhotoRelRepository;
import svobodavlad.service.dto.AlbumPhotoRelDTO;
import svobodavlad.service.mapper.AlbumPhotoRelMapper;
import svobodavlad.web.rest.AlbumPhotoRelResource;
import svobodavlad.web.rest.TestUtil;

/**
 * Integration tests for the {@link AlbumPhotoRelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlbumPhotoRelExtendedExtendedResourceIT {

    private static final String ENTITY_API_URL = "/api/v1/album-photo-rels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlbumPhotoRelRepository albumPhotoRelRepository;

    @Autowired
    private AlbumPhotoRelMapper albumPhotoRelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlbumPhotoRelMockMvc;

    private AlbumPhotoRel albumPhotoRel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlbumPhotoRel createEntity(EntityManager em) {
        AlbumPhotoRel albumPhotoRel = new AlbumPhotoRel();
        return albumPhotoRel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlbumPhotoRel createUpdatedEntity(EntityManager em) {
        AlbumPhotoRel albumPhotoRel = new AlbumPhotoRel();
        return albumPhotoRel;
    }

    @BeforeEach
    public void initTest() {
        albumPhotoRel = createEntity(em);
    }

    @Test
    @Transactional
    void createAlbumPhotoRel() throws Exception {
        int databaseSizeBeforeCreate = albumPhotoRelRepository.findAll().size();
        // Create the AlbumPhotoRel
        AlbumPhotoRelDTO albumPhotoRelDTO = albumPhotoRelMapper.toDto(albumPhotoRel);
        restAlbumPhotoRelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(albumPhotoRelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AlbumPhotoRel in the database
        List<AlbumPhotoRel> albumPhotoRelList = albumPhotoRelRepository.findAll();
        assertThat(albumPhotoRelList).hasSize(databaseSizeBeforeCreate + 1);
        AlbumPhotoRel testAlbumPhotoRel = albumPhotoRelList.get(albumPhotoRelList.size() - 1);
    }

    @Test
    @Transactional
    void createAlbumPhotoRelWithExistingId() throws Exception {
        // Create the AlbumPhotoRel with an existing ID
        albumPhotoRel.setId(1L);
        AlbumPhotoRelDTO albumPhotoRelDTO = albumPhotoRelMapper.toDto(albumPhotoRel);

        int databaseSizeBeforeCreate = albumPhotoRelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlbumPhotoRelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(albumPhotoRelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlbumPhotoRel in the database
        List<AlbumPhotoRel> albumPhotoRelList = albumPhotoRelRepository.findAll();
        assertThat(albumPhotoRelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlbumPhotoRels() throws Exception {
        // Initialize the database
        albumPhotoRelRepository.saveAndFlush(albumPhotoRel);

        // Get all the albumPhotoRelList
        restAlbumPhotoRelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(albumPhotoRel.getId().intValue())));
    }

    @Test
    @Transactional
    void getAlbumPhotoRel() throws Exception {
        // Initialize the database
        albumPhotoRelRepository.saveAndFlush(albumPhotoRel);

        // Get the albumPhotoRel
        restAlbumPhotoRelMockMvc
            .perform(get(ENTITY_API_URL_ID, albumPhotoRel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(albumPhotoRel.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAlbumPhotoRel() throws Exception {
        // Get the albumPhotoRel
        restAlbumPhotoRelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlbumPhotoRel() throws Exception {
        // Initialize the database
        albumPhotoRelRepository.saveAndFlush(albumPhotoRel);

        int databaseSizeBeforeUpdate = albumPhotoRelRepository.findAll().size();

        // Update the albumPhotoRel
        AlbumPhotoRel updatedAlbumPhotoRel = albumPhotoRelRepository.findById(albumPhotoRel.getId()).get();
        // Disconnect from session so that the updates on updatedAlbumPhotoRel are not directly saved in db
        em.detach(updatedAlbumPhotoRel);
        AlbumPhotoRelDTO albumPhotoRelDTO = albumPhotoRelMapper.toDto(updatedAlbumPhotoRel);

        restAlbumPhotoRelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, albumPhotoRelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(albumPhotoRelDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlbumPhotoRel in the database
        List<AlbumPhotoRel> albumPhotoRelList = albumPhotoRelRepository.findAll();
        assertThat(albumPhotoRelList).hasSize(databaseSizeBeforeUpdate);
        AlbumPhotoRel testAlbumPhotoRel = albumPhotoRelList.get(albumPhotoRelList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingAlbumPhotoRel() throws Exception {
        int databaseSizeBeforeUpdate = albumPhotoRelRepository.findAll().size();
        albumPhotoRel.setId(count.incrementAndGet());

        // Create the AlbumPhotoRel
        AlbumPhotoRelDTO albumPhotoRelDTO = albumPhotoRelMapper.toDto(albumPhotoRel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumPhotoRelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, albumPhotoRelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(albumPhotoRelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlbumPhotoRel in the database
        List<AlbumPhotoRel> albumPhotoRelList = albumPhotoRelRepository.findAll();
        assertThat(albumPhotoRelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlbumPhotoRel() throws Exception {
        int databaseSizeBeforeUpdate = albumPhotoRelRepository.findAll().size();
        albumPhotoRel.setId(count.incrementAndGet());

        // Create the AlbumPhotoRel
        AlbumPhotoRelDTO albumPhotoRelDTO = albumPhotoRelMapper.toDto(albumPhotoRel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumPhotoRelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(albumPhotoRelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlbumPhotoRel in the database
        List<AlbumPhotoRel> albumPhotoRelList = albumPhotoRelRepository.findAll();
        assertThat(albumPhotoRelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlbumPhotoRel() throws Exception {
        int databaseSizeBeforeUpdate = albumPhotoRelRepository.findAll().size();
        albumPhotoRel.setId(count.incrementAndGet());

        // Create the AlbumPhotoRel
        AlbumPhotoRelDTO albumPhotoRelDTO = albumPhotoRelMapper.toDto(albumPhotoRel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumPhotoRelMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(albumPhotoRelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlbumPhotoRel in the database
        List<AlbumPhotoRel> albumPhotoRelList = albumPhotoRelRepository.findAll();
        assertThat(albumPhotoRelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlbumPhotoRelWithPatch() throws Exception {
        // Initialize the database
        albumPhotoRelRepository.saveAndFlush(albumPhotoRel);

        int databaseSizeBeforeUpdate = albumPhotoRelRepository.findAll().size();

        // Update the albumPhotoRel using partial update
        AlbumPhotoRel partialUpdatedAlbumPhotoRel = new AlbumPhotoRel();
        partialUpdatedAlbumPhotoRel.setId(albumPhotoRel.getId());

        restAlbumPhotoRelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlbumPhotoRel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlbumPhotoRel))
            )
            .andExpect(status().isOk());

        // Validate the AlbumPhotoRel in the database
        List<AlbumPhotoRel> albumPhotoRelList = albumPhotoRelRepository.findAll();
        assertThat(albumPhotoRelList).hasSize(databaseSizeBeforeUpdate);
        AlbumPhotoRel testAlbumPhotoRel = albumPhotoRelList.get(albumPhotoRelList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateAlbumPhotoRelWithPatch() throws Exception {
        // Initialize the database
        albumPhotoRelRepository.saveAndFlush(albumPhotoRel);

        int databaseSizeBeforeUpdate = albumPhotoRelRepository.findAll().size();

        // Update the albumPhotoRel using partial update
        AlbumPhotoRel partialUpdatedAlbumPhotoRel = new AlbumPhotoRel();
        partialUpdatedAlbumPhotoRel.setId(albumPhotoRel.getId());

        restAlbumPhotoRelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlbumPhotoRel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlbumPhotoRel))
            )
            .andExpect(status().isOk());

        // Validate the AlbumPhotoRel in the database
        List<AlbumPhotoRel> albumPhotoRelList = albumPhotoRelRepository.findAll();
        assertThat(albumPhotoRelList).hasSize(databaseSizeBeforeUpdate);
        AlbumPhotoRel testAlbumPhotoRel = albumPhotoRelList.get(albumPhotoRelList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingAlbumPhotoRel() throws Exception {
        int databaseSizeBeforeUpdate = albumPhotoRelRepository.findAll().size();
        albumPhotoRel.setId(count.incrementAndGet());

        // Create the AlbumPhotoRel
        AlbumPhotoRelDTO albumPhotoRelDTO = albumPhotoRelMapper.toDto(albumPhotoRel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumPhotoRelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, albumPhotoRelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(albumPhotoRelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlbumPhotoRel in the database
        List<AlbumPhotoRel> albumPhotoRelList = albumPhotoRelRepository.findAll();
        assertThat(albumPhotoRelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlbumPhotoRel() throws Exception {
        int databaseSizeBeforeUpdate = albumPhotoRelRepository.findAll().size();
        albumPhotoRel.setId(count.incrementAndGet());

        // Create the AlbumPhotoRel
        AlbumPhotoRelDTO albumPhotoRelDTO = albumPhotoRelMapper.toDto(albumPhotoRel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumPhotoRelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(albumPhotoRelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlbumPhotoRel in the database
        List<AlbumPhotoRel> albumPhotoRelList = albumPhotoRelRepository.findAll();
        assertThat(albumPhotoRelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlbumPhotoRel() throws Exception {
        int databaseSizeBeforeUpdate = albumPhotoRelRepository.findAll().size();
        albumPhotoRel.setId(count.incrementAndGet());

        // Create the AlbumPhotoRel
        AlbumPhotoRelDTO albumPhotoRelDTO = albumPhotoRelMapper.toDto(albumPhotoRel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumPhotoRelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(albumPhotoRelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlbumPhotoRel in the database
        List<AlbumPhotoRel> albumPhotoRelList = albumPhotoRelRepository.findAll();
        assertThat(albumPhotoRelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlbumPhotoRel() throws Exception {
        // Initialize the database
        albumPhotoRelRepository.saveAndFlush(albumPhotoRel);

        int databaseSizeBeforeDelete = albumPhotoRelRepository.findAll().size();

        // Delete the albumPhotoRel
        restAlbumPhotoRelMockMvc
            .perform(delete(ENTITY_API_URL_ID, albumPhotoRel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AlbumPhotoRel> albumPhotoRelList = albumPhotoRelRepository.findAll();
        assertThat(albumPhotoRelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
