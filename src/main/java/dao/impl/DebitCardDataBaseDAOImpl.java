package dao.impl;

import dao.DebitCardDAO;
import dto.DebitCardDTO;
import entity.DebitCard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;


@Repository
public final class DebitCardDataBaseDAOImpl implements DebitCardDAO {

    @PersistenceContext
    private EntityManager entityManager;


    @Contract(pure = true)
    @Override
    public @NotNull @Unmodifiable List<DebitCardDTO> getAllDebitCards() {
        final List<DebitCard> debitCards = entityManager
                .createQuery("SELECT d FROM DebitCard d", DebitCard.class)
                .getResultList();

        return debitCards.stream()
                .map(this::mapToDebitCardDTO)
                .toList();
    }

    @Override
    public boolean isCardNumberAvailable(final String cardNumber) {
        final Long count = entityManager
                .createQuery("SELECT COUNT(d) FROM DebitCard d WHERE d.cardNumber = :cardNumber", Long.class)
                .setParameter("cardNumber", cardNumber)
                .getSingleResult();

        return count == 0;
    }

    @Override
    public @NotNull DebitCardDTO saveDebitCard(final DebitCard debitCard) {
        entityManager.persist(debitCard);

        return mapToDebitCardDTO(debitCard);
    }

    @Override
    public void updateDebitCard(final @NotNull DebitCard debitCard) {
        final DebitCard existingDebitCard = entityManager.find(DebitCard.class, debitCard.getId());

        existingDebitCard.setCardNumber(debitCard.getCardNumber());
        existingDebitCard.setCvv(debitCard.getCvv());
        existingDebitCard.setBalance(debitCard.getBalance());
        existingDebitCard.setExpirationDate(debitCard.getExpirationDate());
        existingDebitCard.setIssueDate(debitCard.getIssueDate());
        existingDebitCard.setCardHolder(debitCard.getCardHolder());
    }

    @Override
    public void deleteDebitCard(final int id) {
        final DebitCard debitCard = entityManager.find(DebitCard.class, id);

        entityManager.remove(debitCard);
    }

    @Override
    public @NotNull Optional<DebitCardDTO> getDebitCardById(final int id) {
        final DebitCard debitCard = entityManager.find(DebitCard.class, id);

        return ofNullable(debitCard == null ? null : mapToDebitCardDTO(debitCard));
    }

    @Override
    public DebitCard getDebitCardByCardNumber(final String CardNumber) {
        return entityManager
                .createQuery("SELECT d FROM DebitCard d WHERE d.cardNumber = :cardNumber", DebitCard.class)
                .setParameter("cardNumber", CardNumber)
                .getSingleResult();
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
