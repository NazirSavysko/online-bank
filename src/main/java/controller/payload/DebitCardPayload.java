package controller.payload;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record DebitCardPayload(
        @NotBlank(message = "card holder passport number must not be blank")
        @Pattern(regexp = "^[A-Z]{2}[0-9]{6}$", message = "card holder passport number must be in the format XX123456")
        String cardHolderPassportNumber
) {
}
