package controller;

import dao.MortgageDAO;
import dao.MortgageListDAOImpl;
import entity.Mortgage;
import org.junit.jupiter.api.Test;
import service.MortgageListServiceImpl;
import service.MortgageService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MortgagesControllerTest {

    @Test
    void getAllMortgages() {
        // Given
        final MortgageDAO mortgageDAO = new MortgageListDAOImpl();
        final MortgageService mortgageService = new MortgageListServiceImpl(mortgageDAO);

        // When
        final List<Mortgage> mortgages = mortgageService.getAllMortgages();

        // Then
        assertNotNull(mortgages, "Mortgages list should not be null");
        assertFalse(mortgages.isEmpty(), "Mortgages list should not be empty");
        assertEquals(4, mortgages.size(), "Expected 4 mortgages in the list");
    }
}