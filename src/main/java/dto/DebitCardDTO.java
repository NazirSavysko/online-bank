package dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DebitCardDTO(
         int id,
         String cardNumber,
         String cardHolderPassportNumber,
         LocalDate expirationDate,
         LocalDate issueDate,
         String cvv,
         BigDecimal balance
) {
}
