package svobodavlad.web.rest;

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
import svobodavlad.domain.Album;
import svobodavlad.repository.AlbumRepository;
import svobodavlad.service.dto.AlbumDTO;
import svobodavlad.service.mapper.AlbumMapper;

/**
 * Integration tests for the {@link AlbumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlbumResourceIT {

    private static final String DEFAULT_ALBUM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALBUM_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/albums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumMapper albumMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlbumMockMvc;

    private Album album;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Album createEntity(EntityManager em) {
        Album album = new Album();
        album.setAlbumName(DEFAULT_ALBUM_NAME);
        return album;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Album createUpdatedEntity(EntityManager em) {
        Album album = new Album();
        album.setAlbumName(UPDATED_ALBUM_NAME);
        return album;
    }

    @BeforeEach
    public void initTest() {
        album = createEntity(em);
    }

    @Test
    @Transactional
    void createAlbum() throws Exception {
        int databaseSizeBeforeCreate = albumRepository.findAll().size();
        // Create the Album
        AlbumDTO albumDTO = albumMapper.toDto(album);
        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isCreated());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeCreate + 1);
        Album testAlbum = albumList.get(albumList.size() - 1);
        assertThat(testAlbum.getAlbumName()).isEqualTo(DEFAULT_ALBUM_NAME);
    }

    @Test
    @Transactional
    void createAlbumWithExistingId() throws Exception {
        // Create the Album with an existing ID
        album.setId(1L);
        AlbumDTO albumDTO = albumMapper.toDto(album);

        int databaseSizeBeforeCreate = albumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAlbumNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setAlbumName(null);

        // Create the Album, which fails.
        AlbumDTO albumDTO = albumMapper.toDto(album);

        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isBadRequest());

        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlbums() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList
        restAlbumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(album.getId().intValue())))
            .andExpect(jsonPath("$.[*].albumName").value(hasItem(DEFAULT_ALBUM_NAME)));
    }

    @Test
    @Transactional
    void getAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get the album
        restAlbumMockMvc
            .perform(get(ENTITY_API_URL_ID, album.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(album.getId().intValue()))
            .andExpect(jsonPath("$.albumName").value(DEFAULT_ALBUM_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAlbum() throws Exception {
        // Get the album
        restAlbumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        int databaseSizeBeforeUpdate = albumRepository.findAll().size();

        // Update the album
        Album updatedAlbum = albumRepository.findById(album.getId()).get();
        // Disconnect from session so that the updates on updatedAlbum are not directly saved in db
        em.detach(updatedAlbum);
        updatedAlbum.setAlbumName(UPDATED_ALBUM_NAME);
        AlbumDTO albumDTO = albumMapper.toDto(updatedAlbum);

        restAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, albumDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(albumDTO))
            )
            .andExpect(status().isOk());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
        Album testAlbum = albumList.get(albumList.size() - 1);
        assertThat(testAlbum.getAlbumName()).isEqualTo(UPDATED_ALBUM_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();
        album.setId(count.incrementAndGet());

        // Create the Album
        AlbumDTO albumDTO = albumMapper.toDto(album);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, albumDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(albumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();
        album.setId(count.incrementAndGet());

        // Create the Album
        AlbumDTO albumDTO = albumMapper.toDto(album);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(albumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();
        album.setId(count.incrementAndGet());

        // Create the Album
        AlbumDTO albumDTO = albumMapper.toDto(album);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlbumWithPatch() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        int databaseSizeBeforeUpdate = albumRepository.findAll().size();

        // Update the album using partial update
        Album partialUpdatedAlbum = new Album();
        partialUpdatedAlbum.setId(album.getId());

        restAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlbum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlbum))
            )
            .andExpect(status().isOk());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
        Album testAlbum = albumList.get(albumList.size() - 1);
        assertThat(testAlbum.getAlbumName()).isEqualTo(DEFAULT_ALBUM_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAlbumWithPatch() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        int databaseSizeBeforeUpdate = albumRepository.findAll().size();

        // Update the album using partial update
        Album partialUpdatedAlbum = new Album();
        partialUpdatedAlbum.setId(album.getId());

        partialUpdatedAlbum.setAlbumName(UPDATED_ALBUM_NAME);

        restAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlbum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlbum))
            )
            .andExpect(status().isOk());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
        Album testAlbum = albumList.get(albumList.size() - 1);
        assertThat(testAlbum.getAlbumName()).isEqualTo(UPDATED_ALBUM_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();
        album.setId(count.incrementAndGet());

        // Create the Album
        AlbumDTO albumDTO = albumMapper.toDto(album);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, albumDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(albumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();
        album.setId(count.incrementAndGet());

        // Create the Album
        AlbumDTO albumDTO = albumMapper.toDto(album);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(albumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();
        album.setId(count.incrementAndGet());

        // Create the Album
        AlbumDTO albumDTO = albumMapper.toDto(album);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        int databaseSizeBeforeDelete = albumRepository.findAll().size();

        // Delete the album
        restAlbumMockMvc
            .perform(delete(ENTITY_API_URL_ID, album.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
