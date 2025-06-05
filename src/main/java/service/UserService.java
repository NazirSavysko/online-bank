package service;

import entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
//    User getUserByPassportId(String passportId);
}
