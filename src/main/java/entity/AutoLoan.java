package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class AutoLoan {
    private int id;
    private BigDecimal creditAmount;
    private BigDecimal currentCreditAmount;
    private int creditTermInMonths;
    private String creditHolderPassportNumber;
}
