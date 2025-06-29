package service.impl;

import dao.UserDAO;
import entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.List.of;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        // Given
        final User user = mock(User.class);
        final List<User> mockUsers = of(user);
        when(userDAO.getAllUsers()).thenReturn(mockUsers);

        // When
        List<User> result = userServiceImpl.getAllUsers();

        // Then
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        verify(userDAO).getAllUsers();
    }

    @Test
    void getUserById_givenExistingId_shouldReturnUser() {
        // Given
        User user = mock(User.class);
        when(userDAO.getUserById(1)).thenReturn(ofNullable(user));

        // When
        User result = userServiceImpl.getUserById(1).orElse(null);

        // Then
        assertNotNull(result);
        assertEquals(user, result);
        verify(userDAO).getUserById(1);
    }

    @Test
    void getUserById_givenNonExistingId_shouldReturnNull() {
        // Given
        when(userDAO.getUserById(0)).thenReturn(empty());

        // When
        User result = userServiceImpl.getUserById(0).orElse(null);

        // Then
        assertNull(result);
        verify(userDAO).getUserById(0);
    }

    @Test
    void saveUser_givenAvailablePassport_shouldReturnSavedUser() {
        // Given
        final long id = 1L;
        final String passport = "1234567890";
        final String name = "John Doe";

        final User expectedUser = new User(id, name, null, null, passport);

        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(true);
        when(userDAO.saveUser(any(User.class))).thenReturn(expectedUser);

        // When
        final User result = userServiceImpl.saveUser(passport, name, null, null);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(passport, result.getPassportNumber());
        assertEquals(name, result.getUserName());
        assertNull(result.getGender());
        assertNull(result.getDateOfBirth());
        verify(userDAO).isPassportNumberAvailable(passport);
        verify(userDAO).saveUser(any(User.class));
    }

    @Test
    void saveUser_givenAvailablePassport_shouldReturnException() {
        // Given
        final String passport = "1234567890";


        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(false);

        // When
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userServiceImpl.saveUser(passport, null, null, null)
        );

        // Then
        assertEquals("User with passport number '1234567890' already exists", exception.getMessage());
        verify(userDAO).isPassportNumberAvailable(passport);
    }

    @Test
    void updateUser_givenSamePassport_shouldUpdateSuccessfully() {
        // Given
        final long id = 1L;
        final String passport = "1234567890";
        final String pastPassport = "1234567890";
        final String name = "John Doe";
        when(userDAO.updateUser(any(User.class))).thenReturn(true);

        // When
        boolean result = userServiceImpl.updateUser(passport, pastPassport, name, null, null, id);

        // Then
        assertTrue(result);
        verify(userDAO).updateUser(any(User.class));
    }

    @Test
    void updateUser_givenChangedPassportAlreadyTaken_shouldFail() {
        // Given
        final long id = 1L;
        final String passport = "NEW123456";
        final String pastPassport = "OLD654321";
        final String name = "John Doe";

        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(false);

        // When
        boolean result = userServiceImpl.updateUser(passport, pastPassport, name, null, null, id);

        // Then
        assertFalse(result);
        verify(userDAO, never()).updateUser(any());
    }

    @Test
    void deleteUser_shouldCallDaoDeleteMethod() {
        // Given
        final long passport = 1L;

        // When
        userServiceImpl.deleteUser(passport);

        // Then
        verify(userDAO).deleteUser(passport);
    }
}