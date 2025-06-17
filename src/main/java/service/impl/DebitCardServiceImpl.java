package service.impl;

import dao.DebitCardDAO;
import entity.DebitCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.DebitCardService;

import java.util.List;

@Service
public class DebitCardServiceImpl implements DebitCardService {

    private final DebitCardDAO debitCardDAO;

    @Autowired
    public DebitCardServiceImpl(DebitCardDAO dao) {
        this.debitCardDAO = dao;
    }

    @Override
    public List<DebitCard> getAllDebitCards() {
        return debitCardDAO.getAllDebitCards();
    }
}
