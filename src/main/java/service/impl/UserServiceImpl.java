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
    public Optional<User> getUserByPassportNumber(final String passportNumber) {
        return userDAO.getUserByPassportNumber(passportNumber);
    }

    @Override
    public boolean saveUser(final String passportNumber, final String userName, final Gender gender, final LocalDate dataOfBirth) {
        final User user = new User(userName, gender, dataOfBirth,passportNumber);
        return userDAO.saveUser(user);
    }


    @Override
    public boolean updateUser(final String passportNumber, final String userName, final Gender gender, final LocalDate dataOfBirth) {
        final User user = new User(userName, gender, dataOfBirth,passportNumber);
        return userDAO.updateUser(passportNumber,user);
    }

    @Override
    public boolean deleteUser(final String passportNumber) {
        return userDAO.deleteUser(passportNumber);
    }

}
