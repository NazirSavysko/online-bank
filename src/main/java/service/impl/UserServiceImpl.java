package service.impl;

import dao.UserDAO;
import entity.User;
import entity.enums.Gender;
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
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public Optional<User> getUserById(final int id) {
        return userDAO.getUserById(id);
    }

    @Override
    public User saveUser(final String passportNumber, final String userName, final Gender gender, final LocalDate dataOfBirth) {
        if (this.userDAO.isPassportNumberAvailable(passportNumber)) {
            final User user = new User(0, userName, gender, dataOfBirth, passportNumber);
            return userDAO.saveUser(user);
        } else {
            throw new IllegalArgumentException("User with passport number '%s' already exists".formatted(passportNumber));
        }
    }


    @Override
    public boolean updateUser(final String passportNumber, final String userName, final Gender gender, final LocalDate dataOfBirth, final long id) {
        if (this.userDAO.isPassportNumberAvailable(passportNumber)) {
            final User user = new User(id, userName, gender, dataOfBirth, passportNumber);
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
