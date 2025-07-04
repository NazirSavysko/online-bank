package entity;

import entity.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bank_user")
public final class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(name = "user_name")
    private  String userName;

    @Enumerated(value = STRING)
    @Column(name = "gender")
    private  Gender gender;

    @Column(name = "birthdate")
    private  LocalDate dateOfBirth;

    @Column(name = "passport_number")
    private  String passportNumber;
}
