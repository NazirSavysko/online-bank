package dao;

import entity.AutoLoan;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.copyOf;

@Repository
public final class AutoLoanListDAOImpl implements AutoLoanDAO {
    private final List<AutoLoan> autoLoans;

    public AutoLoanListDAOImpl() {
        autoLoans = new ArrayList<>();
        autoLoans.add(new AutoLoan(1500000, 1200000, 24, "12345"));
        autoLoans.add(new AutoLoan(2000000, 1500000, 36, "54321"));
        autoLoans.add(new AutoLoan(800000, 600000, 12, "54321"));
    }

    @Override
    public List<AutoLoan> getAllAutoLoans() {
        return copyOf(autoLoans);
    }

    @Override
    public List<AutoLoan> getAutoLoansByPassportId(String passportId) {
        return autoLoans.stream()
                .filter(loan -> loan.getCreditHolderPassportId().equals(passportId))
                .collect(Collectors.toList());
    }
}

