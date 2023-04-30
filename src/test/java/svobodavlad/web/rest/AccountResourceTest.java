package svobodavlad.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import svobodavlad.domain.Authority;
import svobodavlad.domain.User;
import svobodavlad.security.AuthoritiesConstants;
import svobodavlad.service.*;
import svobodavlad.service.dto.AdminUserDTO;
import svobodavlad.service.dto.PasswordChangeDTO;
import svobodavlad.web.rest.vm.ManagedUserVM;

/**
 * Unit tests for the {@link AccountResource} REST controller.
 */
@WebMvcTest(AccountResource.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountResourceTest {

    static final String USER_LOGIN = "test";
    static final String USER_PASSWORD = "123456789012345678901234567890123456789012345678901234567890";
    static final String USER_ACTIVATION_KEY = "12345678901234567890";
    static final String USER_RESET_KEY = "1234567890";
    static final Instant USER_RESET_DATE = Instant.parse("2023-02-09T11:05:19.659222Z");

    @MockBean
    private UserService userService;

    @MockBean
    private MailService mailService;

    @Autowired
    private MockMvc restAccountMockMvc;

    private JacksonTester<AdminUserDTO> adminUserDTOJacksonTester;

    private JacksonTester<ManagedUserVM> managedUserVMJacksonTester;

    private JacksonTester<PasswordChangeDTO> passwordChangeDTOJacksonTester;

    @BeforeEach
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void testNonAuthenticatedUser() throws Exception {
        restAccountMockMvc
            .perform(get("/api/authenticate").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    @Test
    void testAuthenticatedUser() throws Exception {
        restAccountMockMvc
            .perform(
                get("/api/authenticate")
                    .with(request -> {
                        request.setRemoteUser(USER_LOGIN);
                        return request;
                    })
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().string(USER_LOGIN));
    }

    @Test
    void testGetExistingAccount() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/getExistingAccountResponse.json");
        AdminUserDTO expectedResponse = this.adminUserDTOJacksonTester.read(sourceFile).getObject();

        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        User user = new User();
        user.setPassword(USER_PASSWORD);
        user.setActivationKey(USER_ACTIVATION_KEY);
        user.setResetKey(USER_RESET_KEY);
        user.setResetDate(USER_RESET_DATE);
        user.setId(expectedResponse.getId());
        user.setLogin(expectedResponse.getLogin());
        user.setFirstName(expectedResponse.getFirstName());
        user.setLastName(expectedResponse.getLastName());
        user.setEmail(expectedResponse.getEmail());
        user.setImageUrl(expectedResponse.getImageUrl());
        user.setLangKey(expectedResponse.getLangKey());
        user.setActivated(expectedResponse.isActivated());
        user.setCreatedBy(expectedResponse.getCreatedBy());
        user.setCreatedDate(expectedResponse.getCreatedDate());
        user.setLastModifiedBy(expectedResponse.getLastModifiedBy());
        user.setLastModifiedDate(expectedResponse.getLastModifiedDate());
        user.setAuthorities(new HashSet<>(Set.of(authority)));

        when(userService.getUserWithAuthorities()).thenReturn(Optional.of(user));

        String responseString = restAccountMockMvc
            .perform(get("/api/account").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        AdminUserDTO actualResponse = this.adminUserDTOJacksonTester.parseObject(responseString);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void testGetUnknownAccount() throws Exception {
        when(userService.getUserWithAuthorities()).thenReturn(Optional.empty());

        restAccountMockMvc
            .perform(get("/api/account").accept(MediaType.APPLICATION_PROBLEM_JSON))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void testRegisterValid() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testRegisterValidRequest.json");
        ManagedUserVM validUser = this.managedUserVMJacksonTester.read(sourceFile).getObject();

        User user = new User();
        when(userService.registerUser(validUser, validUser.getPassword())).thenReturn(user);

        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(validUser)))
            .andExpect(status().isCreated())
            .andExpect(content().string(""));

        verify(mailService, times(1)).sendActivationEmail(user);
    }

    @Test
    void testRegisterInvalidLogin() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testRegisterInvalidLoginRequest.json");
        ManagedUserVM invalidUser = this.managedUserVMJacksonTester.read(sourceFile).getObject();

        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invalidUser)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterInvalidEmail() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testRegisterInvalidEmailRequest.json");
        ManagedUserVM invalidUser = this.managedUserVMJacksonTester.read(sourceFile).getObject();

        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invalidUser)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterInvalidPassword() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testRegisterInvalidPasswordRequest.json");
        ManagedUserVM invalidUser = this.managedUserVMJacksonTester.read(sourceFile).getObject();

        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invalidUser)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterNullPassword() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testRegisterNullPasswordRequest.json");
        ManagedUserVM invalidUser = this.managedUserVMJacksonTester.read(sourceFile).getObject();

        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invalidUser)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterDuplicateLogin() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testRegisterDuplicateLoginRequest.json");
        ManagedUserVM secondUser = this.managedUserVMJacksonTester.read(sourceFile).getObject();

        when(userService.registerUser(secondUser, secondUser.getPassword())).thenThrow(new UsernameAlreadyUsedException());

        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(secondUser)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void testRegisterDuplicateEmail() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testRegisterDuplicateEmailRequest.json");
        ManagedUserVM secondUser = this.managedUserVMJacksonTester.read(sourceFile).getObject();

        when(userService.registerUser(secondUser, secondUser.getPassword())).thenThrow(new EmailAlreadyUsedException());

        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(secondUser)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void testActivateAccount() throws Exception {
        when(userService.activateRegistration(USER_ACTIVATION_KEY)).thenReturn(Optional.of(new User()));

        restAccountMockMvc
            .perform(get("/api/activate?key={activationKey}", USER_ACTIVATION_KEY))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    @Test
    void testActivateAccountWithWrongKey() throws Exception {
        final String wrongActivationKey = "wrong activation key";

        when(userService.activateRegistration(wrongActivationKey)).thenReturn(Optional.empty());

        restAccountMockMvc
            .perform(get("/api/activate?key={activationKey}", wrongActivationKey))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void testSaveAccount() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testSaveAccountRequest.json");
        AdminUserDTO userDTO = this.managedUserVMJacksonTester.read(sourceFile).getObject();

        restAccountMockMvc
            .perform(post("/api/account").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDTO)))
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
            .perform(post("/api/account").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDTO)))
            .andExpect(status().isBadRequest());

        verify(userService, never())
            .updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getLangKey(), userDTO.getImageUrl());
    }

    @Test
    void testChangePasswordWrongExistingPassword() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testChangePasswordWrongExistingPasswordRequest.json");
        PasswordChangeDTO passwordChangeDTO = this.passwordChangeDTOJacksonTester.read(sourceFile).getObject();

        doThrow(new InvalidPasswordException())
            .when(userService)
            .changePassword(passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());

        restAccountMockMvc
            .perform(
                post("/api/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passwordChangeDTO))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void testChangePassword() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testChangePasswordRequest.json");
        PasswordChangeDTO passwordChangeDTO = this.passwordChangeDTOJacksonTester.read(sourceFile).getObject();

        restAccountMockMvc
            .perform(
                post("/api/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passwordChangeDTO))
            )
            .andExpect(status().isOk())
            .andExpect(content().string(""));

        verify(userService, times(1)).changePassword(passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());
    }

    @Test
    void testChangePasswordTooSmall() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testChangePasswordTooSmallRequest.json");
        PasswordChangeDTO passwordChangeDTO = this.passwordChangeDTOJacksonTester.read(sourceFile).getObject();

        restAccountMockMvc
            .perform(
                post("/api/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passwordChangeDTO))
            )
            .andExpect(status().isBadRequest());

        verify(userService, never()).changePassword(passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());
    }

    @Test
    void testChangePasswordTooLong() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testChangePasswordTooLongRequest.json");
        PasswordChangeDTO passwordChangeDTO = this.passwordChangeDTOJacksonTester.read(sourceFile).getObject();

        restAccountMockMvc
            .perform(
                post("/api/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passwordChangeDTO))
            )
            .andExpect(status().isBadRequest());

        verify(userService, never()).changePassword(passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());
    }

    @Test
    void testChangePasswordEmpty() throws Exception {
        File sourceFile = new File("src/test/resources/templates/accountResource/testChangePasswordEmptyRequest.json");
        PasswordChangeDTO passwordChangeDTO = this.passwordChangeDTOJacksonTester.read(sourceFile).getObject();

        restAccountMockMvc
            .perform(
                post("/api/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passwordChangeDTO))
            )
            .andExpect(status().isBadRequest());

        verify(userService, never()).changePassword(passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());
    }
    /*@Test
    void testRequestPasswordReset() throws Exception {
        User user = new User();
        user.setPassword(RandomStringUtils.randomAlphanumeric(60));
        user.setActivated(true);
        user.setLogin("password-reset");
        user.setEmail("password-reset@example.com");
        user.setLangKey("en");
        userRepository.saveAndFlush(user);

        restAccountMockMvc
            .perform(post("/api/account/reset-password/init").content("password-reset@example.com"))
            .andExpect(status().isOk());
    }

    @Test
    void testRequestPasswordResetUpperCaseEmail() throws Exception {
        User user = new User();
        user.setPassword(RandomStringUtils.randomAlphanumeric(60));
        user.setActivated(true);
        user.setLogin("password-reset-upper-case");
        user.setEmail("password-reset-upper-case@example.com");
        user.setLangKey("en");
        userRepository.saveAndFlush(user);

        restAccountMockMvc
            .perform(post("/api/account/reset-password/init").content("password-reset-upper-case@EXAMPLE.COM"))
            .andExpect(status().isOk());
    }

    @Test
    void testRequestPasswordResetWrongEmail() throws Exception {
        restAccountMockMvc
            .perform(post("/api/account/reset-password/init").content("password-reset-wrong-email@example.com"))
            .andExpect(status().isOk());
    }

    @Test
    void testFinishPasswordReset() throws Exception {
        User user = new User();
        user.setPassword(RandomStringUtils.randomAlphanumeric(60));
        user.setLogin("finish-password-reset");
        user.setEmail("finish-password-reset@example.com");
        user.setResetDate(Instant.now().plusSeconds(60));
        user.setResetKey("reset key");
        userRepository.saveAndFlush(user);

        KeyAndPasswordVM keyAndPassword = new KeyAndPasswordVM();
        keyAndPassword.setKey(user.getResetKey());
        keyAndPassword.setNewPassword("new password");

        restAccountMockMvc
            .perform(
                post("/api/account/reset-password/finish")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(keyAndPassword))
            )
            .andExpect(status().isOk());

        User updatedUser = userRepository.findOneByLogin(user.getLogin()).orElse(null);
        assertThat(passwordEncoder.matches(keyAndPassword.getNewPassword(), updatedUser.getPassword())).isTrue();
    }

    @Test
    void testFinishPasswordResetTooSmall() throws Exception {
        User user = new User();
        user.setPassword(RandomStringUtils.randomAlphanumeric(60));
        user.setLogin("finish-password-reset-too-small");
        user.setEmail("finish-password-reset-too-small@example.com");
        user.setResetDate(Instant.now().plusSeconds(60));
        user.setResetKey("reset key too small");
        userRepository.saveAndFlush(user);

        KeyAndPasswordVM keyAndPassword = new KeyAndPasswordVM();
        keyAndPassword.setKey(user.getResetKey());
        keyAndPassword.setNewPassword("foo");

        restAccountMockMvc
            .perform(
                post("/api/account/reset-password/finish")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(keyAndPassword))
            )
            .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin(user.getLogin()).orElse(null);
        assertThat(passwordEncoder.matches(keyAndPassword.getNewPassword(), updatedUser.getPassword())).isFalse();
    }

    @Test
    void testFinishPasswordResetWrongKey() throws Exception {
        KeyAndPasswordVM keyAndPassword = new KeyAndPasswordVM();
        keyAndPassword.setKey("wrong reset key");
        keyAndPassword.setNewPassword("new password");

        restAccountMockMvc
            .perform(
                post("/api/account/reset-password/finish")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(keyAndPassword))
            )
            .andExpect(status().isInternalServerError());
    }*/
}
