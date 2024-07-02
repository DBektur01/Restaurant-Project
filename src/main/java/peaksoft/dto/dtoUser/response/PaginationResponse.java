package peaksoft.dto.dtoUser.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginationResponse(
        List<UserResponse> userResponseList,
        int size,
        int page) {
    public PaginationResponse {
    }
}
