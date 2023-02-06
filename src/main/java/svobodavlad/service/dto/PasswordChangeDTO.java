package svobodavlad.service.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representing a password change required data - current and new password.
 */
@Data
@NoArgsConstructor
public class PasswordChangeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String currentPassword;
    private String newPassword;

    public PasswordChangeDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
