package dao;

import dto.MortgageDTO;
import entity.Mortgage;

import java.util.List;
import java.util.Optional;

public interface MortgageDAO {
    List<MortgageDTO> getAllMortgages();

    MortgageDTO saveMortgage(Mortgage mortgage);

    Optional<MortgageDTO> getMortgageById(int mortgageId);

    boolean updateMortgage(final Mortgage mortgage);

    void deleteMortgage(int id);
}