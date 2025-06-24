package controller.payload.user;

import entity.annotation.MinAgeUser;
import entity.enums.Gender;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;


public record NewUserPayload(
        @NotBlank(message = "user name must not be blank")
        @Size(min = 3, max = 20, message = "user name must be between 3 and 20 characters")
        String userName,
        @NotBlank(message = "gender must not be null")
        Gender gender,
        @NotBlank(message = "date of birth must not be null")
        @MinAgeUser
        LocalDate dateOfBirth,
        @NotBlank(message = "passport number must not be blank")
        @Pattern(regexp = "^[A-Z]{2}[0-9]{6}$", message = "card holder passport number must be in the format XX123456")
        String passportNumber
){}
