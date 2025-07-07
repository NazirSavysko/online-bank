package service;

import dto.UserDTO;
import entity.enums.Gender;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> getAllUsers();

    Optional<UserDTO> getUserById(final int id);

    @Transactional(rollbackFor = Exception.class)
    boolean updateUser(String passportNumber, String pastPassportNumber, String userName, Gender gender, LocalDate dataOfBirth, long id);

    @Transactional(rollbackFor = Exception.class)
    UserDTO saveUser(String passportNumber, String userName, Gender gender, LocalDate dataOfBirth);

    @Transactional(rollbackFor = Exception.class)
    void deleteUser(long id);
}
