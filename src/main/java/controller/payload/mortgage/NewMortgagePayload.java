package controller.payload.mortgage;

import entity.annotation.CurrentAmount;
import entity.enums.Mortgage_Term;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public record NewMortgagePayload(
        @NotBlank(message = "mortgage amount must not be blank")
        BigDecimal amount,
        @NotBlank(message = " current mortgage amount must not be blank")
        @CurrentAmount
        BigDecimal currentAmount,
        @NotBlank(message = "mortgage holder passport number must not be blank")
        @Pattern(regexp = "^[A-Z]{2}[0-9]{6}$", message = "card holder passport number must be in the format XX123456")
        String mortgageHolderPassportNumber,
        @NotBlank(message = "mortgage term must not be blank")
        Mortgage_Term mortgageTerm

) {
}
