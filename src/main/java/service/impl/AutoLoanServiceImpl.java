package service.impl;

import dao.AutoLoanDAO;
import dao.UserDAO;
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
    private final UserDAO userDAO;


    @Autowired
    public AutoLoanServiceImpl(final AutoLoanDAO autoLoanDAO, final UserDAO userDAO) {
        this.autoLoanDAO = autoLoanDAO;
        this.userDAO = userDAO;
    }

    @Override
    public List<AutoLoan> getAllAutoLoans() {
        return this.autoLoanDAO.getAllAutoLoans();
    }

    @Override
    public AutoLoan saveAutoLoan(final String UserPassportNumber, final BigDecimal amount, final BigDecimal currentAmount, final int termInMonths) {
        if (!this.userDAO.isPassportNumberAvailable(UserPassportNumber)) {
            final AutoLoan autoLoan = new AutoLoan(0, amount, currentAmount, termInMonths, UserPassportNumber);
            return this.autoLoanDAO.saveAutoLoan(autoLoan);
        } else {
            throw new IllegalArgumentException("User with passport number '%s' does not exist".formatted(UserPassportNumber));
        }
    }

    @Override
    public Optional<AutoLoan> getAutoLoanById(final int autoLoanId) {
        return this.autoLoanDAO.getAutoLoanById(autoLoanId);
    }

    @Override
    public boolean updateAutoLoan(final String UserPassportNumber, final BigDecimal amount, final BigDecimal currentAmount, final int termInMonths, final int autoLoanId) {
        if (!this.userDAO.isPassportNumberAvailable(UserPassportNumber)) {
            final AutoLoan autoLoan = new AutoLoan(autoLoanId, amount, currentAmount, termInMonths, UserPassportNumber);
            return this.autoLoanDAO.updateAutoLoan(autoLoan);
        }else {
            return false;
        }
    }

    @Override
    public void deleteAutoLoan(final int autoLoanId) {
         this.autoLoanDAO.deleteAutoLoan(autoLoanId);
    }
}
