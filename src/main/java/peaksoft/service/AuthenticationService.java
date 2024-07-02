package peaksoft.service;

import peaksoft.dto.dtoUser.reguest.AdminTokenRequest;
import peaksoft.dto.dtoUser.response.AuthenticationResponse;
import peaksoft.dto.dtoUser.reguest.SignInRequest;

public interface AuthenticationService {


    AuthenticationResponse signIn(SignInRequest signInRequest);

}
