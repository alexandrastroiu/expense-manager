package com.project.expensemanager.service;

import com.project.expensemanager.entity.Budget;
import com.project.expensemanager.entity.Expense;
import com.project.expensemanager.entity.RecurringExpense;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.repository.BudgetRepository;
import com.project.expensemanager.repository.ExpenseRepository;
import com.project.expensemanager.repository.RecurringExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    public Budget getUserBudgetByPeriod(User user, LocalDate period) {
        return budgetRepository.findByUserAndPeriod(user, period).orElseThrow(() -> new RuntimeException("Budget not found"));
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

    // Get total of current expenses
    public BigDecimal getTotalCurrentExpenses(User user, LocalDate period) {
        LocalDate startDate = period.withDayOfMonth(1);
        LocalDate endDate = period.withDayOfMonth(period.lengthOfMonth());
        List<Expense> currentExpenses = expenseRepository.findByUserAndExpenseDateBetween(user, startDate, endDate);
        BigDecimal total = new BigDecimal("0");

        for (Expense e : currentExpenses) {
            total = total.add(e.getAmount());
        }

        return total;
    }

    // Get total of monthly expenses
    public BigDecimal getTotalMonthlyExpenses(User user, LocalDate period) {
       LocalDate start = period.withDayOfMonth(1);
       LocalDate end = period.withDayOfMonth(period.lengthOfMonth());
       BigDecimal total = new BigDecimal("0");
       BigDecimal currentExpenses = getTotalCurrentExpenses(user, period);
       total = total.add(currentExpenses);
       List<RecurringExpense> recurringExpenses = recurringExpenseRepository.findByUser(user);

       for (RecurringExpense r : recurringExpenses) {
           //TODO
       }

       return total;
    }

    // Get current remaining budget after expenses
    public BigDecimal getRemainingCurrentBudget(User user, LocalDate period) {
        return (getUserBudgetByPeriod(user, period).getAmount()).subtract(getTotalCurrentExpenses(user, period));
    }

    // Get monthly remaining budget after expenses
    public BigDecimal getRemainingMonthlyBudget(User user, LocalDate period) {
        return ((getUserBudgetByPeriod(user, period).getAmount()).subtract(getTotalMonthlyExpenses(user, period)));
    }

    // Get percentage of budget usage per month
    // TODO

}
