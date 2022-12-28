package svobodavlad.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import svobodavlad.web.rest.TestUtil;

class AlbumPhotoRelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlbumPhotoRelDTO.class);
        AlbumPhotoRelDTO albumPhotoRelDTO1 = new AlbumPhotoRelDTO();
        albumPhotoRelDTO1.setId(1L);
        AlbumPhotoRelDTO albumPhotoRelDTO2 = new AlbumPhotoRelDTO();
        assertThat(albumPhotoRelDTO1).isNotEqualTo(albumPhotoRelDTO2);
        albumPhotoRelDTO2.setId(albumPhotoRelDTO1.getId());
        assertThat(albumPhotoRelDTO1).isEqualTo(albumPhotoRelDTO2);
        albumPhotoRelDTO2.setId(2L);
        assertThat(albumPhotoRelDTO1).isNotEqualTo(albumPhotoRelDTO2);
        albumPhotoRelDTO1.setId(null);
        assertThat(albumPhotoRelDTO1).isNotEqualTo(albumPhotoRelDTO2);
    }
}
