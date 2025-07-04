package entity;

import entity.enums.Mortgage_Term;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mortgage")
public class Mortgage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column(name = "mortgage_amount")
    private BigDecimal MortgageAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User mortgageHolder;

    @Column(name = "current_mortgage_amount")
    private BigDecimal currentMortgageAmount;

    @Enumerated(value = STRING)
    @Column(name = "mortgage_term")
    private Mortgage_Term mortgageTerm;
}
