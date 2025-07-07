package service.impl;

import dao.AutoLoanDAO;
import dao.MortgageDAO;
import dao.UserDAO;
import dto.MortgageDTO;
import entity.Mortgage;
import entity.User;
import entity.enums.Mortgage_Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.MortgageService;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MortgageServiceImpl implements MortgageService {

    private final MortgageDAO mortgageDAO;
    private final UserDAO userDAO;
    private final AutoLoanDAO autoLoanDAO;

    @Autowired
    public MortgageServiceImpl(final MortgageDAO mortgageDAO, final UserDAO userDAO,
                               final AutoLoanDAO autoLoanDAO) {
        this.mortgageDAO = mortgageDAO;
        this.userDAO = userDAO;
        this.autoLoanDAO = autoLoanDAO;
    }

    @Override
    public List<MortgageDTO> getAllMortgages() {
        return mortgageDAO.getAllMortgages();
    }

    @Override
    public MortgageDTO saveMortgage(final String holderPassport, final Mortgage_Term mortgageTerm, final BigDecimal mortgageAmount, final BigDecimal mortgageCurrentAmount) {
        if (!userDAO.isPassportNumberAvailable(holderPassport)) {
            final int mortgageCount = this.mortgageDAO.getMortgageCountByUserPassportNumber(holderPassport);
            final int autoLoanCount = this.autoLoanDAO.getAutoLoanCountByUserPassportNumber(holderPassport);

            if ((mortgageCount + autoLoanCount) < 3) {
                final User user = userDAO.getUserByPassportNumber(holderPassport);
                final Mortgage mortgage = new Mortgage(0, mortgageAmount, user, mortgageCurrentAmount, mortgageTerm);

                return mortgageDAO.saveMortgage(mortgage);
            } else {
                throw new IllegalArgumentException("UserDTO with passport number '%s' has enough mortgages and auto loans".formatted(holderPassport));
            }
        } else {
            throw new IllegalArgumentException("UserDTO with passport number '%s' does not exist".formatted(holderPassport));
        }
    }

    @Override
    public Optional<MortgageDTO> getMortgageById(final int mortgageId) {
        return mortgageDAO.getMortgageById(mortgageId);
    }

    @Override
    public boolean updateMortgage(final String holderPassport, final Mortgage_Term mortgageTerm, final BigDecimal mortgageAmount, final BigDecimal mortgageCurrentAmount, final int mortgageId) {
        if (!userDAO.isPassportNumberAvailable(holderPassport)) {
            final User user = userDAO.getUserByPassportNumber(holderPassport);
            final Mortgage mortgage = new Mortgage(mortgageId, mortgageAmount, user, mortgageCurrentAmount, mortgageTerm);

            return mortgageDAO.updateMortgage(mortgage);
        } else {
            return false;
        }
    }

    @Override
    public void deleteMortgage(final int id) {
        mortgageDAO.deleteMortgage(id);
    }

    @Override
    public void payMortgage(final int id, final BigDecimal amount) {
        final Optional<MortgageDTO> mortgageOpt = mortgageDAO.getMortgageById(id);
        if (mortgageOpt.isPresent()) {
            final MortgageDTO mortgage = mortgageOpt.get();
            final BigDecimal currentAmount = mortgage.currentMortgageAmount().subtract(amount);

            if (currentAmount.compareTo(BigDecimal.ZERO) <= 0) {
                this.mortgageDAO.deleteMortgage(id);
            } else {
                final User user = this.userDAO.getUserByPassportNumber(mortgage.mortgageHolderPassportNumber());
                final Mortgage newMortgage = new Mortgage(
                        mortgage.id(),
                        mortgage.mortgageAmount(),
                        user,
                        currentAmount,
                        mortgage.mortgageTerm()
                );

                this.mortgageDAO.updateMortgage(newMortgage);
            }
        } else {
            throw new NoSuchElementException("Mortgage with ID " + id + " not found.");
        }
    }
}
