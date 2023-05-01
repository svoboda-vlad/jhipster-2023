package svobodavlad.web.rest.extended;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import svobodavlad.service.dto.AdminUserDTO;
import svobodavlad.service.dto.PasswordChangeDTO;
import svobodavlad.service.extended.UserService;
import svobodavlad.web.rest.AccountResource;
import svobodavlad.web.rest.vm.KeyAndPasswordVM;
import svobodavlad.web.rest.vm.ManagedUserVM;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AccountExtendedResource {

    private final UserService userService;

    private final AccountResource accountResource;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        accountResource.registerAccount(managedUserVM);
    }

    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        accountResource.activateAccount(key);
    }

    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        return accountResource.isAuthenticated(request);
    }

    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return accountResource.getAccount();
    }

    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        accountResource.changePassword(passwordChangeDto);
    }

    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        accountResource.requestPasswordReset(mail);
    }

    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        accountResource.finishPasswordReset(keyAndPassword);
    }
}
