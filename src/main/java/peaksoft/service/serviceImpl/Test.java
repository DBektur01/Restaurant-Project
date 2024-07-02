package peaksoft.service.serviceImpl;

import org.springframework.stereotype.Service;
import peaksoft.dto.SimpleResponse;
import peaksoft.dto.dtoUser.reguest.SignUpRequest;
import peaksoft.dto.dtoUser.response.PaginationResponse;
import peaksoft.dto.dtoUser.response.UserResponse;
import peaksoft.service.UserService;

/**
 * Author: Bektur Duyshenbek uulu
 */
@Service("Test")
public class Test implements UserService {
    @Override
    public PaginationResponse getAllUsers(int pageSize, int currentPage) {
        return null;
    }

    @Override
    public SimpleResponse signUp(SignUpRequest signUpRequest) {
        return null;
    }

    @Override
    public SimpleResponse assignUser(Long restaurantId, Long userId, String word) {
        return null;
    }

    @Override
    public SimpleResponse updateUserById(Long id, SignUpRequest signUpRequest) {
        return null;
    }

    @Override
    public SimpleResponse deleteUserById(Long id) {
        return null;
    }

    @Override
    public UserResponse getUserById(Long id) {
        return null;
    }
}
