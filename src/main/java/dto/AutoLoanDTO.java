package dto;

import java.math.BigDecimal;

public record AutoLoanDTO(
         int id,
         BigDecimal creditAmount,
         BigDecimal currentCreditAmount,
         int creditTermInMonths,
         String creditHolderPassportNumber
) {
}
