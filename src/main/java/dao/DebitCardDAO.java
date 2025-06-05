package dao;

import entity.DebitCard;

import java.util.List;

public interface DebitCardDAO {
    List<DebitCard> getAllDebitCards();
    List<DebitCard> getDebitCardsByPassportId(String passportId);
}
