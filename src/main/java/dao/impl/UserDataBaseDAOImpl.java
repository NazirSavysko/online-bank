package dao.impl;

import dao.UserDAO;
import entity.User;
import entity.enums.Gender;
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

import static dao.sql.UserSql.*;
import static java.sql.Date.valueOf;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.Objects.requireNonNull;


@Repository
public final class UserDataBaseDAOImpl implements UserDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDataBaseDAOImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public @NotNull List<User> getAllUsers() {
        return jdbcTemplate.query(SELECT_ALL, this::userRowMapper);
    }

    @Override
    public boolean isPassportNumberAvailable(final String passportNumber) {
        final Integer count = jdbcTemplate.queryForObject(COUNT_BY_PASSPORT_NUMBER, Integer.class, passportNumber);
        return count != null && count == 0;
    }

    @Override
    public @NotNull Optional<User> getUserById(final int id) {
        final List<User> users = jdbcTemplate
                .query(SELECT_BY_ID, this::userRowMapper, id);

        return users.stream().findFirst();
    }

    @Override
    public User getUserByPassportNumber(final String passportNumber) {
        final List<User> users = jdbcTemplate
                .query(SELECT_BY_PASSPORT_NUMBER, this::userRowMapper, passportNumber);

        return users.get(0);
    }

    @Contract("_ -> param1")
    @Override
    public @NotNull User saveUser(final @NotNull User user) {
        final GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(INSERT, RETURN_GENERATED_KEYS);
            ps.setString(1, user.getPassportNumber());
            ps.setString(2, user.getUserName());
            ps.setDate(3, valueOf(user.getDateOfBirth()));
            ps.setString(4, user.getGender().toString());
            return ps;
        }, holder);
        user.setId((long) requireNonNull(requireNonNull(holder.getKeys()).get("id")));

        return user;
    }

    @Override
    public boolean updateUser(final @NotNull User user) {
        final int rowsAffected = jdbcTemplate.update(UPDATE, user.getUserName(), user.getDateOfBirth(),
                user.getGender().toString(), user.getPassportNumber(), user.getId());

        return rowsAffected > 0;
    }

    @Override
    public void deleteUser(final long id) {
        jdbcTemplate.update(DELETE, id);
    }

    private @NotNull User userRowMapper(final @NotNull ResultSet rs, final int rowNum) throws SQLException {
        final User user = new User();

        user.setId(rs.getLong("id"));
        user.setUserName(rs.getString("userName"));
        user.setGender(Gender.valueOf(rs.getString("gender").toUpperCase().trim()));
        user.setDateOfBirth(rs.getDate("birthdate").toLocalDate());
        user.setPassportNumber(rs.getString("passport_number"));

        return user;
    }
}
