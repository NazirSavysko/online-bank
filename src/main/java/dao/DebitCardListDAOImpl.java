package dao;

import entity.DebitCard;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.copyOf;

@Repository
public final class DebitCardListDAOImpl implements DebitCardDAO {
    private final List<DebitCard> debitCards;

    public DebitCardListDAOImpl() {
        debitCards = new ArrayList<>();
        debitCards.add(new DebitCard("123", 1000, "12345", "4111111111111111"));
        debitCards.add(new DebitCard("456", 2000, "12345", "4222222222222222"));

        debitCards.add(new DebitCard("789", 5000, "54321", "4333333333333333"));
        debitCards.add(new DebitCard("321", 7500, "54321", "4444444444444444"));
    }

    @Override
    public List<DebitCard> getAllDebitCards() {
        return copyOf(debitCards);
    }

    @Override
    public List<DebitCard> getDebitCardsByPassportId(String passportId) {
        return debitCards.stream()
                .filter(card -> card.getCardHolderPassportId().equals(passportId))
                .collect(Collectors.toList());
    }
}
