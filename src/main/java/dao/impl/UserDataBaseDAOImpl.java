package dao.impl;

import dao.UserDAO;
import entity.User;
import entity.enums.Gender;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;


@Repository
@AllArgsConstructor
public final class UserDataBaseDAOImpl implements UserDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAllUsers() {
        try {
            final String sqlForGettingAllUsers = "SELECT * FROM bank_user";
            return jdbcTemplate.query(sqlForGettingAllUsers, this::userRowMapper);
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public Optional<User> getUserByPassportNumber(final String passportNumber) {
        try {
            final String sqlForGettingUserByPassportNumber = "SELECT * FROM bank_user WHERE passport_number = ?";
            final List<User> users = jdbcTemplate
                    .query(sqlForGettingUserByPassportNumber, this::userRowMapper, passportNumber);

            return Optional.ofNullable(users.isEmpty() ? null : users.get(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean saveUser(final User user) {
        try {
            final String sqlForSavingUser = "INSERT INTO bank_user (passport_number, username, birthdate, gender) VALUES (?, ?, ?, ?)";
            final int rowsAffected = jdbcTemplate.update(sqlForSavingUser, user.getPassportNumber(), user.getUserName(),
                    user.getDateOfBirth(), user.getGender().toString());
            return rowsAffected > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateUser(final String passportNumber, final User user) {
        try {
            final String sqlForUpdatingUser = "UPDATE bank_user SET username = ?, birthdate =?,gender = ? WHERE passport_number = ?";
            final int rowsAffected = jdbcTemplate.update(sqlForUpdatingUser, user.getUserName(), user.getDateOfBirth(),
                    user.getGender().toString(), passportNumber);
            return rowsAffected > 0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteUser(final String passportNumber) {
        try {
            final String sqlForDeletingUser = "DELETE FROM bank_user WHERE passport_number = ?";
            int rowsAffected = jdbcTemplate.update(sqlForDeletingUser, passportNumber);
            return rowsAffected > 0;
        }catch (Exception e){
            return false;
        }
    }

    private User userRowMapper(final ResultSet rs, final int rowNum) {
        final User user = new User();
        try {
            user.setUserName(rs.getString("userName"));
            user.setGender(Gender.valueOf(rs.getString("gender")));
            user.setDateOfBirth(rs.getDate("birthdate").toLocalDate());
            user.setPassportNumber(rs.getString("passport_number"));
        } catch (Exception e) {
            System.out.println("Error mapping user: " + e.getMessage());
        }

        return user;
    }
}
