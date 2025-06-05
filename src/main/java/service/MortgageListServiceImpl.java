package service;

import dao.MortgageDAO;
import entity.Mortgage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MortgageListServiceImpl implements MortgageService {

    private final MortgageDAO mortgageDAO;

    @Autowired
    public MortgageListServiceImpl(MortgageDAO mortgageDAO) {
        this.mortgageDAO = mortgageDAO;
    }

    @Override
    public List<Mortgage> getAllMortgages() {
        return mortgageDAO.getAllMortgages();
    }
}
