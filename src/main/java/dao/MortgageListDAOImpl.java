package dao;

import entity.DebitCard;
import entity.Mortgage;
import entity.enums.Mortgage_Term;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.copyOf;

@Repository
public final class MortgageListDAOImpl implements MortgageDAO {
    private final List<Mortgage> mortgages;

    public MortgageListDAOImpl() {
        mortgages = new ArrayList<>();
        mortgages.add(
                new Mortgage(
                        Mortgage_Term.FIFTEEN_YEARS,
                        8000000,
                        "12345",
                        10000000));
        mortgages.add(
                new Mortgage(
                        Mortgage_Term.TWENTY_YEARS,
                        6000000,
                        "12345",
                        7500000));
        mortgages.add(
                new Mortgage(
                        Mortgage_Term.TEN_YEARS,
                        3000000,
                        "54321",
                        5000000));
        mortgages.add(
                new Mortgage(
                        Mortgage_Term.TWENTY_YEARS,
                        12000000,
                        "54321",
                        15000000));
    }


    @Override
    public List<Mortgage> getAllMortgages() {
        return copyOf(mortgages);
    }

    @Override
    public List<Mortgage> getMortgagesByPassportId(String passportId) {
        return mortgages.stream()
                .filter(mortgage -> mortgage.getMortgageHolderPassportId().equals(passportId))
                .collect(Collectors.toList());
    }
}
