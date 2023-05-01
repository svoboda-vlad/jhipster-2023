package svobodavlad.web.rest.extended;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import svobodavlad.service.dto.UserDTO;
import svobodavlad.web.rest.PublicUserResource;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PublicUserExtendedResource {

    private final PublicUserResource publicUserResource;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllPublicUsers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        return publicUserResource.getAllPublicUsers(pageable);
    }

    @GetMapping("/authorities")
    public List<String> getAuthorities() {
        return publicUserResource.getAuthorities();
    }
}
