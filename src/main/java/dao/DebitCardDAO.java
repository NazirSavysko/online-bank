package dao;

import entity.DebitCard;

import java.math.BigDecimal;
import java.util.List;

public interface DebitCardDAO {
    List<DebitCard> getAllDebitCards();

    boolean findByCardNumber(String cardNumber);

    DebitCard saveDebitCard(DebitCard debitCard);

    DebitCard getByCardNumber(String cardNumber);

    boolean updateDebitCard(BigDecimal balance, String cardNumber);
}

