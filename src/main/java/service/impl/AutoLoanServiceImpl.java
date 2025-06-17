package service.impl;

import dao.AutoLoanDAO;
import entity.AutoLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.AutoLoanService;

import java.util.List;

@Service
public class AutoLoanServiceImpl implements AutoLoanService {

    private final AutoLoanDAO autoLoanDAO;

    @Autowired
    public AutoLoanServiceImpl(AutoLoanDAO autoLoanDAO) {
        this.autoLoanDAO = autoLoanDAO;
    }

    @Override
    public List<AutoLoan> getAllAutoLoans() {
        return this.autoLoanDAO.getAllAutoLoans();
    }
}
