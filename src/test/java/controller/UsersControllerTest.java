package controller;

import dao.*;
import entity.User;
import org.junit.jupiter.api.Test;
import service.UserListServiceImpl;
import service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsersControllerTest {

    @Test
    void getAllUsers() {
        // Given
        final AutoLoanDAO autoLoanDAO = new AutoLoanListDAOImpl();
        final DebitCardDAO debitCardDAO = new DebitCardListDAOImpl();
        final MortgageDAO mortgageDAO = new MortgageListDAOImpl();
        final UserDAO userDAO = new UserListDAOImpl(debitCardDAO, mortgageDAO, autoLoanDAO);
        final UserService userService = new UserListServiceImpl(userDAO);

        // When
       final List<User> users = userService.getAllUsers(); // Pass a mock Model if necessary

        // Then
        assertNotNull(users, "Users list should not be null");
        assertFalse(users.isEmpty(), "Users list should not be empty");
        assertEquals(2, users.size(), "There should be 2 users in the list");
    }
}