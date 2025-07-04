package dto;

import entity.enums.Gender;

import java.time.LocalDate;

public record UserDTO(
        long id,
        String userName,
        Gender gender,
        LocalDate dateOfBirth,
        String passportNumber
) {
}
