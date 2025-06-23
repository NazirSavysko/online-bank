package dao;

import entity.DebitCard;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DebitCardDAO {
    List<DebitCard> getAllDebitCards();

    boolean isCardNumberAvailable(String cardNumber);

    Optional<DebitCard> saveDebitCard(DebitCard debitCard);

    Optional<DebitCard> getByCardNumber(String cardNumber);

    boolean updateDebitCard(BigDecimal balance, String cardNumber);

    boolean deleteDebitCard(String cardNumber);
}

