package controller.payload.mortgage;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record PaymentMortgagePayload(
        @NotBlank(message = "id must not be blank")
        int id,
        @NotBlank(message = "amount must not be blank")
        BigDecimal amount
) {
}
