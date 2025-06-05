package service;

import dao.AutoLoanDAO;
import entity.AutoLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutoLoanListServiceImpl implements AutoLoanService {

    private final AutoLoanDAO autoLoanDAO;

    @Autowired
    public AutoLoanListServiceImpl(AutoLoanDAO autoLoanDAO) {
        this.autoLoanDAO = autoLoanDAO;
    }

    @Override
    public List<AutoLoan> getAllAutoLoans() {
        return this.autoLoanDAO.getAllAutoLoans();
    }
}
