package peaksoft.dto.dtoUser.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import peaksoft.enums.Role;
@Builder
public record AuthenticationResponse(
        String token,
        String email,
        Role role
//HttpStatus httpStatus, String message
) {
    public AuthenticationResponse {
    }
}
