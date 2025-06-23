package controller.payload.debit_card;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record NewDebitCardPayload(
        @NotBlank(message = "card holder passport number must not be blank")
        @Pattern(regexp = "^[A-Z]{2}[0-9]{6}$", message = "card holder passport number must be in the format XX123456")
        String cardHolderPassportNumber
) {
}
