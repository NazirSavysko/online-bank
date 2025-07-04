package dao.impl;

import dao.AutoLoanDAO;
import dto.AutoLoanDTO;
import entity.AutoLoan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;


@Repository
public final class AutoLoanDataBaseDAOImpl implements AutoLoanDAO {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public AutoLoanDataBaseDAOImpl(final EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Contract(pure = true)
    @Override
    public @NotNull @Unmodifiable List<AutoLoanDTO> getAllAutoLoans() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final List<AutoLoan> autoLoans = entityManager.createQuery("SELECT a FROM AutoLoan a", AutoLoan.class)
                .getResultList();
        entityManager.close();
        return autoLoans.stream()
                .map(this::mapToAutoLoanDTO)
                .toList();
    }


    @Contract(pure = true)
    @Override
    public @Nullable AutoLoanDTO saveAutoLoan(final AutoLoan autoLoan) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.persist(autoLoan);

        entityTransaction.commit();
        entityManager.close();

        return mapToAutoLoanDTO(autoLoan);
    }

    @Contract(pure = true)
    @Override
    public @NotNull Optional<AutoLoanDTO> getAutoLoanById(final int autoLoanId) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final AutoLoan autoLoan = entityManager.find(AutoLoan.class, autoLoanId);
        entityManager.close();

        return ofNullable( autoLoan == null ? null :mapToAutoLoanDTO(autoLoan));
    }

    @Override
    public void deleteAutoLoan(final int autoLoanId) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        final AutoLoan autoLoan = entityManager.find(AutoLoan.class, autoLoanId);
        entityManager.remove(autoLoan);

        transaction.commit();
        entityManager.close();
    }

    @Override
    public boolean updateAutoLoan(final @NotNull AutoLoan autoLoan) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        final AutoLoan existingAutoLoan = entityManager.find(AutoLoan.class, autoLoan.getId());

        existingAutoLoan.setCreditAmount(autoLoan.getCreditAmount());
        existingAutoLoan.setCurrentCreditAmount(autoLoan.getCurrentCreditAmount());
        existingAutoLoan.setCreditTermInMonths(autoLoan.getCreditTermInMonths());
        existingAutoLoan.setCreditHolder(autoLoan.getCreditHolder());


        transaction.commit();
        entityManager.close();

        return true;
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

