package svobodavlad.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import svobodavlad.web.rest.TestUtil;

class AlbumPhotoRelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlbumPhotoRel.class);
        AlbumPhotoRel albumPhotoRel1 = new AlbumPhotoRel();
        albumPhotoRel1.setId(1L);
        AlbumPhotoRel albumPhotoRel2 = new AlbumPhotoRel();
        albumPhotoRel2.setId(albumPhotoRel1.getId());
        assertThat(albumPhotoRel1).isEqualTo(albumPhotoRel2);
        albumPhotoRel2.setId(2L);
        assertThat(albumPhotoRel1).isNotEqualTo(albumPhotoRel2);
        albumPhotoRel1.setId(null);
        assertThat(albumPhotoRel1).isNotEqualTo(albumPhotoRel2);
    }
}
