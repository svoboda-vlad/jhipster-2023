package svobodavlad.web.rest.extended;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import svobodavlad.web.rest.UserJWTController;
import svobodavlad.web.rest.vm.LoginVM;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserJWTExtendedController {

    private final UserJWTController userJWTController;

    @PostMapping("/authenticate")
    public ResponseEntity<UserJWTController.JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        return userJWTController.authorize(loginVM);
    }
}
