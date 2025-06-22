package service.impl;

import dao.AutoLoanDAO;
import entity.AutoLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.AutoLoanService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AutoLoanServiceImpl implements AutoLoanService {

    private final AutoLoanDAO autoLoanDAO;

    @Autowired
    public AutoLoanServiceImpl(AutoLoanDAO autoLoanDAO) {
        this.autoLoanDAO = autoLoanDAO;
    }

    @Override
    public List<AutoLoan> getAllAutoLoans() {
        return this.autoLoanDAO.getAllAutoLoans();
    }

    @Override
    public AutoLoan saveAutoLoan(final String UserPassportNumber, final BigDecimal amount, final BigDecimal currentAmount, final int termInMonths) {
        final AutoLoan autoLoan = new AutoLoan(0,amount, currentAmount, termInMonths, UserPassportNumber);
        return this.autoLoanDAO.saveAutoLoan(autoLoan);
    }

    @Override
    public Optional<AutoLoan> getAutoLoanById(final int autoLoanId) {
        return this.autoLoanDAO.getAutoLoanById(autoLoanId);
    }

    @Override
    public AutoLoan updateAutoLoan(final Integer currentAmount, final Integer termInMonth, final int autoLoanId) {
        return this.autoLoanDAO.updateAutoLoan(currentAmount,termInMonth, autoLoanId);
    }

    @Override
    public boolean deleteAutoLoan(final int autoLoanId) {
        return  this.autoLoanDAO.deleteAutoLoan(autoLoanId);
    }
}
