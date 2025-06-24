package service;

import entity.User;
import entity.enums.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    Optional<User> getUserById(final int id);

    boolean updateUser(String passportNumber,String pastPassportNumber, String userName, Gender gender, LocalDate dataOfBirth, long id);

    User saveUser(String passportNumber, String userName, Gender gender, LocalDate dataOfBirth);

    void deleteUser(long id);
}
