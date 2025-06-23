package dao.impl;

import dao.DebitCardDAO;
import entity.DebitCard;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.lang.System.err;
import static java.util.List.of;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;


@Repository
public final class DebitCardDataBaseDAOImpl implements DebitCardDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DebitCardDataBaseDAOImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DebitCard> getAllDebitCards() {
        try {
            final String sqlForGettingAllDebitCards = "SELECT * FROM debit_card";
            return jdbcTemplate.query(sqlForGettingAllDebitCards, this::debitCardRowMapper);
        } catch (final Exception e) {
            err.println("Error retrieving all debit cards: " + e.getMessage());
            return of();
        }
    }

    @Override
    public boolean isCardNumberAvailable(final String cardNumber) {
        try {
            final String sql = "SELECT COUNT(*) FROM debit_card WHERE card_number = ?";
            final Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cardNumber);
            return count != null && count == 0;
        } catch (final Exception e) {
            err.println("Error checking card number: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<DebitCard> saveDebitCard(final DebitCard debitCard) {
        try {
            final String sqlForSavingDebitCard = "INSERT INTO debit_card (card_number, card_holder_passport_number, expiration_date, issue_date, cvv, balance) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlForSavingDebitCard, debitCard.getCardNumber(), debitCard.getCardHolderPassportNumber(),
                    debitCard.getExpirationDate(), debitCard.getIssueDate(), debitCard.getCvv(), debitCard.getBalance());

            return Optional.of(debitCard);
        } catch (final Exception e) {
            err.println("Error saving debit card: " + e.getMessage());
            return empty();
        }
    }

    @Override
    public Optional<DebitCard> getByCardNumber(final String cardNumber) {
        try {
            final String sqlForGettingDebitCardByNumber = "SELECT * FROM debit_card WHERE card_number = ?";

            return ofNullable(jdbcTemplate.queryForObject(sqlForGettingDebitCardByNumber, this::debitCardRowMapper, cardNumber));
        } catch (final Exception e) {
            return empty();
        }
    }

    @Override
    public boolean updateDebitCard(final BigDecimal balance, final String cardNumber) {
        try {
            final String sqlForUpdatingDebitCard = "UPDATE debit_card SET balance = ? WHERE card_number = ?";
            final int rowsAffected = jdbcTemplate.update(sqlForUpdatingDebitCard, balance, cardNumber);
            return rowsAffected > 0;
        } catch (final Exception e) {
            err.println("Error updating debit card: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteDebitCard(final String cardNumber) {
        try {
            final String sqlForDeletingDebitCard = "DELETE FROM debit_card WHERE card_number = ?";
            final int rowsAffected = jdbcTemplate.update(sqlForDeletingDebitCard, cardNumber);
            return rowsAffected > 0;
        } catch (final Exception e) {
            err.println("Error deleting debit card: " + e.getMessage());
            return false;
        }
    }

    private @NotNull DebitCard debitCardRowMapper(final @NotNull ResultSet resultSet, final int i) throws SQLException {
        final DebitCard debitCard = new DebitCard();
        debitCard.setCardNumber(resultSet.getString("card_number"));
        debitCard.setCardHolderPassportNumber(resultSet.getString("card_holder_passport_number"));
        debitCard.setExpirationDate(resultSet.getDate("expiration_date").toLocalDate());
        debitCard.setCvv(resultSet.getString("cvv"));
        debitCard.setBalance(resultSet.getBigDecimal("balance"));
        debitCard.setIssueDate(resultSet.getDate("issue_date").toLocalDate());

        return debitCard;
    }
}
