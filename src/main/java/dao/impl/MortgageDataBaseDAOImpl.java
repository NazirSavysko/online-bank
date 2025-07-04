package dao.impl;

import dao.MortgageDAO;
import dto.MortgageDTO;
import entity.Mortgage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;


@Repository
public final class MortgageDataBaseDAOImpl implements MortgageDAO {

    private final EntityManagerFactory entityManagerFactory;

    public MortgageDataBaseDAOImpl(final EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }


    @Contract(pure = true)
    @Override
    public @NotNull @Unmodifiable List<MortgageDTO> getAllMortgages() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final List<Mortgage> mortgages = entityManager.createQuery("SELECT m FROM Mortgage m", Mortgage.class)
                .getResultList();
        entityManager.close();

        return mortgages.stream()
                .map(this::mapToMortgageDTO)
                .toList();
    }

    @Override
    public @NotNull MortgageDTO saveMortgage(final Mortgage mortgage) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(mortgage);
        transaction.commit();
        entityManager.close();
        return mapToMortgageDTO(mortgage);
    }

    @Override
    public @NotNull Optional<MortgageDTO> getMortgageById(final int mortgageId) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final Mortgage mortgage = entityManager.find(Mortgage.class, mortgageId);
        entityManager.close();

        return ofNullable( mortgage == null ? null :mapToMortgageDTO(mortgage));
    }

    @Override
    public boolean updateMortgage(final @NotNull Mortgage mortgage) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        final Mortgage existingMortgage = entityManager.find(Mortgage.class, mortgage.getId());

        existingMortgage.setMortgageTerm(mortgage.getMortgageTerm());
        existingMortgage.setMortgageAmount(mortgage.getMortgageAmount());
        existingMortgage.setCurrentMortgageAmount(mortgage.getCurrentMortgageAmount());
        existingMortgage.setMortgageHolder(mortgage.getMortgageHolder());

        transaction.commit();
        entityManager.close();

        return true;
    }

    @Override
    public void deleteMortgage(final int id) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        final Mortgage mortgage = entityManager.find(Mortgage.class, id);
        entityManager.remove(mortgage);

        transaction.commit();
        entityManager.close();
    }


    @Contract("_ -> new")
    private @NotNull MortgageDTO mapToMortgageDTO(final @NotNull Mortgage mortgage) {
        return new MortgageDTO(
                mortgage.getId(),
                mortgage.getMortgageAmount(),
                mortgage.getMortgageHolder().getPassportNumber(),
                mortgage.getCurrentMortgageAmount(),
                mortgage.getMortgageTerm()
        );
    }
}
