package dto;

import entity.enums.Mortgage_Term;

import java.math.BigDecimal;

public record MortgageDTO(
         int id,
         BigDecimal mortgageAmount,
         String mortgageHolderPassportNumber,
         BigDecimal currentMortgageAmount,
         Mortgage_Term mortgageTerm
) {
}
