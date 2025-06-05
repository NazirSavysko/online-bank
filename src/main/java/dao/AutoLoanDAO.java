package dao;

import entity.AutoLoan;

import java.util.List;

public interface AutoLoanDAO {
    List<AutoLoan> getAllAutoLoans();
    List<AutoLoan> getAutoLoansByPassportId(String passportId);
}
