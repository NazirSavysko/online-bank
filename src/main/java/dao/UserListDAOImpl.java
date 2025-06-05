package dao;

import entity.AutoLoan;
import entity.DebitCard;
import entity.Mortgage;
import entity.User;
import entity.enums.Gender;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.List.copyOf;

@Repository
public final class UserListDAOImpl implements UserDAO {
    private final List<User> users;

    private final DebitCardDAO debitCardDAO;
    private final MortgageDAO mortgageDAO;
    private final AutoLoanDAO autoLoanDAO;

    @Autowired
    public UserListDAOImpl(DebitCardDAO debitCardDAO, MortgageDAO mortgageDAO, AutoLoanDAO autoLoanDAO) {
        this.debitCardDAO = debitCardDAO;
        this.mortgageDAO = mortgageDAO;
        this.autoLoanDAO = autoLoanDAO;
        users = new ArrayList<>();
        User user1 = new User(
                "John Smith",
                Gender.MALE,
                LocalDate.of(1990, 1, 15),
                "12345",
                debitCardDAO.getDebitCardsByPassportId("12345"),
                mortgageDAO.getMortgagesByPassportId("12345"),
                autoLoanDAO.getAutoLoansByPassportId("12345")
        );

        User user2 = new User(
                "Anna Johnson",
                Gender.FEMALE,
                LocalDate.of(1985, 5, 22),
                "54321",
                debitCardDAO.getDebitCardsByPassportId("54321"),
                mortgageDAO.getMortgagesByPassportId("54321"),
                autoLoanDAO.getAutoLoansByPassportId("54321")
        );

        users.add(user1);
        users.add(user2);
    }

    @Override
    public List<User> getAllUsers() {
        return copyOf(users);
    }

}
