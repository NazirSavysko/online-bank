package controller.payload.debit_card;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public record DepositPayload(
        @NotBlank(message = "card passport number must not be blank")
        @Pattern(regexp = "^[0-9]{16}$", message = "card number from which the money is debited must be 16 digits")
        String cardNumber,

        @NotBlank(message = "amount must not be blank")
        BigDecimal amount
) {
}
