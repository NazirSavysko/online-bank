package service;

import dto.AutoLoanDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AutoLoanService {
    List<AutoLoanDTO> getAllAutoLoans();

    @Transactional(rollbackFor = Exception.class)
    AutoLoanDTO saveAutoLoan(String UserPassportNumber, BigDecimal amount, BigDecimal currentAmount, int termInMonths);

    Optional<AutoLoanDTO> getAutoLoanById(int autoLoanId);

    @Transactional(rollbackFor = Exception.class)
    boolean updateAutoLoan(String UserPassportNumber, BigDecimal amount, BigDecimal currentAmount, int termInMonths, int autoLoanId);

    @Transactional(rollbackFor = Exception.class)
    void deleteAutoLoan(int autoLoanId);

    @Transactional(rollbackFor = Exception.class)
    void payAutoLoan(int id,BigDecimal amount);
}
