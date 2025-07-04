package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "debit_card")
public final class DebitCard {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column(name = "card_number")
    private String cardNumber;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User cardHolder;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "cvv")
    private String cvv;

    @Column(name = "balance")
    private BigDecimal balance;
}
