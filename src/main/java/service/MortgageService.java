package service;

import dto.MortgageDTO;
import entity.enums.Mortgage_Term;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MortgageService {
    List<MortgageDTO> getAllMortgages();

    MortgageDTO saveMortgage(String holderPassport, Mortgage_Term mortgageTerm, BigDecimal mortgageAmount, BigDecimal mortgageCurrentAmount);

    Optional<MortgageDTO> getMortgageById(int mortgageId);

    boolean updateMortgage(String holderPassport, Mortgage_Term mortgageTerm, BigDecimal mortgageAmount, BigDecimal mortgageCurrentAmount, int mortgageId);

    void deleteMortgage(int id);
}
