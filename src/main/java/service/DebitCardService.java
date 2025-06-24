package service;

import entity.DebitCard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DebitCardService {
    List<DebitCard> getAllDebitCards();

    DebitCard saveDebitCard( String holderPassportNumber, String cardNumber, String cvv, BigDecimal balance, LocalDate expirationDate, LocalDate issueDate);

    Optional<DebitCard> getDebitCardById(int id);

    void updateDebitCard(int id, String holderPassportNumber, String cardNumber,String pastCardNumber, String cvv, BigDecimal balance, LocalDate expirationDate, LocalDate issueDate);

    void deleteDebitCard(int id);
}
