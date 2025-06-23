package dao;

import entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> getAllUsers();

    boolean isPassportNumberAvailable(String passportNumber);

    Optional<User> getUserById(int id);

    User getUserByPassportNumber(String passportNumber);

    User saveUser(User user);

    boolean updateUser(User user);

    void deleteUser(long id);

}
