package entity;

import entity.enums.Gender;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public final class User {
    private  String username;
    private  Gender gender;
    private  LocalDate dateOfBirth;
    private  String passportNumber;
}
