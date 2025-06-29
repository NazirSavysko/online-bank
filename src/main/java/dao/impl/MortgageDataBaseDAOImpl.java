package dao.impl;

import dao.MortgageDAO;
import dao.UserDAO;
import entity.Mortgage;
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

import static dao.sql.MortgageSql.*;
import static entity.enums.Mortgage_Term.valueOf;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.Objects.requireNonNull;

@Repository
public final class MortgageDataBaseDAOImpl implements MortgageDAO {

    private final JdbcTemplate jdbcTemplate;
    private final UserDAO userDAO;

    @Autowired
    public MortgageDataBaseDAOImpl(final JdbcTemplate jdbcTemplate, final UserDAO userDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDAO = userDAO;
    }

    @Override
    public @NotNull List<Mortgage> getAllMortgages() {
        return jdbcTemplate.query(SELECT_ALL, this::mortgageRowMapper);
    }

    @Contract("_ -> param1")
    @Override
    public @NotNull Mortgage saveMortgage(final @NotNull Mortgage mortgage) {
        final User user = this.userDAO.getUserByPassportNumber(mortgage.getMortgageHolderPassportNumber());
        final GeneratedKeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(INSERT, RETURN_GENERATED_KEYS);
            ps.setLong(1, user.getId());
            ps.setBigDecimal(2, mortgage.getMortgageAmount());
            ps.setBigDecimal(3, mortgage.getCurrentMortgageAmount());
            ps.setString(4, mortgage.getMortgageTerm().toString());
            return ps;
        }, holder);

        mortgage.setId((int) requireNonNull(requireNonNull(holder.getKeys()).get("id")));
        return mortgage;
    }

    @Override
    public @NotNull Optional<Mortgage> getMortgageById(final int mortgageId) {
        final List<Mortgage> mortgages = jdbcTemplate.query(SELECT_BY_ID, this::mortgageRowMapper, mortgageId);

        return mortgages.stream().findFirst();
    }

    @Override
    public boolean updateMortgage(final @NotNull Mortgage mortgage) {
        final User user = this.userDAO.getUserByPassportNumber(mortgage.getMortgageHolderPassportNumber());

        final int rowsAffected = jdbcTemplate.update(UPDATE,
                user.getId(),
                mortgage.getMortgageAmount(),
                mortgage.getCurrentMortgageAmount(),
                mortgage.getMortgageTerm().toString(),
                mortgage.getId()
        );

        return rowsAffected > 0;
    }

    @Override
    public void deleteMortgage(final int id) {
        jdbcTemplate.update(DELETE, id);
    }

    private @NotNull Mortgage mortgageRowMapper(final @NotNull ResultSet resultSet, final int i) throws SQLException {
        final Mortgage mortgage = new Mortgage();

        mortgage.setId(resultSet.getInt("id"));
        mortgage.setMortgageHolderPassportNumber(resultSet.getString("passport_number"));
        mortgage.setMortgageAmount(resultSet.getBigDecimal("mortgage_amount"));
        mortgage.setCurrentMortgageAmount(resultSet.getBigDecimal("current_mortgage_amount"));
        mortgage.setMortgageTerm(valueOf(resultSet.getString("mortgage_term")));

        return mortgage;
    }

}
