package entity;

import entity.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public final class User {
    private long id;
    private  String userName;
    private  Gender gender;
    private  LocalDate dateOfBirth;
    private  String passportNumber;
}
