package dao.impl;

import dao.AutoLoanDAO;
import entity.AutoLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.sql.ResultSet;
import java.util.List;

@Repository
public final class AutoLoanDataBaseDAOImpl implements AutoLoanDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AutoLoanDataBaseDAOImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<AutoLoan> getAllAutoLoans() {
        final String sqlForGettingAllAutoLoans = "SELECT * FROM auto_loan";
        return jdbcTemplate.query(sqlForGettingAllAutoLoans,this::autoLoanRowMapper);
    }

    private AutoLoan autoLoanRowMapper(ResultSet resultSet, int i) {
        final AutoLoan autoLoan = new AutoLoan();
        try {
            autoLoan.setId(resultSet.getInt("id"));
            autoLoan.setCreditHolderPassportNumber(resultSet.getString("loan_holder_passport_number"));
            autoLoan.setCreditAmount(resultSet.getBigDecimal("loan_amount"));
            autoLoan.setCurrentCreditAmount(resultSet.getBigDecimal("current_loan_amount"));
            autoLoan.setCreditTermInMonths(resultSet.getInt("loan_term"));
        } catch (Exception e) {
            e.printStackTrace();
    }
        return autoLoan;
    }
}

