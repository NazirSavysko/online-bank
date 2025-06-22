package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class DebitCard {
    private String cardNumber;
    private String cardHolderPassportNumber;
    private LocalDate expirationDate;
    private LocalDate issueDate;
    private String cvv;
    private BigDecimal balance;
}
