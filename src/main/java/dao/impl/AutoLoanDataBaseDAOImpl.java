package dao.impl;

import dao.AutoLoanDAO;
import dto.AutoLoanDTO;
import entity.AutoLoan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;


@Repository
public final class AutoLoanDataBaseDAOImpl implements AutoLoanDAO {

    @PersistenceContext
    private EntityManager entityManager;


    @Contract(pure = true)
    @Override
    public @NotNull @Unmodifiable List<AutoLoanDTO> getAllAutoLoans() {
        final List<AutoLoan> autoLoans = entityManager
                .createQuery("SELECT a FROM AutoLoan a", AutoLoan.class)
                .getResultList();

        return autoLoans.stream()
                .map(this::mapToAutoLoanDTO)
                .toList();
    }


    @Contract(pure = true)
    @Override
    public @Nullable AutoLoanDTO saveAutoLoan(final AutoLoan autoLoan) {
        entityManager.persist(autoLoan);

        return mapToAutoLoanDTO(autoLoan);
    }

    @Contract(pure = true)
    @Override
    public @NotNull Optional<AutoLoanDTO> getAutoLoanById(final int autoLoanId) {
        final AutoLoan autoLoan = entityManager.find(AutoLoan.class, autoLoanId);
        entityManager.close();

        return ofNullable(autoLoan == null ? null : mapToAutoLoanDTO(autoLoan));
    }

    @Override
    public void deleteAutoLoan(final int autoLoanId) {
        final AutoLoan autoLoan = entityManager.find(AutoLoan.class, autoLoanId);
        entityManager.remove(autoLoan);
    }

    @Override
    public boolean updateAutoLoan(final @NotNull AutoLoan autoLoan) {
        final AutoLoan existingAutoLoan = entityManager.find(AutoLoan.class, autoLoan.getId());

        existingAutoLoan.setCreditAmount(autoLoan.getCreditAmount());
        existingAutoLoan.setCurrentCreditAmount(autoLoan.getCurrentCreditAmount());
        existingAutoLoan.setCreditTermInMonths(autoLoan.getCreditTermInMonths());
        existingAutoLoan.setCreditHolder(autoLoan.getCreditHolder());

        return true;
    }

    @Override
    public int getAutoLoanCountByUserPassportNumber(final String holderPassport) {
        return entityManager.createQuery(
                "SELECT COUNT(a) FROM AutoLoan a WHERE a.creditHolder.passportNumber = :passportNumber", Long.class)
                .setParameter("passportNumber", holderPassport)
                .getSingleResult()
                .intValue();
    }

    @Contract("_ -> new")
    private @NotNull AutoLoanDTO mapToAutoLoanDTO(final @NotNull AutoLoan autoLoan) {
        return new AutoLoanDTO(
                autoLoan.getId(),
                autoLoan.getCreditAmount(),
                autoLoan.getCurrentCreditAmount(),
                autoLoan.getCreditTermInMonths(),
                autoLoan.getCreditHolder().getPassportNumber()
        );
    }
}

