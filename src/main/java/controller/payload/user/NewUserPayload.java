package controller.payload.user;

import entity.enums.Gender;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public record NewUserPayload(
        @NotBlank(message = "errors.user.username_is_blank")
        @Size(min = 3, max = 20, message = "errors.user.username_size_is_invalid")
        String username,

        @NotBlank(message = "errors.user.gender_is_blank")
        Gender gender,

        @NotBlank(message = "Date of birth cannot be blank")
        LocalDate dateOfBirth,

        @NotBlank(message = "errors.user.password_is_blank")
        @Pattern(regexp = "^[A-Z]{2}[0-9]{6}$",
                message = "errors.user.password_format_is_invalid")
        String passportNumber
) {
}
