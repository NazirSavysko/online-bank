package service;

import entity.Mortgage;
import entity.enums.Mortgage_Term;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MortgageService {
    List<Mortgage> getAllMortgages();

    Mortgage saveMortgage(String holderPassport, Mortgage_Term mortgageTerm, BigDecimal mortgageAmount, BigDecimal mortgageCurrentAmount);

    Optional<Mortgage> getMortgageById(int mortgageId);

    boolean updateMortgage(String holderPassport, Mortgage_Term mortgageTerm, BigDecimal mortgageAmount, BigDecimal mortgageCurrentAmount, int mortgageId);

    void deleteMortgage(int id);
}
