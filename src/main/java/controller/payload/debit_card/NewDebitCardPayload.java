package controller.payload.debit_card;


import entity.annotation.ExpirationDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

public record NewDebitCardPayload(
        @NotBlank(message = "card holder passport number must not be blank")
        @Pattern(regexp = "^[A-Z]{2}[0-9]{6}$", message = "card holder passport number must be in the format XX123456")
        String cardHolderPassportNumber,
        @NotBlank(message = "card number must not be blank")
        @Pattern(regexp = "^[0-9]{16}$", message = "card number must be 16 digits")
        String cardNumber,
        @NotBlank(message = "expiration date must not be blank")
        @ExpirationDate
        LocalDate expirationDate,
        @NotBlank(message = "issue date must not be blank")
        LocalDate issueDate,
        @NotBlank(message = "cvv must not be blank")
        @Pattern(regexp = "^[0-9]{3}$", message = "cvv must be 3 digits")
        String cvv,
        @NotBlank(message = "balance must not be blank")
        BigDecimal balance
) {
}
