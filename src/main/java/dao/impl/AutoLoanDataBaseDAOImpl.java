package dao.impl;

import dao.AutoLoanDAO;
import entity.AutoLoan;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public final class AutoLoanDataBaseDAOImpl implements AutoLoanDAO {

    public AutoLoanDataBaseDAOImpl() {
    }

    @Override
    public List<AutoLoan> getAllAutoLoans() {
        return null;
    }

}

