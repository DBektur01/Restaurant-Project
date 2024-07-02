package peaksoft.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.dtoUser.reguest.AdminTokenRequest;
import peaksoft.dto.dtoUser.response.AuthenticationResponse;
import peaksoft.dto.dtoUser.reguest.SignInRequest;
import peaksoft.service.AuthenticationService;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "User api")
public class AuthAPI {


    private final AuthenticationService authenticationService;


   @PostMapping("/signIn")
   public AuthenticationResponse signIn(@RequestBody SignInRequest signInRequest){
        return authenticationService.signIn(signInRequest);
   }
}