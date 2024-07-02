package peaksoft.dto.dtoUser.reguest;

import lombok.Builder;

@Builder
public record SignInRequest(
        String email,
        String password
) {
    public SignInRequest {
    }
}
