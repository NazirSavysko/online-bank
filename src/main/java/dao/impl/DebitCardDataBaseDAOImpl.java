package dao.impl;

import dao.DebitCardDAO;
import entity.DebitCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


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

            return jdbcTemplate.query(sqlForGettingAllDebitCards, this.debitCardRowMapper());
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public boolean findByCardNumber(final String cardNumber) {
        try {
            final String sql = "SELECT COUNT(*) FROM debit_card WHERE card_number = ?";
            final Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cardNumber);
            return count != null && count == 0;
        } catch (final Exception e) {
            return false;
        }
    }

    @Override
    public DebitCard saveDebitCard(final DebitCard debitCard) {
        try {
            final String sqlForSavingDebitCard = "INSERT INTO debit_card (card_number, card_holder_passport_number, expiration_date, issue_date, cvv, balance) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlForSavingDebitCard, debitCard.getCardNumber(), debitCard.getCardHolderPassportNumber(),
                    debitCard.getExpirationDate(), debitCard.getIssueDate(), debitCard.getCvv(), debitCard.getBalance());

            return debitCard;
        } catch (final Exception e) {
            return null;
        }
    }

    @Override
    public DebitCard getByCardNumber(final String cardNumber) {
        try {
            final String sqlForGettingDebitCardByNumber = "SELECT * FROM debit_card WHERE card_number = ?";
            return jdbcTemplate.queryForObject(sqlForGettingDebitCardByNumber, new Object[]{cardNumber}, this.debitCardRowMapper());
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean updateDebitCard(final BigDecimal balance, final String cardNumber) {
        try {
            final String sqlForUpdatingDebitCard = "UPDATE debit_card SET balance = ? WHERE card_number = ?";
            final int rowsAffected = jdbcTemplate.update(sqlForUpdatingDebitCard, balance, cardNumber);
            return rowsAffected > 0;
        } catch (final Exception e) {
            return false;
        }
    }


    private RowMapper<DebitCard> debitCardRowMapper() {
        return (rs, rowNum) -> {
            final DebitCard debitCard = new DebitCard();
            debitCard.setCardNumber(rs.getString("card_number"));
            debitCard.setCardHolderPassportNumber(rs.getString("card_holder_passport_number"));
            debitCard.setExpirationDate(rs.getDate("expiration_date").toLocalDate());
            debitCard.setCvv(rs.getString("cvv"));
            debitCard.setBalance(rs.getBigDecimal("balance"));
            debitCard.setIssueDate(rs.getDate("issue_date").toLocalDate());

            return debitCard;
        };
    }
}
