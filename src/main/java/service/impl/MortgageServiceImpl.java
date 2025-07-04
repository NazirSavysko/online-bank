package service.impl;

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
    public List<MortgageDTO> getAllMortgages() {
        return mortgageDAO.getAllMortgages();
    }

    @Override
    public MortgageDTO saveMortgage(final String holderPassport, final Mortgage_Term mortgageTerm, final BigDecimal mortgageAmount, final BigDecimal mortgageCurrentAmount) {
        if (!userDAO.isPassportNumberAvailable(holderPassport)) {
            final User user = userDAO.getUserByPassportNumber(holderPassport);
            final Mortgage mortgage = new Mortgage(0, mortgageAmount, user,mortgageCurrentAmount, mortgageTerm);
            return mortgageDAO.saveMortgage(mortgage);
        }else {
            throw new IllegalArgumentException("UserDTO with passport number '%s' does not exist".formatted(holderPassport));
        }
    }

    @Override
    public Optional<MortgageDTO> getMortgageById(final int mortgageId) {
        return mortgageDAO.getMortgageById(mortgageId);
    }

    @Override
    public boolean updateMortgage(final String holderPassport, final Mortgage_Term mortgageTerm, final BigDecimal mortgageAmount, final BigDecimal mortgageCurrentAmount, final int mortgageId) {
        if(!userDAO.isPassportNumberAvailable(holderPassport)) {
            final User user = userDAO.getUserByPassportNumber(holderPassport);
            final Mortgage mortgage = new Mortgage(mortgageId, mortgageAmount, user,mortgageCurrentAmount, mortgageTerm);
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
