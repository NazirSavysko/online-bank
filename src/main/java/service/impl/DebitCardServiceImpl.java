package service.impl;

import dao.DebitCardDAO;
import entity.DebitCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.DebitCardService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.lang.String.valueOf;
import static java.math.BigDecimal.ZERO;
import static java.time.LocalDate.now;

@Service
public class DebitCardServiceImpl implements DebitCardService {

    private final DebitCardDAO debitCardDAO;

    @Autowired
    public DebitCardServiceImpl(final DebitCardDAO dao) {
        this.debitCardDAO = dao;
    }

    @Override
    public List<DebitCard> getAllDebitCards() {
        return debitCardDAO.getAllDebitCards();
    }

    @Override
    public DebitCard saveDebitCard(final String cardHolderPassportNumber) {
        final String cardNumber = this.createCardNumber();
        final String ccv = this.createCvv();
        final LocalDate issueDate = now();
        final LocalDate expirationDate = issueDate.plusYears(5);
        final DebitCard debitCard = new DebitCard(cardNumber, cardHolderPassportNumber,
                expirationDate, issueDate, ccv, ZERO);
        return debitCardDAO.saveDebitCard(debitCard);
    }

    @Override
    public Optional<DebitCard> getDebitCardByCardNumber(final String cardNumber) {
        return Optional.of(debitCardDAO.getByCardNumber(cardNumber));
    }

    @Override
    public boolean updateDebitCard(final BigDecimal balance, final String cardNumber) {
        return debitCardDAO.updateDebitCard(balance, cardNumber);
    }

    private String createCardNumber() {
        String cardNumber;
        do {
            final Random random = new Random();
            final StringBuilder card = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                card.append(random.nextInt(10));
            }
            cardNumber = card.toString();
        } while (!debitCardDAO.findByCardNumber(cardNumber));
        return cardNumber;
    }

    private String createCvv() {
        return valueOf(new Random().nextInt(900) + 100);
    }
}
