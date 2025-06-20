package controller.payload;

import entity.enums.Gender;

import java.time.LocalDate;


public record UserPayload(
        String userName,
        Gender gender,
        LocalDate dateOfBirth,
        String passportNumber
){}
