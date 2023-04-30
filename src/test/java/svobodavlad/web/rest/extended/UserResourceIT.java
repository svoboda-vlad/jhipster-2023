package svobodavlad.web.rest.extended;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import svobodavlad.IntegrationTest;
import svobodavlad.domain.User;
import svobodavlad.security.AuthoritiesConstants;
import svobodavlad.service.dto.AdminUserDTO;
import svobodavlad.web.rest.UserResource;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class UserResourceIT extends svobodavlad.web.rest.UserResourceIT {

    @Test
    @Override
    protected void testUserDTOtoUser() {
        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setId(DEFAULT_ID);
        userDTO.setLogin(DEFAULT_LOGIN);
        userDTO.setFirstName(DEFAULT_FIRSTNAME);
        userDTO.setLastName(DEFAULT_LASTNAME);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setActivated(true);
        userDTO.setImageUrl(DEFAULT_IMAGEURL);
        userDTO.setLangKey(DEFAULT_LANGKEY);
        userDTO.setCreatedBy(DEFAULT_LOGIN);
        userDTO.setLastModifiedBy(DEFAULT_LOGIN);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        User user = userMapper.userDTOToUser(userDTO);
        assertThat(user.getId()).isEqualTo(DEFAULT_ID);
        assertThat(user.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(user.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(user.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(user.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(user.isActivated()).isTrue();
        assertThat(user.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(user.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(user.getCreatedBy()).isNull();
        assertThat(user.getCreatedDate()).isNull();
        assertThat(user.getLastModifiedBy()).isNull();
        assertThat(user.getLastModifiedDate()).isNull();
        assertThat(user.getAuthorities()).extracting("name").containsExactly(AuthoritiesConstants.USER);
    }
}
