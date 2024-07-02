package peaksoft.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.dtoUser.response.PaginationResponse;
import peaksoft.dto.SimpleResponse;
import peaksoft.dto.dtoUser.reguest.SignUpRequest;
import peaksoft.dto.dtoUser.response.UserResponse;
import peaksoft.service.UserService;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserAPI {

//@Autowired
    @Qualifier("Test")
    private final UserService service;

    @PreAuthorize("hasAnyAuthority('ADMIN','WAITER','CHEF')")
    @GetMapping("/getAllUsers")
    PaginationResponse getAllUsers(@RequestParam int pageSize,int currentPage){
        return service.getAllUsers(currentPage,pageSize);
    }

    @PostMapping("/signUp")
    public SimpleResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest){
        return service.signUp(signUpRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/assign")
    public  SimpleResponse assignUser(@RequestParam Long userId,@RequestParam Long restaurantId, @RequestParam String word){
        return service.assignUser(restaurantId,userId, word);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public SimpleResponse updateUser(@PathVariable Long id,@RequestBody SignUpRequest signUpRequest){
        return service.updateUserById(id, signUpRequest);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','WAITER','CHEF')")
    @GetMapping("/getUserById/{id}")
    public UserResponse getUserById(@PathVariable Long id){
        return service.getUserById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CHEF')")
    @DeleteMapping("/delete/{id}")
    public SimpleResponse deleteUserById(@PathVariable Long id){
        return service.deleteUserById(id);
    }


}
