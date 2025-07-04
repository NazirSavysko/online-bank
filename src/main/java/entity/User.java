package entity;

import entity.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Setter
@Builder
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

    @OneToMany(mappedBy = "creditHolder",
            cascade = {REMOVE})
    private List<AutoLoan> accounts;

    @OneToMany(mappedBy = "cardHolder",
            cascade = {REMOVE})
    private List<DebitCard> creditCards;

    @OneToMany(mappedBy = "mortgageHolder",
            cascade = {REMOVE})
    private List<Mortgage> debitCards;
}
