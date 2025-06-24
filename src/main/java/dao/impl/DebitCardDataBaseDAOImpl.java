package dao.impl;

import dao.DebitCardDAO;
import dao.UserDAO;
import entity.DebitCard;
import entity.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static dao.sql.DebitCardSql.*;
import static java.sql.Date.valueOf;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;


@Repository
public final class DebitCardDataBaseDAOImpl implements DebitCardDAO {

    private final JdbcTemplate jdbcTemplate;
    private final UserDAO userDAO;

    @Autowired
    public DebitCardDataBaseDAOImpl(final JdbcTemplate jdbcTemplate, final UserDAO userDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDAO = userDAO;
    }

    @Override
    public @NotNull List<DebitCard> getAllDebitCards() {
        return jdbcTemplate.query(SELECT_ALL, this::debitCardRowMapper);
    }

    @Override
    public boolean isCardNumberAvailable(final String cardNumber) {
        final Integer count = jdbcTemplate.queryForObject(CHECK_CARD_NUMBER, Integer.class, cardNumber);

        return count != null && count == 0;
    }

    @Contract("_ -> param1")
    @Override
    public @NotNull DebitCard saveDebitCard(final @NotNull DebitCard debitCard) {
        final User user = userDAO.getUserByPassportNumber(debitCard.getCardHolderPassportNumber());
        final GeneratedKeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(INSERT, RETURN_GENERATED_KEYS);
            ps.setString(1, debitCard.getCardNumber());
            ps.setLong(2, user.getId());
            ps.setDate(3, valueOf(debitCard.getExpirationDate()));
            ps.setDate(4, valueOf(debitCard.getIssueDate()));
            ps.setString(5, debitCard.getCvv());
            ps.setBigDecimal(6, debitCard.getBalance());
            return ps;
        }, holder);

        debitCard.setId((int) requireNonNull(requireNonNull(holder.getKeys()).get("id")));
        return debitCard;
    }


    @Override
    public void updateDebitCard(final @NotNull DebitCard debitCard) {
        final User user = userDAO.getUserByPassportNumber(debitCard.getCardHolderPassportNumber());

        jdbcTemplate.update(UPDATE,
                debitCard.getCardNumber(),
                valueOf(debitCard.getExpirationDate()),
                valueOf(debitCard.getIssueDate()),
                debitCard.getCvv(),
                debitCard.getBalance(),
                user.getId(),
                debitCard.getId()
        );
    }

    @Override
    public void deleteDebitCard(final int id) {
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public @NotNull Optional<DebitCard> getDebitCardById(final int id) {
        return ofNullable(jdbcTemplate.queryForObject(SELECT_BY_ID, this::debitCardRowMapper, id));
    }

    private @NotNull DebitCard debitCardRowMapper(final @NotNull ResultSet resultSet, final int i) throws SQLException {
        final DebitCard debitCard = new DebitCard();

        debitCard.setId(resultSet.getInt("id"));
        debitCard.setCardNumber(resultSet.getString("card_number"));
        debitCard.setCardHolderPassportNumber(resultSet.getString("passport_number"));
        debitCard.setExpirationDate(resultSet.getDate("expiration_date").toLocalDate());
        debitCard.setCvv(resultSet.getString("cvv"));
        debitCard.setBalance(resultSet.getBigDecimal("balance"));
        debitCard.setIssueDate(resultSet.getDate("issue_date").toLocalDate());

        return debitCard;
    }
}
