package com.project.expensemanager.service;

import com.project.expensemanager.entity.Budget;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class BudgetService {
    // Inject repositories
    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    // Business logic
    // Create
    public Budget createBudget(User user, BigDecimal amount, LocalDate budgetPeriod) {
        budgetPeriod = budgetPeriod.withDayOfMonth(1);        // Allow only one monthly budget
        Budget budget = new Budget(user, amount, budgetPeriod);

        return budgetRepository.save(budget);
    }


    // Read

    // Update

    // Delete

    // Get total of monthly expenses

    // Get monthly remaining budget after expenses

}
