package service;

import entity.DebitCard;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DebitCardService {
    List<DebitCard> getAllDebitCards();

    Optional<DebitCard> saveDebitCard(String cardHolderPassportNumber);

    Optional<DebitCard> getDebitCardByCardNumber(String cardNumber);

    boolean updateDebitCard(BigDecimal balance, String cardNumber);

    boolean deleteDebitCard(String cardNumber);
}
