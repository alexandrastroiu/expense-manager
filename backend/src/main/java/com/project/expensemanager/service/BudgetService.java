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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
       LocalDate monthStart = period.withDayOfMonth(1);
       LocalDate monthEnd = period.withDayOfMonth(period.lengthOfMonth());
       BigDecimal total = new BigDecimal("0");
       BigDecimal currentExpenses = getTotalCurrentExpenses(user, period);
       total = total.add(currentExpenses);
       List<RecurringExpense> recurringExpenses = recurringExpenseRepository.findByUser(user);

       for (RecurringExpense r : recurringExpenses) {
           //TODO
           LocalDate rStart = r.getStartDate();
           LocalDate rEnd = r.getEndDate();
           long activeDays;
           boolean startsBeforeMonthEnd = rStart.isBefore(monthEnd) || rStart.isEqual(monthEnd);
           boolean endsAfterMonthStart = rEnd == null || rEnd.isAfter(monthStart) || rEnd.isEqual(monthStart);

           if (startsBeforeMonthEnd && endsAfterMonthStart) {
               switch (r.getFrequency()) {
                   case DAILY:
                       LocalDate recurringStart = rStart.isAfter(monthStart) ? rStart : monthStart;
                       LocalDate recurringEnd = rEnd == null || rEnd.isAfter(monthEnd) ? monthEnd : rEnd;

                       activeDays = ChronoUnit.DAYS.between(recurringStart, recurringEnd) + 1;

                       total = total.add(r.getAmount().multiply(BigDecimal.valueOf(activeDays)));
                       break;
                   case WEEKLY:
                       //TODO
                       break;
                   case MONTHLY:
                           total = total.add(r.getAmount());
                       break;
                   case YEARLY:
                       if (period.getMonthValue() == rStart.getMonthValue()) {
                           total = total.add(r.getAmount());
                       }
                       break;
               }
           }
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
    public BigDecimal getBudgetPercentage(User user, LocalDate period) {
        BigDecimal budget = getUserBudgetByPeriod(user, period).getAmount();
        BigDecimal expenses = getTotalMonthlyExpenses(user, period);

        if (budget.compareTo(BigDecimal.ZERO) == 0) {   // Handle edge case
            return BigDecimal.ZERO;
        }

        return expenses.multiply(BigDecimal.valueOf(100)).divide(budget, RoundingMode.HALF_UP);
    }
}