package dao;

import entity.DebitCard;

import java.util.List;
import java.util.Optional;

public interface DebitCardDAO {
    List<DebitCard> getAllDebitCards();

    boolean isCardNumberAvailable(String cardNumber);

    DebitCard saveDebitCard(DebitCard debitCard);

    void updateDebitCard(DebitCard debitCard);

    void deleteDebitCard(int id);

    Optional<DebitCard> getDebitCardById(int id);
}

