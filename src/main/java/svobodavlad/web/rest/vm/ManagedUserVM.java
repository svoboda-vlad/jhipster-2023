package svobodavlad.web.rest.vm;

import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import svobodavlad.service.dto.AdminUserDTO;

/**
 * View Model extending the AdminUserDTO, which is meant to be used in the user management UI.
 */
@Data
@NoArgsConstructor
public class ManagedUserVM extends AdminUserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;
}
