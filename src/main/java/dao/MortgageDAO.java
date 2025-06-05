package dao;

import entity.Mortgage;

import java.util.List;

public interface MortgageDAO {
    List<Mortgage> getAllMortgages();
    List<Mortgage> getMortgagesByPassportId(String passportId);
}