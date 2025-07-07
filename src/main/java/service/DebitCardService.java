package service;

import dto.DebitCardDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DebitCardService {
    List<DebitCardDTO> getAllDebitCards();

    @Transactional(rollbackFor = Exception.class)
    DebitCardDTO saveDebitCard(String holderPassportNumber, String cardNumber, String cvv, BigDecimal balance, LocalDate expirationDate, LocalDate issueDate);

    Optional<DebitCardDTO> getDebitCardById(int id);

    @Transactional(rollbackFor = Exception.class)
    void updateDebitCard(int id, String holderPassportNumber, String cardNumber,String pastCardNumber, String cvv, BigDecimal balance, LocalDate expirationDate, LocalDate issueDate);

    @Transactional(rollbackFor = Exception.class)
    void deleteDebitCard(int id);

    @Transactional(rollbackFor = Exception.class)
    void transferMoney(String fromCardNumber, String toCardNumber, BigDecimal amount);

    @Transactional(rollbackFor = Exception.class)
    void depositMoney(String cardNumber, BigDecimal amount);
}
