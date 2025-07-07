package dao;

import dto.AutoLoanDTO;
import entity.AutoLoan;

import java.util.List;
import java.util.Optional;

public interface AutoLoanDAO {
    List<AutoLoanDTO> getAllAutoLoans();

    AutoLoanDTO saveAutoLoan(AutoLoan autoLoan);

    Optional<AutoLoanDTO> getAutoLoanById(int autoLoanId);

    void deleteAutoLoan(int autoLoanId);

    boolean updateAutoLoan(AutoLoan autoLoan);

    int getAutoLoanCountByUserPassportNumber(String holderPassport);
}
