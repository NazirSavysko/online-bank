package dao.impl;

import dao.DebitCardDAO;
import entity.DebitCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
        final String sqlForGettingAllDebitCards = "SELECT * FROM debit_card";

        return jdbcTemplate.query(sqlForGettingAllDebitCards, this.debitCardRowMapper());
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
