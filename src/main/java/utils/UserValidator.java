package utils;

import controller.payload.UserPayload;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;



import java.util.Objects;


@Component
public final class UserValidator implements Validator {

    private static final String PASSPORT_PATTERN = "^[A-Z]{2}[0-9]{6}$";

    @Override
    public boolean supports(Class<?> clazz) {
        return UserPayload.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserPayload user = (UserPayload) target;

        if (user.passportNumber() == null || user.passportNumber().isBlank()) {
            errors.rejectValue("passportNumber", "passportNumber.empty", "Passport number cannot be blank");
        } else if (!user.passportNumber().matches(PASSPORT_PATTERN)) {
            errors.rejectValue("passportNumber", "passportNumber.invalidFormat", "Passport number must be in the format XX123456");
        }

        if (user.userName() == null || user.userName().isBlank()) {
            errors.rejectValue("userName", "userName.empty", "Username cannot be blank");
        } else if (user.userName().length() < 3 || user.userName().length() > 20) {
            errors.rejectValue("userName", "userName.size", "Username must be between 3 and 20 characters");
        }
        if (user.dateOfBirth() == null) {
            errors.rejectValue("dateOfBirth", "dateOfBirth.empty", "Date of birth cannot be empty");
        } else {
            if (user.dateOfBirth().isAfter(java.time.LocalDate.now())) {
                errors.rejectValue("dateOfBirth", "dateOfBirth.future", "Date of birth cannot be in the future");
            }
        }
        if (user.gender() == null) {
            errors.rejectValue("gender", "gender.empty", "Gender cannot be empty");
        }
    }
}
