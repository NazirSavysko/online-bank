package dao;

import dto.DebitCardDTO;
import entity.DebitCard;

import java.util.List;
import java.util.Optional;

public interface DebitCardDAO {
    List<DebitCardDTO> getAllDebitCards();

    boolean isCardNumberAvailable(String cardNumber);

    DebitCardDTO saveDebitCard(DebitCard debitCard);

    void updateDebitCard(DebitCard debitCard);

    void deleteDebitCard(int id);

    Optional<DebitCardDTO> getDebitCardById(int id);

    DebitCard getDebitCardByCardNumber(String CardNumber);
}

