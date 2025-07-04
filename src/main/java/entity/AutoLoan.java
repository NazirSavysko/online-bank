package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auto_loan")
public final class AutoLoan {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column(name = "loan_amount")
    private BigDecimal creditAmount;

    @Column(name = "current_loan_amount")
    private BigDecimal currentCreditAmount;

    @Column(name = "loan_term")
    private int creditTermInMonths;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User creditHolder;

}
