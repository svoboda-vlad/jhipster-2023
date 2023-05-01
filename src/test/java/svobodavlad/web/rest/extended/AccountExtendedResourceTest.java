package svobodavlad.web.rest.extended;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import svobodavlad.service.dto.AdminUserDTO;
import svobodavlad.service.extended.UserService;
import svobodavlad.web.rest.AccountResource;
import svobodavlad.web.rest.TestUtil;
import svobodavlad.web.rest.vm.ManagedUserVM;

/**
 * Unit tests for the {@link AccountResource} REST controller.
 */
@WebMvcTest(AccountExtendedResource.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountExtendedResourceTest {

    private static final String ACCOUNT_URL = "/api/v1/account";

    @MockBean
    private UserService userService;

    @MockBean
    private AccountResource accountResource;

    @Autowired
    private MockMvc restAccountMockMvc;

    private JacksonTester<ManagedUserVM> managedUserVMJacksonTester;

    @BeforeEach
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void testSaveAccount() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testSaveAccountRequest.json");
        AdminUserDTO userDTO = this.managedUserVMJacksonTester.read(sourceFile).getObject();

        restAccountMockMvc
            .perform(post(ACCOUNT_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDTO)))
            .andExpect(status().isOk())
            .andExpect(content().string(""));

        verify(userService, times(1))
            .updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getLangKey(), userDTO.getImageUrl());
    }

    @Test
    void testSaveInvalidEmail() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testSaveInvalidEmailRequest.json");
        AdminUserDTO userDTO = this.managedUserVMJacksonTester.read(sourceFile).getObject();

        restAccountMockMvc
            .perform(post(ACCOUNT_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDTO)))
            .andExpect(status().isBadRequest());

        verify(userService, never())
            .updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getLangKey(), userDTO.getImageUrl());
    }
}
