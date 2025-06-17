package controller;

import dao.DebitCardDAO;
import dao.impl.DebitCardDataBaseDAOImpl;
import entity.DebitCard;
import org.junit.jupiter.api.Test;
import service.impl.DebitCardServiceImpl;
import service.DebitCardService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DebitCardsControllerTest {

    @Test
    void getDebitCardsPage() {
//        //Given
//        final DebitCardDAO debitCardDAO = new DebitCardDataBaseDAOImpl();
//        final DebitCardService debitCardService = new DebitCardServiceImpl(debitCardDAO);
//
//        // When
//        final List<DebitCard> debitCards = debitCardService.getAllDebitCards();
//
//        // Then
//        assertNotNull(debitCards, "Debit cards list should not be null");
//        assertFalse(debitCards.isEmpty(), "Debit cards list should not be empty");
//        assertEquals(4, debitCards.size(), "There should be 4 debit cards in the list");
    }
}