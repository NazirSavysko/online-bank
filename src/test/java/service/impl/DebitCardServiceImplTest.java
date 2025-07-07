package service.impl;

import dao.DebitCardDAO;
import dao.UserDAO;
import dto.DebitCardDTO;
import entity.DebitCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.TEN;
import static java.time.LocalDate.now;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebitCardServiceImplTest {

    @Mock
    private DebitCardDAO debitCardDAO;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private DebitCardServiceImpl debitCardServiceImpl;


    @Test
    void getAllDebitCards_shouldReturnListOfCards() {
        // Given
        final DebitCardDTO card = mock(DebitCardDTO.class);
        final List<DebitCardDTO> cards = List.of(card);

        when(debitCardDAO.getAllDebitCards()).thenReturn(cards);

        // When
        final List<DebitCardDTO> result = debitCardServiceImpl.getAllDebitCards();

        // Then
        assertEquals(1, result.size());
        assertEquals(card, result.get(0));
        verify(debitCardDAO).getAllDebitCards();
    }

    @Test
    void saveDebitCard_givenValidData_shouldSaveSuccessfully() {
        // Given
        final String passport = "AA123456";
        final String cardNumber = "1111-2222-3333-4444";
        final String cvv = "123";
        final BigDecimal balance = new BigDecimal("5000");
        final LocalDate issueDate = LocalDate.of(2024, 1, 1);
        final LocalDate expirationDate = LocalDate.of(2028, 1, 1);

        final DebitCardDTO expected = new DebitCardDTO(0, cardNumber, passport, expirationDate, issueDate, cvv, balance);

        when(debitCardDAO.isCardNumberAvailable(cardNumber)).thenReturn(true);
        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(false);
        when(debitCardDAO.saveDebitCard(any(DebitCard.class))).thenReturn(expected);

        // When
        final DebitCardDTO result = debitCardServiceImpl.saveDebitCard(passport, cardNumber, cvv, balance, expirationDate, issueDate);

        // Then
        assertEquals(expected, result);
        verify(debitCardDAO).isCardNumberAvailable(cardNumber);
        verify(userDAO).isPassportNumberAvailable(passport);
        verify(debitCardDAO).saveDebitCard(any(DebitCard.class));
    }

    @Test
    void saveDebitCard_givenUnavailableCardNumber_shouldThrowException() {
        // Given
        final String cardNumber = "1111-2222-3333-4444";

        when(debitCardDAO.isCardNumberAvailable(cardNumber)).thenReturn(false);

        // When + Then
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> debitCardServiceImpl.saveDebitCard("AA123456", cardNumber, "123", TEN, now(), now())
        );

        assertEquals("Card number 1111-2222-3333-4444 is already in use.", exception.getMessage());
        verify(debitCardDAO).isCardNumberAvailable(cardNumber);
    }

    @Test
    void saveDebitCard_givenNonExistentUser_shouldThrowException() {
        // Given
        final String passport = "AA123456";
        final String cardNumber = "1111-2222-3333-4444";

        when(debitCardDAO.isCardNumberAvailable(cardNumber)).thenReturn(true);
        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(true);

        // When + Then
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> debitCardServiceImpl.saveDebitCard(passport, cardNumber, "123", TEN, now(), now())
        );

        assertEquals("UserDTO with passport number AA123456 does not exist.", exception.getMessage());
        verify(userDAO).isPassportNumberAvailable(passport);
    }

    @Test
    void getDebitCardById_givenExistingId_shouldReturnCard() {
        // Given
        final int id = 1;
        final DebitCardDTO expected = mock(DebitCardDTO.class);

        when(debitCardDAO.getDebitCardById(id)).thenReturn(Optional.of(expected));

        // When
        final Optional<DebitCardDTO> result = debitCardServiceImpl.getDebitCardById(id);

        // Then
        assertEquals(expected, result.orElse(null));
        verify(debitCardDAO).getDebitCardById(id);
    }

    @Test
    void getDebitCardById_givenExistingId_shouldReturnNull() {
        // Given
        final int id = 1;

        when(debitCardDAO.getDebitCardById(id)).thenReturn(empty());

        // When
        final Optional<DebitCardDTO> result = debitCardServiceImpl.getDebitCardById(id);

        // Then
        assertNull(result.orElse(null));
        verify(debitCardDAO).getDebitCardById(id);
    }


    @Test
    void updateDebitCard_givenValidChange_shouldUpdateSuccessfully() {
        // Given
        final int id = 1;
        final String passport = "AA123456";
        final String cardNumber = "1111-2222-3333-4444";
        final String cvv = "123";
        final LocalDate issueDate = LocalDate.of(2024, 1, 1);
        final LocalDate expirationDate = LocalDate.of(2028, 1, 1);

        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(false);
        doNothing().when(debitCardDAO).updateDebitCard(any(DebitCard.class));

        // When
        debitCardServiceImpl.updateDebitCard(id, passport, cardNumber, cardNumber, cvv, TEN, expirationDate, issueDate);

        // Then
        verify(debitCardDAO).updateDebitCard(any(DebitCard.class));
        verify(userDAO).isPassportNumberAvailable(passport);
    }

    @Test
    void updateDebitCard_givenInvalidPassport_shouldThrowException() {
        // Given
        final int id = 1;
        final String cardNumber = "1111-2222-3333-4444";
        final String passport = "ZZ999999";
        final String cvv = "123";
        final LocalDate issueDate = LocalDate.of(2024, 1, 1);
        final LocalDate expirationDate = LocalDate.of(2028, 1, 1);

        when(userDAO.isPassportNumberAvailable(passport)).thenReturn(true);

        // When
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> debitCardServiceImpl.updateDebitCard(id, passport, cardNumber, cardNumber, cvv, TEN, expirationDate, issueDate)
        );

        // Then
        assertEquals("UserDTO with passport number ZZ999999 does not exist.", exception.getMessage());
        verify(userDAO).isPassportNumberAvailable(passport);
    }

    @Test
    void updateDebitCard_givenUnavailableNewCardNumber_shouldThrowException() {
        // Given
        final int id = 1;
        final String oldCardNumber = "1111-2222-3333-4444";
        final String newCardNumber = "9999-8888-7777-6666";
        final String passport = "AA123456";
        final String cvv = "123";
        final LocalDate issueDate = LocalDate.of(2024, 1, 1);
        final LocalDate expirationDate = LocalDate.of(2028, 1, 1);

        when(debitCardDAO.isCardNumberAvailable(newCardNumber)).thenReturn(false);

        // When
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> debitCardServiceImpl.updateDebitCard(id, passport, newCardNumber, oldCardNumber, cvv, TEN, expirationDate, issueDate)
        );

        // Then
        assertEquals("Card number 9999-8888-7777-6666 is already in use.", exception.getMessage());
        verify(debitCardDAO).isCardNumberAvailable(newCardNumber);
        verify(userDAO, never()).isPassportNumberAvailable(any());
        verify(debitCardDAO, never()).updateDebitCard(any());
    }


    @Test
    void deleteDebitCard_shouldCallDaoDelete() {
        // Given
        final int id = 1;

        // When
        debitCardServiceImpl.deleteDebitCard(id);

        // Then
        verify(debitCardDAO).deleteDebitCard(id);
    }

    @Test
    void depositMoney_shouldCallDaoDeposit() {
        // Given
        final String cardNumber = "1111222233334444";
        final BigDecimal amount = new BigDecimal("100.00");

        when(debitCardDAO.isCardNumberAvailable(cardNumber)).thenReturn(false);
        when(debitCardDAO.getDebitCardByCardNumber(anyString())).thenReturn(new DebitCard(1, cardNumber, null, LocalDate.now(), LocalDate.now(), "123", BigDecimal.ZERO));

        // When
        debitCardServiceImpl.depositMoney(cardNumber, amount);

        // Then
        verify(debitCardDAO).updateDebitCard(any(DebitCard.class));
    }

    @Test
    void depositMoney_shouldThrowException() {
        // Given
        final String cardNumber = "1111222233334444";
        final BigDecimal amount = new BigDecimal("100.00");

        when(debitCardDAO.isCardNumberAvailable(cardNumber)).thenReturn(true);

        // When
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> debitCardServiceImpl.depositMoney(cardNumber, amount)
        );

        // Then
        assertEquals("Card number 1111222233334444 does not exist.", exception.getMessage());
        verify(debitCardDAO).isCardNumberAvailable(cardNumber);
    }

    @Test
    void transferMoney_whenCardNumberIsEqual_shouldThrowException() {
        // Given
        final String fromCardNumber = "1111222233334444";
        final String toCardNumber = "1111222233334444";
        final BigDecimal amount = new BigDecimal("100.00");

        // When
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> debitCardServiceImpl.transferMoney(fromCardNumber, toCardNumber, amount)
        );

        // Then
        assertEquals("Cannot transfer money to the same card: 1111222233334444", exception.getMessage());
    }

    @Test
    void transferMoney_whenFirstCardNumberAvailable_shouldTrowException() {
        // Given
        final String fromCardNumber = "1111222233334444";
        final String toCardNumber = "2222333344445555";
        final BigDecimal amount = new BigDecimal("100.00");

        when(debitCardDAO.isCardNumberAvailable(fromCardNumber)).thenReturn(true);
        when(debitCardDAO.isCardNumberAvailable(toCardNumber)).thenReturn(false);

        // When
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> debitCardServiceImpl.transferMoney(fromCardNumber, toCardNumber, amount)
        );

        // Then
        assertEquals("Card number 1111222233334444 does not exist.", exception.getMessage());
    }

    @Test
    void transferMoney_whenSecondCardNumberAvailable_shouldThrowException() {
        // Given
        final String fromCardNumber = "1111222233334444";
        final String toCardNumber = "2222333344445555";
        final BigDecimal amount = new BigDecimal("100.00");

        when(debitCardDAO.isCardNumberAvailable(toCardNumber)).thenReturn(true);

        // When
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> debitCardServiceImpl.transferMoney(fromCardNumber, toCardNumber, amount)
        );

        // Then
        assertEquals("Card number 2222333344445555 does not exist.", exception.getMessage());
    }

    @Test
    void transferMoney_whenBothCardNumbersAvailable_shouldTransferSuccessfully() {
        // Given
        final String fromCardNumber = "1111222233334444";
        final String toCardNumber = "2222333344445555";
        final BigDecimal amount = new BigDecimal("100.00");

        when(debitCardDAO.isCardNumberAvailable(fromCardNumber)).thenReturn(false);
        when(debitCardDAO.isCardNumberAvailable(toCardNumber)).thenReturn(false);
        when(debitCardDAO.getDebitCardByCardNumber(fromCardNumber)).thenReturn(new DebitCard(1, fromCardNumber, null, LocalDate.now(), LocalDate.now(), "123", new BigDecimal("500.00")));
        when(debitCardDAO.getDebitCardByCardNumber(toCardNumber)).thenReturn(new DebitCard(2, toCardNumber, null, LocalDate.now(), LocalDate.now(), "456", new BigDecimal("200.00")));

        // When
        debitCardServiceImpl.transferMoney(fromCardNumber, toCardNumber, amount);

        // Then
        verify(debitCardDAO, times(2)).updateDebitCard(any(DebitCard.class));
    }

    @Test
    void transferMoney_whenBothCardNumbersAvailable_shouldThrowException() {
        // Given
        final String fromCardNumber = "1111222233334444";
        final String toCardNumber = "2222333344445555";
        final BigDecimal amount = new BigDecimal("600.00");

        when(debitCardDAO.isCardNumberAvailable(fromCardNumber)).thenReturn(false);
        when(debitCardDAO.isCardNumberAvailable(toCardNumber)).thenReturn(false);
        when(debitCardDAO.getDebitCardByCardNumber(fromCardNumber)).thenReturn(new DebitCard(1, fromCardNumber, null, LocalDate.now(), LocalDate.now(), "123", new BigDecimal("500.00")));
        when(debitCardDAO.getDebitCardByCardNumber(toCardNumber)).thenReturn(new DebitCard(2, toCardNumber, null, LocalDate.now(), LocalDate.now(), "456", new BigDecimal("200.00")));

        // When
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> debitCardServiceImpl.transferMoney(fromCardNumber, toCardNumber, amount)
        );

        // Then
        assertEquals("Insufficient funds on card 1111222233334444", exception.getMessage());
    }
}