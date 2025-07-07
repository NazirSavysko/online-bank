package service.impl;

import dao.AutoLoanDAO;
import dao.MortgageDAO;
import dao.UserDAO;
import dto.AutoLoanDTO;
import entity.AutoLoan;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.AutoLoanService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;

@Service
public class AutoLoanServiceImpl implements AutoLoanService {

    private final AutoLoanDAO autoLoanDAO;
    private final UserDAO userDAO;
    private final MortgageDAO mortgageDAO;


    @Autowired
    public AutoLoanServiceImpl(final AutoLoanDAO autoLoanDAO, final UserDAO userDAO,
                               final MortgageDAO mortgageDAO) {
        this.autoLoanDAO = autoLoanDAO;
        this.userDAO = userDAO;
        this.mortgageDAO = mortgageDAO;
    }

    @Override
    public List<AutoLoanDTO> getAllAutoLoans() {
        return this.autoLoanDAO.getAllAutoLoans();
    }

    @Override
    public AutoLoanDTO saveAutoLoan(final String userPassportNumber,
                                    final BigDecimal amount,
                                    final BigDecimal currentAmount,
                                    final int termInMonths) {
        if (!this.userDAO.isPassportNumberAvailable(userPassportNumber)) {
            final int mortgageCount = this.mortgageDAO.getMortgageCountByUserPassportNumber(userPassportNumber);
            final int autoLoanCount = this.autoLoanDAO.getAutoLoanCountByUserPassportNumber(userPassportNumber);

            if ((mortgageCount + autoLoanCount) < 3) {
                final User user = this.userDAO.getUserByPassportNumber(userPassportNumber);
                final AutoLoan autoLoan = new AutoLoan(0, amount, currentAmount, termInMonths, user);

                return this.autoLoanDAO.saveAutoLoan(autoLoan);
            } else {
                throw new IllegalArgumentException("User with passport number '%s' has enough mortgages and auto loans".formatted(userPassportNumber));
            }
        } else {
            throw new IllegalArgumentException("UserDTO with passport number '%s' does not exist".formatted(userPassportNumber));
        }
    }

    @Override
    public Optional<AutoLoanDTO> getAutoLoanById(final int autoLoanId) {
        return this.autoLoanDAO.getAutoLoanById(autoLoanId);
    }

    @Override
    public boolean updateAutoLoan(final String userPassportNumber,
                                  final BigDecimal amount,
                                  final BigDecimal currentAmount,
                                  final int termInMonths,
                                  final int autoLoanId) {
        if (!this.userDAO.isPassportNumberAvailable(userPassportNumber)) {
            final User user = this.userDAO.getUserByPassportNumber(userPassportNumber);
            final AutoLoan autoLoan = new AutoLoan(autoLoanId, amount, currentAmount, termInMonths, user);

            return this.autoLoanDAO.updateAutoLoan(autoLoan);
        } else {
            return false;
        }
    }

    @Override
    public void deleteAutoLoan(final int autoLoanId) {
        this.autoLoanDAO.deleteAutoLoan(autoLoanId);
    }

    @Override
    public void payAutoLoan(final int id, final BigDecimal amount) {
        final Optional<AutoLoanDTO> autoLoan = this.autoLoanDAO.getAutoLoanById(id);
        if (autoLoan.isPresent()) {
            final AutoLoanDTO autoLoanDTO = autoLoan.get();
            final BigDecimal currentAmount = autoLoanDTO.currentCreditAmount().subtract(amount);

            if (currentAmount.compareTo(ZERO) <= 0) {
                this.autoLoanDAO.deleteAutoLoan(id);
            } else {
                final User user = this.userDAO.getUserByPassportNumber(autoLoanDTO.creditHolderPassportNumber());
                final AutoLoan autoLoanUpdate = new AutoLoan(
                        autoLoanDTO.id(),
                        autoLoanDTO.creditAmount(),
                        currentAmount,
                        autoLoanDTO.creditTermInMonths(),
                        user
                );

                this.autoLoanDAO.updateAutoLoan(autoLoanUpdate);
            }
        } else {
            throw new IllegalArgumentException("Auto loan with id '%d' does not exist".formatted(id));
        }
    }
}
