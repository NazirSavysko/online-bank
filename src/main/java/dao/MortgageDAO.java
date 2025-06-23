package dao;

import entity.Mortgage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MortgageDAO {
    List<Mortgage> getAllMortgages();

    Mortgage saveMortgage(Mortgage mortgage);

    Optional<Mortgage> getMortgageById(int mortgageId);

    boolean updateMortgage(final Mortgage mortgage);

    void deleteMortgage(int id);
}