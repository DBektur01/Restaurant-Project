package peaksoft.dto.dtoUser.reguest;


import lombok.Builder;
import jakarta.validation.constraints.Email;
import peaksoft.enums.Role;
import peaksoft.validation.PhoneNumberValid;
import peaksoft.validation.passwordAnotation.PasswordValidator;

import java.time.LocalDate;


@Builder
public record SignUpRequest(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        @Email
        String email,
        @PasswordValidator
        String password,
        @PhoneNumberValid
        String phoneNumber,
        Role role,
        int experience)
{
    public SignUpRequest {
    }
}
