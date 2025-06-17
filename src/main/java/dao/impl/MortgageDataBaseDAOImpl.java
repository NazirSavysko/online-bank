package dao.impl;

import dao.MortgageDAO;
import entity.Mortgage;
import entity.enums.Mortgage_Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.copyOf;

@Repository
public final class MortgageDataBaseDAOImpl implements MortgageDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MortgageDataBaseDAOImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mortgage> getAllMortgages() {
        final String sqlForGettingAllMortgages = "SELECT * FROM mortgage";
        return jdbcTemplate.query(sqlForGettingAllMortgages, this::mortgageRowMapper);
    }

    private Mortgage mortgageRowMapper(ResultSet resultSet, int i) {
        final Mortgage mortgage = new Mortgage();
        try {
            mortgage.setId(resultSet.getInt("id"));
            mortgage.setMortgageHolderPassportNumber(resultSet.getString("mortgage_holder_passport_number"));
            mortgage.setMortgageAmount(resultSet.getBigDecimal("mortgage_amount"));
            mortgage.setCurrentMortgageAmount(resultSet.getBigDecimal("current_mortgage_amount"));
            mortgage.setMortgageTerm(Mortgage_Term.valueOf(resultSet.getString("mortgage_term")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mortgage;
    }

}
