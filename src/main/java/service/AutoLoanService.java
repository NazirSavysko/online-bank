package service;

import entity.AutoLoan;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AutoLoanService {
    List<AutoLoan> getAllAutoLoans();
    AutoLoan saveAutoLoan(String UserPassportNumber, BigDecimal amount, BigDecimal currentAmount, int termInMonths);
    Optional<AutoLoan> getAutoLoanById(int autoLoanId);
    boolean updateAutoLoan(String UserPassportNumber, BigDecimal amount, BigDecimal currentAmount, int termInMonths, int autoLoanId);
    void deleteAutoLoan(int autoLoanId);
}
