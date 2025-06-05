package entity;

import entity.enums.Gender;

import java.time.LocalDate;
import java.util.List;

public final class User {
    private final String username;
    private final Gender gender;
    private final LocalDate dateOfBirth;
    private final String passportNumber;
    private final List<DebitCard> debitCards;
    private final List<Mortgage> mortgages;
    private final List<AutoLoan> autoLoans;

    public User(
            final String username,
            final Gender gender,
            final LocalDate dateOfBirth,
            final String passportNumber,
            final List<DebitCard> debitCards,
            final List<Mortgage> mortgages,
            final List<AutoLoan> autoLoans
    ) {
        this.username = username;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.debitCards = debitCards;
        this.mortgages = mortgages;
        this.autoLoans = autoLoans;
        this.passportNumber = passportNumber;
    }

    public List<AutoLoan> getAutoLoans() {
        return autoLoans;
    }

    public List<Mortgage> getMortgages() {
        return mortgages;
    }

    public List<DebitCard> getDebitCards() {
        return debitCards;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public String getUsername() {
        return username;
    }
    public String getPassportNumber() {
        return passportNumber;
    }
}
