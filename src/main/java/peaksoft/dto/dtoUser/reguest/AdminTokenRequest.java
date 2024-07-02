package peaksoft.dto.dtoUser.reguest;

import lombok.Builder;

@Builder
public record AdminTokenRequest(String email) {
    public AdminTokenRequest {
    }
}
