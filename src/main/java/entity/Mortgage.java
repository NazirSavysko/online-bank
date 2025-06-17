package entity;

import entity.enums.Mortgage_Term;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Mortgage {
    private int id;
    private BigDecimal MortgageAmount;
    private String mortgageHolderPassportNumber;
    private BigDecimal currentMortgageAmount;
    private Mortgage_Term mortgageTerm;
}
