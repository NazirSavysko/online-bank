package dao;

import dto.UserDTO;
import entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<UserDTO> getAllUsers();

    boolean isPassportNumberAvailable(String passportNumber);

    Optional<UserDTO> getUserById(int id);

    User getUserByPassportNumber(String passportNumber);

    UserDTO saveUser(User user);

    boolean updateUser(User user);

    void deleteUser(long id);

}
