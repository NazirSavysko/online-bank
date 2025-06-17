package service.impl;

import dao.MortgageDAO;
import entity.Mortgage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.MortgageService;

import java.util.List;

@Service
public class MortgageServiceImpl implements MortgageService {

    private final MortgageDAO mortgageDAO;

    @Autowired
    public MortgageServiceImpl(MortgageDAO mortgageDAO) {
        this.mortgageDAO = mortgageDAO;
    }

    @Override
    public List<Mortgage> getAllMortgages() {
        return mortgageDAO.getAllMortgages();
    }
}
