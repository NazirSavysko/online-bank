package service.impl;

import dao.AutoLoanDAO;
import dao.MortgageDAO;
import dao.UserDAO;
import dto.MortgageDTO;
import entity.Mortgage;
import entity.enums.Mortgage_Term;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static entity.enums.Mortgage_Term.TWENTY_YEARS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MortgageServiceImplTest {

    @Mock
    private MortgageDAO mortgageDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private AutoLoanDAO autoLoanDAO;

    @InjectMocks
    private MortgageServiceImpl mortgageServiceImpl;

    @Test
    void getAllMortgages_shouldReturnListOfMortgages() {
        // Given
        final MortgageDTO mortgage = mock(MortgageDTO.class);
        final List<MortgageDTO> mockMortgages = List.of(mortgage);

        when(mortgageDAO.getAllMortgages()).thenReturn(mockMortgages);

        // When
        final List<MortgageDTO> mortgages = mortgageServiceImpl.getAllMortgages();

        // Then
        assertEquals(1, mortgages.size());
        assertEquals(mortgage, mortgages.get(0));
        verify(mortgageDAO).getAllMortgages();
    }

    @Test
    void saveMortgage_givenExistingUser_shouldSaveSuccessfully() {
        // Given
        final String passport = "AA123456";
        final BigDecimal amount = new BigDecimal("150000");
        final BigDecimal currentAmount = new BigDecimal("50000");
        final Mortgage_Term term = TWENTY_YEARS;

        final MortgageDTO expected = new MortgageDTO(0, amount, passport, currentAmount, term);

        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(false);
        when(mortgageDAO.saveMortgage(any(Mortgage.class))).thenReturn(expected);

        // When
        final MortgageDTO result = mortgageServiceImpl.saveMortgage(passport, term, amount, currentAmount);

        // Then
        assertEquals(expected, result);
        verify(userDAO).isPassportNumberAvailable(passport);
        verify(mortgageDAO).saveMortgage(any(Mortgage.class));
    }

    @Test
    void saveMortgage_givenNonExistentUser_shouldThrowException() {
        // Given
        final String passport = "AA123456";
        final BigDecimal amount = new BigDecimal("150000");
        final BigDecimal currentAmount = new BigDecimal("50000");

        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(true);

        // When
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mortgageServiceImpl.saveMortgage(passport, TWENTY_YEARS, amount, currentAmount)
        );

        // Then
        assertEquals("UserDTO with passport number 'AA123456' does not exist", exception.getMessage());
        verify(userDAO).isPassportNumberAvailable(passport);
        verify(mortgageDAO, never()).saveMortgage(any());
    }

    @Test
    void saveMortgage_whenUserHasThreeLoansAndMortgages_shouldThrowException() {
        // Given
        final String passport = "AA123456";
        final BigDecimal amount = new BigDecimal("150000");
        final BigDecimal currentAmount = new BigDecimal("50000");

        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(false);
        when(mortgageDAO.getMortgageCountByUserPassportNumber(passport)).thenReturn(2);
        when(autoLoanDAO.getAutoLoanCountByUserPassportNumber(passport)).thenReturn(1);

        // When
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mortgageServiceImpl.saveMortgage(passport, TWENTY_YEARS, amount, currentAmount)
        );

        // Then
        assertEquals("UserDTO with passport number 'AA123456' has enough mortgages and auto loans", exception.getMessage());
    }
    @Test
    void getMortgageById_givenExistingId_shouldReturnMortgage() {
        // Given
        final int id = 1;
        final MortgageDTO expected = mock(MortgageDTO.class);

        when(mortgageDAO.getMortgageById(id)).thenReturn(Optional.of(expected));

        // When
        final Optional<MortgageDTO> result = mortgageServiceImpl.getMortgageById(id);

        // Then
        assertEquals(expected, result.orElse(null));
        verify(mortgageDAO).getMortgageById(id);
    }

    @Test
    void getMortgageById_givenNonExistingId_shouldReturnEmpty() {
        // Given
        final int id = 1;

        when(mortgageDAO.getMortgageById(id)).thenReturn(Optional.empty());

        // When
        final Optional<MortgageDTO> result = mortgageServiceImpl.getMortgageById(id);

        // Then
        assertFalse(result.isPresent());
        verify(mortgageDAO).getMortgageById(id);
    }

    @Test
    void updateMortgage_givenExistingUser_shouldReturnTrue() {
        // Given
        final String passport = "AA123456";
        final BigDecimal amount = new BigDecimal("150000");
        final BigDecimal currentAmount = new BigDecimal("50000");
        final int id = 1;

        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(false);
        when(mortgageDAO.updateMortgage(any(Mortgage.class))).thenReturn(true);

        // When
        final boolean result = mortgageServiceImpl.updateMortgage(passport, TWENTY_YEARS, amount, currentAmount, id);

        // Then
        assertTrue(result);
        verify(userDAO).isPassportNumberAvailable(passport);
        verify(mortgageDAO).updateMortgage(any(Mortgage.class));
    }

    @Test
    void updateMortgage_givenNonExistentUser_shouldReturnFalse() {
        // Given
        final String passport = "AA123456";
        final BigDecimal amount = new BigDecimal("150000");
        final BigDecimal currentAmount = new BigDecimal("50000");
        final int id = 1;

        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(true);

        // When
        final boolean result = mortgageServiceImpl.updateMortgage(passport, TWENTY_YEARS, amount, currentAmount, id);

        // Then
        assertFalse(result);
        verify(userDAO).isPassportNumberAvailable(passport);
        verify(mortgageDAO, never()).updateMortgage(any());
    }

    @Test
    void deleteMortgage_shouldCallDaoDelete() {
        // Given
        final int id = 1;

        // When
        mortgageServiceImpl.deleteMortgage(id);

        // Then
        verify(mortgageDAO).deleteMortgage(id);
    }

    @Test
    void payMortgage_givenExistingMortgage_shouldUpdate() {
        // Given
        final int id = 1;
        final BigDecimal paymentAmount = new BigDecimal("5000");
        final MortgageDTO existingMortgage = mock(MortgageDTO.class);
        when(existingMortgage.currentMortgageAmount()).thenReturn(new BigDecimal("10000"));
        when(mortgageDAO.getMortgageById(id)).thenReturn(Optional.of(existingMortgage));

        // When
        mortgageServiceImpl.payMortgage(id, paymentAmount);

        // Then
        verify(mortgageDAO).getMortgageById(id);
        verify(mortgageDAO).updateMortgage(any(Mortgage.class));
    }

    @Test
    void payMortgage_givenMortgagePaidOff_shouldDelete() {
        // Given
        final int id = 1;
        final BigDecimal paymentAmount = new BigDecimal("10000");
        final MortgageDTO existingMortgage = mock(MortgageDTO.class);
        when(existingMortgage.currentMortgageAmount()).thenReturn(new BigDecimal("10000"));
        when(mortgageDAO.getMortgageById(id)).thenReturn(Optional.of(existingMortgage));

        // When
        mortgageServiceImpl.payMortgage(id, paymentAmount);

        // Then
        verify(mortgageDAO).getMortgageById(id);
        verify(mortgageDAO).deleteMortgage(id);
    }

    @Test
    void payMortgage_givenNonExistingMortgage_shouldThrowException() {
        // Given
        final int id = 1;
        final BigDecimal paymentAmount = new BigDecimal("5000");

        when(mortgageDAO.getMortgageById(id)).thenReturn(Optional.empty());

        // When
        final NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> mortgageServiceImpl.payMortgage(id, paymentAmount)
        );

        // Then
        assertEquals("Mortgage with ID 1 not found.", exception.getMessage());
        verify(mortgageDAO).getMortgageById(id);
        verify(mortgageDAO, never()).updateMortgage(any());
    }
}