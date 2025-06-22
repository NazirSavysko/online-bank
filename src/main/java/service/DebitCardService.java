package service;

import entity.DebitCard;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DebitCardService {
    List<DebitCard> getAllDebitCards();

    DebitCard saveDebitCard(String cardHolderPassportNumber);

    Optional<DebitCard> getDebitCardByCardNumber(String cardNumber);

    boolean updateDebitCard(BigDecimal balance, String cardNumber);
}
