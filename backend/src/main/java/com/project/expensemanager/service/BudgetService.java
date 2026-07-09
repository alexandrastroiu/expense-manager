package com.project.expensemanager.service;

import com.project.expensemanager.entity.Budget;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.repository.BudgetRepository;
import com.project.expensemanager.repository.ExpenseRepository;
import com.project.expensemanager.repository.RecurringExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class BudgetService {
    // Inject repositories
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;

    public BudgetService(BudgetRepository budgetRepository, ExpenseRepository expenseRepository, RecurringExpenseRepository recurringExpenseRepository) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
        this.recurringExpenseRepository = recurringExpenseRepository;
    }

    // Business logic
    // Create
    public Budget createBudget(User user, BigDecimal amount, LocalDate budgetPeriod) {
        budgetPeriod = budgetPeriod.withDayOfMonth(1);        // Allow only one monthly budget
        Budget budget = new Budget(user, amount, budgetPeriod);

        return budgetRepository.save(budget);
    }


    // Read
    public Budget getUserBudgetById(User user, Integer budgetId) {
        return budgetRepository.findByUserAndId(user, budgetId).orElseThrow(() -> new RuntimeException("Budget not found"));
    }

    // Update
    public Budget updateBudget(User user, Integer budgetId, BigDecimal amount, LocalDate budgetPeriod) {
        Budget budget = getUserBudgetById(user, budgetId);
        budgetPeriod = budgetPeriod.withDayOfMonth(1);

        budget.setAmount(amount);
        budget.setBudgetPeriod(budgetPeriod);
        return budgetRepository.save(budget);
    }

    // Delete
    public void deleteBudget(User user, Integer budgetId) {
        Budget budget = getUserBudgetById(user, budgetId);
        budgetRepository.delete(budget);
    }

    // Get total of monthly expenses

    // Get monthly remaining budget after expenses

}
