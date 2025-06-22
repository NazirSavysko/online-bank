package service;

import entity.AutoLoan;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AutoLoanService {
    List<AutoLoan> getAllAutoLoans();
    AutoLoan saveAutoLoan(String UserPassportNumber, BigDecimal amount,BigDecimal currentAmount, int termInMonths);
    Optional<AutoLoan> getAutoLoanById(int autoLoanId);
    AutoLoan updateAutoLoan(Integer currentAmount, Integer termInMonth, int autoLoanId);
    boolean deleteAutoLoan(int autoLoanId);
}
