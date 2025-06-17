package controller;

import dao.AutoLoanDAO;
import dao.impl.AutoLoanDataBaseDAOImpl;
import entity.AutoLoan;
import org.junit.jupiter.api.Test;
import service.impl.AutoLoanServiceImpl;
import service.AutoLoanService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class AutoLoansControllerTest {



    @Test
    void getAutoLoansPage() {
        //Given
        final AutoLoanDAO autoLoanDAO = new AutoLoanDataBaseDAOImpl();
        final AutoLoanService autoLoanService = new AutoLoanServiceImpl(autoLoanDAO);

        // When
        final List<AutoLoan> autoLoans = autoLoanService.getAllAutoLoans();

        // Then
        assertNotNull(autoLoans);
        assertFalse(autoLoans.isEmpty(), "Auto loans list should not be empty");
        assertEquals(3,autoLoans.size());
    }

}