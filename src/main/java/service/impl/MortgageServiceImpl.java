package service.impl;

import dao.MortgageDAO;
import dao.UserDAO;
import entity.Mortgage;
import entity.enums.Mortgage_Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.MortgageService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MortgageServiceImpl implements MortgageService {

    private final MortgageDAO mortgageDAO;
    private final UserDAO userDAO;

    @Autowired
    public MortgageServiceImpl(final MortgageDAO mortgageDAO,final UserDAO userDAO) {
        this.mortgageDAO = mortgageDAO;
        this.userDAO = userDAO;
    }

    @Override
    public List<Mortgage> getAllMortgages() {
        return mortgageDAO.getAllMortgages();
    }

    @Override
    public Mortgage saveMortgage(final String holderPassport, final Mortgage_Term mortgageTerm, final BigDecimal mortgageAmount, final BigDecimal mortgageCurrentAmount) {
        if (!userDAO.isPassportNumberAvailable(holderPassport)) {
            final Mortgage mortgage = new Mortgage(0, mortgageAmount, holderPassport,mortgageCurrentAmount, mortgageTerm);
            return mortgageDAO.saveMortgage(mortgage);
        }else {
            throw new IllegalArgumentException("User with passport number '%s' does not exist".formatted(holderPassport));
        }
    }

    @Override
    public Optional<Mortgage> getMortgageById(final int mortgageId) {
        return mortgageDAO.getMortgageById(mortgageId);
    }

    @Override
    public boolean updateMortgage(final String holderPassport, final Mortgage_Term mortgageTerm, final BigDecimal mortgageAmount, final BigDecimal mortgageCurrentAmount, final int mortgageId) {
        if(!userDAO.isPassportNumberAvailable(holderPassport)) {
            final Mortgage mortgage = new Mortgage(mortgageId, mortgageAmount, holderPassport, mortgageCurrentAmount, mortgageTerm);
            return mortgageDAO.updateMortgage(mortgage);
        }else {
            return false;
        }
    }

    @Override
    public void deleteMortgage(final int id) {
         mortgageDAO.deleteMortgage(id);
    }
}
