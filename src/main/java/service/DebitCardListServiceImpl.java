package service;

import dao.DebitCardDAO;
import entity.DebitCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebitCardListServiceImpl implements DebitCardService {

    private final DebitCardDAO debitCardDAO;

    @Autowired
    public DebitCardListServiceImpl(DebitCardDAO dao) {
        this.debitCardDAO = dao;
    }

    @Override
    public List<DebitCard> getAllDebitCards() {
        return debitCardDAO.getAllDebitCards();
    }
}
