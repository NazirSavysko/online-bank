package dao.impl;

import dao.AutoLoanDAO;
import entity.AutoLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.Objects.requireNonNull;

@Repository
public final class AutoLoanDataBaseDAOImpl implements AutoLoanDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AutoLoanDataBaseDAOImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<AutoLoan> getAllAutoLoans() {
        try {
            final String sqlForGettingAllAutoLoans = "SELECT * FROM auto_loan";
            return jdbcTemplate.query(sqlForGettingAllAutoLoans, this::autoLoanRowMapper);
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public AutoLoan saveAutoLoan(final AutoLoan autoLoan) {
        try {
            final String sqlForSavingAutoLoan = "INSERT INTO auto_loan (loan_holder_passport_number, loan_amount, current_loan_amount, loan_term) VALUES (?, ?, ?, ?)";
            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sqlForSavingAutoLoan, RETURN_GENERATED_KEYS);
                ps.setString(1, autoLoan.getCreditHolderPassportNumber());
                ps.setBigDecimal(2, autoLoan.getCreditAmount());
                ps.setBigDecimal(3, autoLoan.getCurrentCreditAmount());
                ps.setInt(4, autoLoan.getCreditTermInMonths());
                return ps;
            }, holder);
            autoLoan.setId((int) requireNonNull(requireNonNull(holder.getKeys()).get("id")));
            ;
            return autoLoan;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Optional<AutoLoan> getAutoLoanById(final int autoLoanId) {
        try {
            final String sqlForGettingAutoLoanById = "SELECT * FROM auto_loan WHERE id = ?";
            final List<AutoLoan> autoLoans = jdbcTemplate.query(sqlForGettingAutoLoanById, this::autoLoanRowMapper, autoLoanId);

            return Optional.ofNullable(autoLoans.isEmpty() ? null : autoLoans.get(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public AutoLoan updateAutoLoan(final Integer currentAmount, final Integer termInMonth, final int autoLoanId) {
        return jdbcTemplate.queryForObject(
                "UPDATE auto_loan SET  current_loan_amount = ?, loan_term = ? WHERE id = ? RETURNING *",
                this::autoLoanRowMapper, currentAmount, termInMonth, autoLoanId
        );
    }

    @Override
    public boolean deleteAutoLoan(final int autoLoanId) {
        final String sqlForDeletingAutoLoan = "DELETE FROM auto_loan WHERE id = ?";
        try {
            return jdbcTemplate.update(sqlForDeletingAutoLoan, autoLoanId) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private AutoLoan autoLoanRowMapper(final ResultSet resultSet, final int i) {
        final AutoLoan autoLoan = new AutoLoan();
        try {
            autoLoan.setId(resultSet.getInt("id"));
            autoLoan.setCreditHolderPassportNumber(resultSet.getString("loan_holder_passport_number"));
            autoLoan.setCreditAmount(resultSet.getBigDecimal("loan_amount"));
            autoLoan.setCurrentCreditAmount(resultSet.getBigDecimal("current_loan_amount"));
            autoLoan.setCreditTermInMonths(resultSet.getInt("loan_term"));
        } catch (Exception e) {
            return null;
        }
        return autoLoan;
    }
}

