package service.impl;

import dao.UserDAO;
import dto.UserDTO;
import entity.User;
import entity.enums.Gender;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public Optional<UserDTO> getUserById(final int id) {
        return userDAO.getUserById(id);
    }

    @Override
    public UserDTO saveUser(final String passportNumber, final String userName, final Gender gender, final LocalDate dataOfBirth) {
        if (this.userDAO.isPassportNumberAvailable(passportNumber)) {
            final User user = User.builder()
                    .userName(userName)
                    .gender(gender)
                    .passportNumber(passportNumber)
                    .dateOfBirth(dataOfBirth)
                    .build();

            return userDAO.saveUser(user);
        } else {
            throw new IllegalArgumentException("UserDTO with passport number '%s' already exists".formatted(passportNumber));
        }
    }

    @Override
    public boolean updateUser(final @NotNull String passportNumber, final String pastPassportNumber, final String userName, final Gender gender, final LocalDate dataOfBirth, final long id) {
        if (passportNumber.equals(pastPassportNumber) || this.userDAO.isPassportNumberAvailable(passportNumber)) {
            final User user = User.builder()
                    .id(id)
                    .userName(userName)
                    .gender(gender)
                    .passportNumber(passportNumber)
                    .dateOfBirth(dataOfBirth)
                    .build();

            return userDAO.updateUser(user);
        } else {
            return false;
        }
    }

    @Override
    public void deleteUser(final long id) {
         userDAO.deleteUser(id);
    }

}
