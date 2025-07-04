package service;

import dto.AutoLoanDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AutoLoanService {
    List<AutoLoanDTO> getAllAutoLoans();

    AutoLoanDTO saveAutoLoan(String UserPassportNumber, BigDecimal amount, BigDecimal currentAmount, int termInMonths);

    Optional<AutoLoanDTO> getAutoLoanById(int autoLoanId);

    boolean updateAutoLoan(String UserPassportNumber, BigDecimal amount, BigDecimal currentAmount, int termInMonths, int autoLoanId);

    void deleteAutoLoan(int autoLoanId);
}
