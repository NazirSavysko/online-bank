package service;

import dao.UserDAO;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserListServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserListServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

//    @Override
//    public User getUserByPassportId(String passportId) {
//        return userDAO.getUserByPassportId(passportId);
//    }
}
