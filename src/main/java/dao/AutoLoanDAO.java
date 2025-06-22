package dao;

import entity.AutoLoan;

import java.util.List;
import java.util.Optional;

public interface AutoLoanDAO {
    List<AutoLoan> getAllAutoLoans();
    AutoLoan saveAutoLoan(AutoLoan autoLoan);

    Optional<AutoLoan> getAutoLoanById(int autoLoanId);

    AutoLoan updateAutoLoan(Integer currentAmount,Integer termInMonth, int autoLoanId);

    boolean deleteAutoLoan(int autoLoanId);
}
