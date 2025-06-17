package dao.impl;

import dao.UserDAO;
import entity.User;
import entity.enums.Gender;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;



@Repository
@AllArgsConstructor
public final class UserDataBaseDAOImpl implements UserDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAllUsers() {
        final String sqlForGettingAllUsers = "SELECT * FROM bank_user";

        return jdbcTemplate.query(sqlForGettingAllUsers,this::userRowMapper);
    }

    @Override
    public Optional<User> getUserByPassportNumber(final String passportNumber) {
        final String sqlForGettingUserByPassportNumber = "SELECT * FROM bank_user WHERE passport_number = ?";
        final List<User> users = jdbcTemplate
                .query(sqlForGettingUserByPassportNumber,this::userRowMapper, passportNumber);

        return Optional.ofNullable(users.isEmpty() ? null : users.get(0));
    }

    @Override
    public boolean saveUser(final User user) {
        final String sqlForSavingUser = "INSERT INTO bank_user (passport_number, username, birthday, gender) VALUES (?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sqlForSavingUser, user.getPassportNumber(), user.getUsername(),
                user.getDateOfBirth(), user.getGender());

        return rowsAffected > 0;
    }

    @Override
    public boolean updateUser(final String passportNumber,final User user) {
        final String sqlForUpdatingUser = "UPDATE bank_user SET username = ?, birthday =?,gender = ? WHERE passport_number = ?";
        int rowsAffected = jdbcTemplate.update(sqlForUpdatingUser, user.getUsername(), user.getDateOfBirth(),
                user.getGender(), passportNumber);
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteUser(final String passportNumber) {
        final String sqlForDeletingUser = "DELETE FROM bank_user WHERE passport_number = ?";
        int rowsAffected = jdbcTemplate.update(sqlForDeletingUser, passportNumber);
        return rowsAffected > 0;
    }

    private User userRowMapper(final ResultSet rs, final int rowNum) {
        final User user = new User();
        try {
            user.setUsername(rs.getString("username"));
            user.setGender(Gender.valueOf(rs.getString("gender")));

            Date sqlDate = rs.getDate("birthdate");
            if (sqlDate != null) {
                user.setDateOfBirth(sqlDate.toLocalDate());
            }
            user.setPassportNumber(rs.getString("passport_number"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}
