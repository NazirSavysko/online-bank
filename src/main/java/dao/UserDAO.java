package dao;

import entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> getAllUsers();
    Optional<User> getUserByPassportNumber(String passportNumber);
    boolean saveUser(User user);
    boolean updateUser(String passportNumber, User user);
    boolean deleteUser(String passportNumber);
}
