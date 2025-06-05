package controller;

import dao.DebitCardDAO;
import dao.DebitCardListDAOImpl;
import entity.DebitCard;
import org.junit.jupiter.api.Test;
import service.DebitCardListServiceImpl;
import service.DebitCardService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DebitCardsControllerTest {

    @Test
    void getDebitCardsPage() {
        //Given
        final DebitCardDAO debitCardDAO = new DebitCardListDAOImpl();
        final DebitCardService debitCardService = new DebitCardListServiceImpl(debitCardDAO);

        // When
        final List<DebitCard> debitCards = debitCardService.getAllDebitCards();

        // Then
        assertNotNull(debitCards, "Debit cards list should not be null");
        assertFalse(debitCards.isEmpty(), "Debit cards list should not be empty");
        assertEquals(4, debitCards.size(), "There should be 4 debit cards in the list");
    }
}