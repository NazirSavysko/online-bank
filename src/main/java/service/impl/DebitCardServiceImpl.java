package service.impl;

import dao.DebitCardDAO;
import dao.UserDAO;
import dto.DebitCardDTO;
import entity.DebitCard;
import entity.User;
import org.jetbrains.annotations.NotNull;
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
    public List<DebitCardDTO> getAllDebitCards() {
        return debitCardDAO.getAllDebitCards();
    }

    @Override
    public DebitCardDTO saveDebitCard(final String holderPassportNumber,
                                      final String cardNumber,
                                      final String cvv,
                                      final BigDecimal balance,
                                      final LocalDate expirationDate,
                                      final LocalDate issueDate) {
        if (!this.debitCardDAO.isCardNumberAvailable(cardNumber)) {
            throw new IllegalArgumentException("Card number " + cardNumber + " is already in use.");
        }
        if (this.userDAO.isPassportNumberAvailable(holderPassportNumber)) {
            throw new IllegalArgumentException("UserDTO with passport number " + holderPassportNumber + " does not exist.");
        }
        final User user = this.userDAO.getUserByPassportNumber(holderPassportNumber);
        final DebitCard debitCard = new DebitCard(0, cardNumber, user, expirationDate, issueDate, cvv, balance);
        return debitCardDAO.saveDebitCard(debitCard);

    }

    @Override
    public Optional<DebitCardDTO> getDebitCardById(final int id) {
        return debitCardDAO.getDebitCardById(id);
    }

    @Override
    public void updateDebitCard(final int id, final String holderPassportNumber, final @NotNull String cardNumber, final String pastCardNumber, final String cvv, final BigDecimal balance, final LocalDate expirationDate, final LocalDate issueDate) {
        if (!(cardNumber.equals(pastCardNumber) || this.debitCardDAO.isCardNumberAvailable(cardNumber))) {
            throw new IllegalArgumentException("Card number " + cardNumber + " is already in use.");
        }
        if (this.userDAO.isPassportNumberAvailable(holderPassportNumber)) {
            throw new IllegalArgumentException("UserDTO with passport number " + holderPassportNumber + " does not exist.");
        } else {
            final User user = this.userDAO.getUserByPassportNumber(holderPassportNumber);
            final DebitCard debitCard = new DebitCard(id, cardNumber, user, expirationDate, issueDate, cvv, balance);
            debitCardDAO.updateDebitCard(debitCard);
        }
    }

    @Override
    public void deleteDebitCard(final int id) {
        debitCardDAO.deleteDebitCard(id);
    }

    @Override
    public void transferMoney(final @NotNull String fromCardNumber, final String toCardNumber, final BigDecimal amount) {
        if(fromCardNumber.equals(toCardNumber)){
            throw new IllegalArgumentException("Cannot transfer money to the same card: " + fromCardNumber);
        }
        if (debitCardDAO.isCardNumberAvailable(toCardNumber)) {
            throw new IllegalArgumentException("Card number " + toCardNumber + " does not exist.");
        }
        if (debitCardDAO.isCardNumberAvailable(fromCardNumber)) {
            throw new IllegalArgumentException("Card number " + fromCardNumber + " does not exist.");
        }

        final DebitCard fromCard = debitCardDAO.getDebitCardByCardNumber(fromCardNumber);
        final DebitCard toCard = debitCardDAO.getDebitCardByCardNumber(toCardNumber);

        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds on card " + fromCardNumber);
        }

        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));
        debitCardDAO.updateDebitCard(fromCard);
        debitCardDAO.updateDebitCard(toCard);
    }

    @Override
    public void depositMoney(final String cardNumber, final BigDecimal amount) {
        if(debitCardDAO.isCardNumberAvailable(cardNumber)) {
            throw new IllegalArgumentException("Card number " + cardNumber + " does not exist.");
        }

        final DebitCard debitCard = debitCardDAO.getDebitCardByCardNumber(cardNumber);
        debitCard.setBalance(debitCard.getBalance().add(amount));
        debitCardDAO.updateDebitCard(debitCard);
    }
}
