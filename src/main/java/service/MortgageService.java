package service;

import dto.MortgageDTO;
import entity.enums.Mortgage_Term;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MortgageService {
    List<MortgageDTO> getAllMortgages();

    @Transactional(rollbackFor = Exception.class)
    MortgageDTO saveMortgage(String holderPassport, Mortgage_Term mortgageTerm, BigDecimal mortgageAmount, BigDecimal mortgageCurrentAmount);

    Optional<MortgageDTO> getMortgageById(int mortgageId);

    @Transactional(rollbackFor = Exception.class)
    boolean updateMortgage(String holderPassport, Mortgage_Term mortgageTerm, BigDecimal mortgageAmount, BigDecimal mortgageCurrentAmount, int mortgageId);

    @Transactional(rollbackFor = Exception.class)
    void deleteMortgage(int id);

    @Transactional(rollbackFor = Exception.class)
    void payMortgage( int id, BigDecimal amount);
}
