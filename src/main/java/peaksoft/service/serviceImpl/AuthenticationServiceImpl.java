package peaksoft.service.serviceImpl;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.config.JWTService;
import peaksoft.dto.dtoUser.reguest.AdminTokenRequest;
import peaksoft.dto.dtoUser.response.AuthenticationResponse;
import peaksoft.dto.dtoUser.reguest.SignInRequest;
import peaksoft.entity.Restaurant;
import peaksoft.entity.User;
import peaksoft.enums.Role;
import peaksoft.exceptions.BadRequestException;
import peaksoft.exceptions.NotFoundException;
import peaksoft.repository.RestaurantRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.AuthenticationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RestaurantRepository restaurantRepository;



    @Override
    public AuthenticationResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.getUserByEmail(signInRequest.email()).orElseThrow(() -> new NotFoundException("user is not found"));
        if (signInRequest.email().isBlank()){
            throw new BadCredentialsException("email does not exist...");
        }
        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())){
            throw new BadRequestException("Incorrect password");

        }

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .role(user.getRole())
                .email(user.getEmail())
                .build();
    }

    @PostConstruct
    public  void saveAdminAndRestaurant(){
        List<User> allUsers = restaurantRepository.getAllUsers();
        Restaurant restaurant = new Restaurant();
        User user = new User();
        List<User> users = new ArrayList<>();
        users.add(user);
        restaurant.setName("Java&Bar");
        restaurant.setService(15);
        restaurant.setLocation("KG");
        restaurant.setRestType("European");
        restaurant.setUsers(users);
        restaurant.setNumberOfEmployees(allUsers.size());
        if(!restaurantRepository.existsByName(restaurant.getName())){
            restaurantRepository.save(restaurant);
            log.info("Success");
        }

        user.setFirstName("Admin");
        user.setLastName("Adminov");
        user.setPassword(passwordEncoder.encode("1234B"));
        user.setEmail("admin@gmail.com");
        user.setRole(Role.ADMIN);
        user.setPhoneNumber("+996500700800");
        user.setDateOfBirth(LocalDate.of(2000,2,23));
        user.setExperience(3);

        user.setRestaurant(restaurant);
        if (!userRepository.existsByEmail(user.getEmail())){
            userRepository.save(user);
        }


    }
}
