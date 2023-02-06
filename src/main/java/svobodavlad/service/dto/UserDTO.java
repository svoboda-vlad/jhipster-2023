package svobodavlad.service.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import svobodavlad.domain.User;

/**
 * A DTO representing a user, with only the public attributes.
 */
@Data
@NoArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String login;

    public UserDTO(User user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.login = user.getLogin();
    }
}
