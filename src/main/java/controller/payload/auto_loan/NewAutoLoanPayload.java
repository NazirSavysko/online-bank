package controller.payload.auto_loan;


import entity.annotation.CurrentAmount;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public record NewAutoLoanPayload(
        @NotBlank(message = "amount must not be blank")
        BigDecimal amount,
        @NotBlank(message = "amount must not be blank")
        @CurrentAmount
        BigDecimal currentAmount,
        @NotBlank(message = "term in months must not be blank")
        Integer termInMonths,
        @NotBlank(message = "holder passport number must not be blank")
        @Pattern(regexp = "^[A-Z]{2}[0-9]{6}$", message = "holder passport number must be in the format XX123456")
        String holderPassportNumber
) {
}
