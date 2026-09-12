package com.project.expensemanager.unit_tests;

import com.project.expensemanager.entity.User;
import com.project.expensemanager.repository.BudgetRepository;
import com.project.expensemanager.repository.ExpenseRepository;
import com.project.expensemanager.repository.RecurringExpenseRepository;
import com.project.expensemanager.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Unit tests for Budget service methods

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

    // Simulate external dependencies using the Mockito framework
    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private RecurringExpenseRepository recurringExpenseRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetService budgetService;

    User user;

    // Setup
    @BeforeEach
    void setUp() {
        user = new User("test_user");
    }

    // Test create method
    @Test
    public void createBudget_ValidBudget_Saved() {

    }

    @Test
    public void createBudget_DuplicateBudget_ThrowsBudgetExistsException() {

    }

    // Test read methods
    @Test
    public void getUserBudgetById_ReturnsCorrectData() {

    }

    @Test
    public void getUserBudgetById_ThrowsResourceNotFoundException() {

    }

    @Test
    public void getUserBudgetByPeriod_ReturnsCorrectData() {

    }

    @Test
    public void getUserBudgetByPeriod_ThrowsResourceNotFoundException() {

    }

    @Test
    public void getTotalCurrentExpenses_CalculatesTotal() {

    }

    // TODO test get monthly expenses

    @Test
    public void getRemainingCurrentBudget_CalculatesTotal() {

    }

    @Test
    public void getRemainingMonthlyBudget_CalculatesTotal() {

    }

    @Test
    public void getBudgetPercentage_CalculatesTotal() {

    }

    // Test update method
    @Test
    public void updateBudget_UpdatesBudget() {

    }

    @Test
    public void updateBudget_WithDuplicateBudget_ThrowsBudgetExistsException() {

    }

    // Test delete method
    @Test
    public void deleteBudget_DeletesBudget() {

    }

    @Test
    public void deleteBudget_ThrowsResourceNotFoundException() {

    }
}