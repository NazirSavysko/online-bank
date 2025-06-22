package controller.payload;


import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record UpdateDebitCardPayload(
        @NotBlank(message = "balance passport number must not be blank")
        BigDecimal balance
) {
}
