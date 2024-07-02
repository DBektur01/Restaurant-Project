package peaksoft.service;

import peaksoft.dto.SimpleResponse;
import peaksoft.dto.dtoUser.response.PaginationResponse;
import peaksoft.dto.dtoUser.reguest.SignUpRequest;
import peaksoft.dto.dtoUser.response.UserResponse;

public interface UserService {

    PaginationResponse getAllUsers(int pageSize,int currentPage);
    SimpleResponse signUp(SignUpRequest signUpRequest);
    SimpleResponse assignUser(Long restaurantId, Long userId, String word);
    SimpleResponse updateUserById(Long id, SignUpRequest signUpRequest);
    SimpleResponse deleteUserById(Long id);
    UserResponse getUserById(Long id);


}
