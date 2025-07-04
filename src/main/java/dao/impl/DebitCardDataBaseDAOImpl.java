package dao.impl;

import dao.DebitCardDAO;
import dto.DebitCardDTO;
import entity.DebitCard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;


@Repository
public final class DebitCardDataBaseDAOImpl implements DebitCardDAO {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public DebitCardDataBaseDAOImpl(final EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Contract(pure = true)
    @Override
    public @NotNull @Unmodifiable List<DebitCardDTO> getAllDebitCards() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final List<DebitCard> debitCards = entityManager
                .createQuery("SELECT d FROM DebitCard d", DebitCard.class)
                .getResultList();

        entityManager.close();

        return debitCards.stream()
                .map(this::mapToDebitCardDTO)
                .toList();
    }

    @Override
    public boolean isCardNumberAvailable(final String cardNumber) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final Long count = entityManager
                .createQuery("SELECT COUNT(d) FROM DebitCard d WHERE d.cardNumber = :cardNumber", Long.class)
                .setParameter("cardNumber", cardNumber)
                .getSingleResult();

        entityManager.close();

        return count == 0;
    }

    @Override
    public @NotNull DebitCardDTO saveDebitCard(final DebitCard debitCard) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(debitCard);

        transaction.commit();
        entityManager.close();

        return mapToDebitCardDTO(debitCard);
    }

    @Override
    public void updateDebitCard(final DebitCard debitCard) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        final DebitCard existingDebitCard = entityManager.find(DebitCard.class, debitCard.getId());

        existingDebitCard.setCardNumber(debitCard.getCardNumber());
        existingDebitCard.setCvv(debitCard.getCvv());
        existingDebitCard.setBalance(debitCard.getBalance());
        existingDebitCard.setExpirationDate(debitCard.getExpirationDate());
        existingDebitCard.setIssueDate(debitCard.getIssueDate());
        existingDebitCard.setCardHolder(debitCard.getCardHolder());

        transaction.commit();
        entityManager.close();
    }

    @Override
    public void deleteDebitCard(final int id) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        final DebitCard debitCard = entityManager.find(DebitCard.class, id);

        entityManager.remove(debitCard);

        transaction.commit();
        entityManager.close();
    }

    @Override
    public Optional<DebitCardDTO> getDebitCardById(final int id) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final DebitCard debitCard = entityManager.find(DebitCard.class, id);
        entityManager.close();

        return ofNullable( debitCard == null ? null :mapToDebitCardDTO(debitCard));
    }

    @Contract("_ -> new")
    private @NotNull DebitCardDTO mapToDebitCardDTO(final @NotNull DebitCard debitCard) {
        return new DebitCardDTO(
                debitCard.getId(),
                debitCard.getCardNumber(),
                debitCard.getCardHolder().getPassportNumber(),
                debitCard.getExpirationDate(),
                debitCard.getIssueDate(),
                debitCard.getCvv(),
                debitCard.getBalance()
        );
    }
}
