package peaksoft.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.dto.SimpleResponse;
import peaksoft.dto.dtoUser.response.PaginationResponse;
import peaksoft.dto.dtoUser.reguest.SignUpRequest;
import peaksoft.dto.dtoUser.response.UserResponse;
import peaksoft.entity.Restaurant;
import peaksoft.entity.User;
import peaksoft.enums.Role;
import peaksoft.exceptions.BadCredentialException;
import peaksoft.exceptions.BadRequestException;
import peaksoft.exceptions.NoSuchElementException;
import peaksoft.exceptions.NotFoundException;
import peaksoft.repository.RestaurantRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.UserService;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public PaginationResponse getAllUsers(int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<UserResponse> allUsers = userRepository.getAllUsers(pageable);

        return PaginationResponse.builder()
                .userResponseList(allUsers.getContent())
                .page(allUsers.getNumber() + 1)
                .size(allUsers.getTotalPages())
                .build();
    }

    @Override
    public SimpleResponse signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setFirstName(signUpRequest.firstName());
        user.setLastName(signUpRequest.lastName());
        int age = Period.between(signUpRequest.dateOfBirth(), LocalDate.now()).getYears();
        if (signUpRequest.role().equals(Role.CHEF)) {
            if (age >= 25 && age <= 45) {
                user.setDateOfBirth(signUpRequest.dateOfBirth());
            } else throw new BadCredentialException("You can not apply for this job because of your age...");

        } else if (signUpRequest.role().equals(Role.WAITER)) {
            if (age < 30 && age > 18) {
                user.setDateOfBirth(signUpRequest.dateOfBirth());

            } else throw new BadRequestException("You can not apply for this job because your age...");
        }

            user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setPhoneNumber(signUpRequest.phoneNumber());
        user.setRole(signUpRequest.role());
        if (signUpRequest.role().equals(Role.CHEF)) {
            if (signUpRequest.experience() >= 2) {
                user.setExperience(signUpRequest.experience());
            } else throw new BadRequestException("Your experience does not enough for chef vocation");
        } else if (signUpRequest.role().equals(Role.WAITER)) {
            if (signUpRequest.experience() >= 1) {
                user.setExperience(signUpRequest.experience());
            } else throw new BadRequestException("Your experience does not enough for waiter vocation");
        }
        Restaurant restaurant = restaurantRepository.findById(1L).orElseThrow(
                () -> new NoSuchElementException("Restaurant does not exist..."));

        if (restaurant.getUsers().size() < 15) {
            userRepository.save(user);
        } else throw new BadCredentialException("There is no more vocation!");

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format("You successfully applied for the job..." + restaurant.getUsers().size()))
                .build();
    }

    @Override
    public SimpleResponse assignUser(Long restaurantId, Long userId, String word) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("Restaurant with id: " + restaurantId + " is not found!"));

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " is not found!"));
        if (word.equalsIgnoreCase("accepted")) {
            restaurant.getUsers().add(user);
            restaurantRepository.save(restaurant);
            user.setRestaurant(restaurant);
            userRepository.save(user);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Successfully accepted!")
                    .build();
        }
        userRepository.delete(user);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully rejected")
                .build();

    }

    @Override
    public SimpleResponse updateUserById(Long id, SignUpRequest signUpRequest) {
        getAdmin();
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User with id: " + id + " is not found!"));
        user.setFirstName(signUpRequest.firstName());
        user.setLastName(signUpRequest.lastName());
        user.setDateOfBirth(signUpRequest.dateOfBirth());
        user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setPhoneNumber(signUpRequest.phoneNumber());
        user.setRole(signUpRequest.role());
        user.setExperience(signUpRequest.experience());
        userRepository.save(user);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("successfully updated...")
                .build();
    }

    @Override
    public SimpleResponse deleteUserById(Long id) {
        User user = getAdmin();
        User user1 = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User with id: " + id + " is not found!"));
        if (user.getRole().equals(Role.ADMIN)) {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Successfully deleted")
                        .build();
            } else throw new NotFoundException("User with id:" + id + " is does not exist...");
        } else {
            if (user.equals(user1)) {
                if (userRepository.existsById(id)) {
                    userRepository.deleteById(id);
                    return SimpleResponse.builder()
                            .httpStatus(HttpStatus.OK)
                            .message("Successfully deleted")
                            .build();
                } else throw new NotFoundException("User with id:" + id + " is does not exist...");
            } else throw new BadCredentialException("You can not get user with id:" + user1.getId());
        }
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user1 = getAdmin();
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User with id: " + id + " is not found!"));
        if (user1.getRole().equals(Role.ADMIN)) {
            return UserResponse.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .dateOfBirth(user.getDateOfBirth())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .phoneNumber(user.getPhoneNumber())
                    .role(user.getRole())
                    .experience(user.getExperience())
                    .build();
        } else {
            if (user1.equals(user)) {
                return UserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .dateOfBirth(user.getDateOfBirth())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .phoneNumber(user.getPhoneNumber())
                        .role(user.getRole())
                        .experience(user.getExperience())
                        .build();
            } else throw new BadCredentialException("You can not get user with id:" + user.getId());
        }


    }

    private User getAdmin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findAll().stream()
                .filter(user -> email.equals(user.getEmail()) && Role.ADMIN.equals(user.getRole()))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("Forbidden 403"));
    }


}

