package service.impl;

import dao.DebitCardDAO;
import dao.UserDAO;
import entity.DebitCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.DebitCardService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DebitCardServiceImpl implements DebitCardService {

    private final DebitCardDAO debitCardDAO;
    private final UserDAO userDAO;

    @Autowired
    public DebitCardServiceImpl(final DebitCardDAO dao, final UserDAO userDAO) {
        this.debitCardDAO = dao;
        this.userDAO = userDAO;
    }

    @Override
    public List<DebitCard> getAllDebitCards() {
        return debitCardDAO.getAllDebitCards();
    }

    @Override
    public DebitCard saveDebitCard(final String holderPassportNumber,
                                   final String cardNumber,
                                   final String cvv,
                                   final BigDecimal balance,
                                   final LocalDate expirationDate,
                                   final LocalDate issueDate) {
        if (!this.debitCardDAO.isCardNumberAvailable(cardNumber)) {
            throw new IllegalArgumentException("Card number " + cardNumber + " is already in use.");
        }
        if (this.userDAO.isPassportNumberAvailable(holderPassportNumber)) {
            throw new IllegalArgumentException("User with passport number " + holderPassportNumber + " does not exist.");
        }

        final DebitCard debitCard = new DebitCard(0, cardNumber, holderPassportNumber, expirationDate, issueDate, cvv, balance);
        return debitCardDAO.saveDebitCard(debitCard);

    }

    @Override
    public Optional<DebitCard> getDebitCardById(final int id) {
        return debitCardDAO.getDebitCardById(id);
    }

    @Override
    public void updateDebitCard(final int id, final String holderPassportNumber, final String cardNumber,final String pastCardNumber, final String cvv, final BigDecimal balance, final LocalDate expirationDate, final LocalDate issueDate) {
        if (!(cardNumber.equals(pastCardNumber) || this.debitCardDAO.isCardNumberAvailable(cardNumber))) {
            throw new IllegalArgumentException("Card number " + cardNumber + " is already in use.");
        }
        if (this.userDAO.isPassportNumberAvailable(holderPassportNumber)) {
            throw new IllegalArgumentException("User with passport number " + holderPassportNumber + " does not exist.");
        } else {
            final DebitCard debitCard = new DebitCard(id, cardNumber, holderPassportNumber, expirationDate, issueDate, cvv, balance);
            debitCardDAO.updateDebitCard(debitCard);
        }
    }

    @Override
    public void deleteDebitCard(final int id) {
        debitCardDAO.deleteDebitCard(id);
    }
}
