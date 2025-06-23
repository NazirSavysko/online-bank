package service;

import entity.User;
import entity.enums.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(final int id);
    User saveUser(String passportNumber, String userName, Gender gender, LocalDate dataOfBirth);
    boolean updateUser(String passportNumber, String userName, Gender gender, LocalDate dataOfBirth, long id);
    void deleteUser(long id);
}
