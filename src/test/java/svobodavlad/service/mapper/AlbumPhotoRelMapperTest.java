package svobodavlad.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlbumPhotoRelMapperTest {

    private AlbumPhotoRelMapper albumPhotoRelMapper;

    @BeforeEach
    public void setUp() {
        albumPhotoRelMapper = new AlbumPhotoRelMapperImpl();
    }
}
