package dao.impl;

import dao.AutoLoanDAO;
import dao.UserDAO;
import entity.AutoLoan;
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

import static dao.sql.AutoLoanSql.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.Objects.requireNonNull;

@Repository
public final class AutoLoanDataBaseDAOImpl implements AutoLoanDAO {

    private final JdbcTemplate jdbcTemplate;
    private final UserDAO userDAO;

    @Autowired
    public AutoLoanDataBaseDAOImpl(final JdbcTemplate jdbcTemplate, final UserDAO userDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDAO = userDAO;
    }

    @Override
    public @NotNull List<AutoLoan> getAllAutoLoans() {
        return jdbcTemplate.query(SELECT_ALL, this::autoLoanRowMapper);
    }

    @Contract("_ -> param1")
    @Override
    public @NotNull AutoLoan saveAutoLoan(final @NotNull AutoLoan autoLoan) {
        final User user = this.userDAO.getUserByPassportNumber(autoLoan.getCreditHolderPassportNumber());
        final GeneratedKeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(INSERT, RETURN_GENERATED_KEYS);
            ps.setLong(1, user.getId());
            ps.setBigDecimal(2, autoLoan.getCreditAmount());
            ps.setBigDecimal(3, autoLoan.getCurrentCreditAmount());
            ps.setInt(4, autoLoan.getCreditTermInMonths());
            return ps;
        }, holder);
        autoLoan.setId((int) requireNonNull(requireNonNull(holder.getKeys()).get("id")));

        return autoLoan;

    }

    @Override
    public @NotNull Optional<AutoLoan> getAutoLoanById(final int autoLoanId) {
        final List<AutoLoan> autoLoans = jdbcTemplate.query(SELECT_BY_ID, this::autoLoanRowMapper, autoLoanId);

        return autoLoans.stream().findFirst();
    }

    @Override
    public boolean updateAutoLoan(final @NotNull AutoLoan autoLoan) {
        final User user = this.userDAO.getUserByPassportNumber(autoLoan.getCreditHolderPassportNumber());


        int rowsAffected = jdbcTemplate.update(UPDATE,
                user.getId(),
                autoLoan.getCreditAmount(),
                autoLoan.getCurrentCreditAmount(),
                autoLoan.getCreditTermInMonths(),
                autoLoan.getId());
        return rowsAffected > 0;
    }

    @Override
    public void deleteAutoLoan(final int autoLoanId) {
        jdbcTemplate.update(DELETE, autoLoanId);
    }

    private @NotNull AutoLoan autoLoanRowMapper(final @NotNull ResultSet resultSet, final int i) throws SQLException {
        final AutoLoan autoLoan = new AutoLoan();

        autoLoan.setId(resultSet.getInt("id"));
        autoLoan.setCreditHolderPassportNumber(resultSet.getString("passport_number"));
        autoLoan.setCreditAmount(resultSet.getBigDecimal("loan_amount"));
        autoLoan.setCurrentCreditAmount(resultSet.getBigDecimal("current_loan_amount"));
        autoLoan.setCreditTermInMonths(resultSet.getInt("loan_term"));

        return autoLoan;
    }
}

