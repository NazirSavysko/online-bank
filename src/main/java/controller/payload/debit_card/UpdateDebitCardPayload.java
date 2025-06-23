package controller.payload.debit_card;


import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record UpdateDebitCardPayload(
        @NotBlank(message = "balance must not be blank")
        BigDecimal balance
) {
}
