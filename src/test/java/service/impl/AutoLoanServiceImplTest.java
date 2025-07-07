package service.impl;

import dao.AutoLoanDAO;
import dao.MortgageDAO;
import dao.UserDAO;
import dto.AutoLoanDTO;
import entity.AutoLoan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutoLoanServiceImplTest {

    @Mock
    private AutoLoanDAO autoLoanDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private MortgageDAO mortgageDAO;

    @InjectMocks
    private AutoLoanServiceImpl autoLoanServiceImpl;

    @Test
    void getAllAutoLoans_shouldReturnListOfLoans() {
        // Given
        final AutoLoanDTO autoLoanDTO = mock(AutoLoanDTO.class);
        final List<AutoLoanDTO> mockAutoLoans = List.of(autoLoanDTO);
        when(autoLoanDAO.getAllAutoLoans()).thenReturn(mockAutoLoans);

        // When
        final List<AutoLoanDTO> autoLoans = autoLoanServiceImpl.getAllAutoLoans();

        // Then
        assertEquals(1, autoLoans.size());
        assertEquals(autoLoanDTO, autoLoans.get(0));
        verify(autoLoanDAO).getAllAutoLoans();
    }

    @Test
    void saveAutoLoan_givenExistingUser_shouldSaveSuccessfully() {
        // Given
        final String passport = "AA123456";
        final BigDecimal amount = new BigDecimal("10000");
        final BigDecimal currentAmount = new BigDecimal("5000");
        final int term = 12;

        final AutoLoanDTO expectedLoan = new AutoLoanDTO(0, amount, currentAmount, term, passport);

        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(false);
        when(autoLoanDAO.saveAutoLoan(any(AutoLoan.class))).thenReturn(expectedLoan);

        // When
        final AutoLoanDTO result = autoLoanServiceImpl.saveAutoLoan(passport, amount, currentAmount, term);

        // Then
        assertNotNull(result);
        assertEquals(expectedLoan, result);
        verify(userDAO).isPassportNumberAvailable(passport);
        verify(autoLoanDAO).saveAutoLoan(any(AutoLoan.class));
    }

    @Test
    void saveAutoLoan_whenUserHasThreeLoansAndMortgages_shouldThrowException() {
        // Given
        final String passport = "AA123456";
        final BigDecimal amount = new BigDecimal("10000");
        final BigDecimal currentAmount = new BigDecimal("5000");
        final int term = 12;


        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(false);
        when(autoLoanDAO.getAutoLoanCountByUserPassportNumber(passport)).thenReturn(2);
        when(mortgageDAO.getMortgageCountByUserPassportNumber(passport)).thenReturn(1);

        // When
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> autoLoanServiceImpl.saveAutoLoan(passport, amount, currentAmount, term)
        );

        // Then
        assertEquals("User with passport number 'AA123456' has enough mortgages and auto loans", exception.getMessage());
    }


    @Test
    void saveAutoLoan_givenNonExistentUser_shouldThrowException() {
        // Given
        final String passport = "NOT_EXISTING";
        final BigDecimal amount = new BigDecimal("10000");
        final BigDecimal currentAmount = new BigDecimal("5000");
        final int term = 12;

        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(true);

        // When
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> autoLoanServiceImpl.saveAutoLoan(passport, amount, currentAmount, term)
        );

        // Then
        assertEquals("UserDTO with passport number 'NOT_EXISTING' does not exist", exception.getMessage());
        verify(userDAO).isPassportNumberAvailable(passport);
        verify(autoLoanDAO, never()).saveAutoLoan(any());
    }

    @Test
    void getAutoLoanById_givenExistingId_shouldReturnLoan() {
        // Given
        final int autoLoanId = 1;
        final AutoLoanDTO expectedAutoLoan = new AutoLoanDTO(autoLoanId, new BigDecimal("10000"), new BigDecimal("5000"), 12, "AA123456");

        when(autoLoanDAO.getAutoLoanById(autoLoanId)).thenReturn(Optional.of(expectedAutoLoan));

        // When
        final Optional<AutoLoanDTO> result = autoLoanServiceImpl.getAutoLoanById(autoLoanId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedAutoLoan, result.get());
        verify(autoLoanDAO).getAutoLoanById(autoLoanId);
    }

    @Test
    void getAutoLoanById_givenNonExistingId_shouldReturnEmpty() {
        // Given
        final int autoLoanId = 1;
        when(autoLoanDAO.getAutoLoanById(autoLoanId)).thenReturn(Optional.empty());

        // When
        final Optional<AutoLoanDTO> result = autoLoanServiceImpl.getAutoLoanById(autoLoanId);

        // Then
        assertFalse(result.isPresent());
        verify(autoLoanDAO).getAutoLoanById(autoLoanId);
    }

    @Test
    void updateAutoLoan_givenExistingUser_shouldUpdateSuccessfully() {
        // Given
        final String passport = "AA123456";
        final BigDecimal amount = new BigDecimal("15000");
        final BigDecimal currentAmount = new BigDecimal("7000");
        final int term = 24;
        final int autoLoanId = 1;

        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(false);
        when(autoLoanDAO.updateAutoLoan(any(AutoLoan.class))).thenReturn(true);

        // When
        final boolean result = autoLoanServiceImpl.updateAutoLoan(passport, amount, currentAmount, term, autoLoanId);

        // Then
        assertTrue(result);
        verify(userDAO).isPassportNumberAvailable(passport);
        verify(autoLoanDAO).updateAutoLoan(any(AutoLoan.class));
    }

    @Test
    void updateAutoLoan_givenNonExistentUser_shouldReturnFalse() {
        // Given
        final String passport = "AA123456";
        final BigDecimal amount = new BigDecimal("15000");
        final BigDecimal currentAmount = new BigDecimal("7000");
        final int term = 24;
        final int autoLoanId = 1;

        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(true);

        // When
        final boolean result = autoLoanServiceImpl.updateAutoLoan(passport, amount, currentAmount, term, autoLoanId);

        // Then
        assertFalse(result);
        verify(userDAO).isPassportNumberAvailable(passport);
        verify(autoLoanDAO, never()).updateAutoLoan(any());
    }

    @Test
    void deleteAutoLoan_shouldCallDaoDelete() {
        // Given
        final int autoLoanId = 1;

        // When
        autoLoanServiceImpl.deleteAutoLoan(autoLoanId);

        // Then
        verify(autoLoanDAO).deleteAutoLoan(autoLoanId);
    }

    @Test
    void payAutoLoan_givenExistingLoan_shouldDelete() {
        // Given
        final int autoLoanId = 1;
        final BigDecimal paymentAmount = new BigDecimal("5000");
        final AutoLoanDTO existingLoan = new AutoLoanDTO(autoLoanId, new BigDecimal("10000"), new BigDecimal("5000"), 12, "AA123456");

        when(autoLoanDAO.getAutoLoanById(autoLoanId)).thenReturn(Optional.of(existingLoan));

        // When
        autoLoanServiceImpl.payAutoLoan(autoLoanId, paymentAmount);

        // Then
        verify(autoLoanDAO).getAutoLoanById(autoLoanId);
        verify(autoLoanDAO).deleteAutoLoan(autoLoanId);
    }

    @Test
    void payAutoLoan_givenNonExistingLoan_shouldNotUpdateOrDelete() {
        // Given
        final int autoLoanId = 1;
        final BigDecimal paymentAmount = new BigDecimal("5000");

        when(autoLoanDAO.getAutoLoanById(autoLoanId)).thenReturn(Optional.empty());

        // When
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> autoLoanServiceImpl.payAutoLoan(autoLoanId, paymentAmount)
        );

        // Then
        assertEquals("Auto loan with id '1' does not exist", exception.getMessage());
    }

    @Test
    void payAutoLoan_givenExistingLoanWithRemainingAmount_shouldUpdateCurrentAmount() {
        // Given
        final int autoLoanId = 1;
        final BigDecimal paymentAmount = new BigDecimal("2000");
        final AutoLoanDTO existingLoan = new AutoLoanDTO(autoLoanId, new BigDecimal("10000"), new BigDecimal("5000"), 12, "AA123456");

        when(autoLoanDAO.getAutoLoanById(autoLoanId)).thenReturn(Optional.of(existingLoan));

        // When
        autoLoanServiceImpl.payAutoLoan(autoLoanId, paymentAmount);

        // Then
        verify(autoLoanDAO).getAutoLoanById(autoLoanId);
        verify(autoLoanDAO).updateAutoLoan(any(AutoLoan.class));
    }
}
