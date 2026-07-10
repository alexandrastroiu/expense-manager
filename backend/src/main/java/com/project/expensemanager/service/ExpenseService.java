package com.project.expensemanager.service;

import com.project.expensemanager.dto.expense.ExpenseResponse;
import com.project.expensemanager.entity.Category;
import com.project.expensemanager.entity.Expense;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.repository.CategoryRepository;
import com.project.expensemanager.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    // Inject repositories
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    // Business logic
    // Create
    public Expense createExpense(User user, String title, String description, BigDecimal amount, Integer categoryId, LocalDate expenseDate) {
        Category selectedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found."));

        Expense newExpense = new Expense(user, title, description, amount, selectedCategory, expenseDate);

        return expenseRepository.save(newExpense);
    }

    // Read
    public List<Expense> getExpensesForUser(User user) {
        return expenseRepository.findByUser(user);
    }

    public Expense getUserExpenseById(User user, Integer expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new RuntimeException("Expense not found."));

        if (expense.getUser().getId().equals(user.getId())) {
            return expense;
        }
        throw new RuntimeException("Cannot access this expense.");
    }

    public List<Expense> getUserExpensesByDate(User user, LocalDate expenseDate) {
        return expenseRepository.findByUserAndExpenseDate(user, expenseDate);
    }

    public List<Expense> getUserExpensesByTitle(User user, String title) {
        return expenseRepository.findByUserAndTitle(user, title);
    }

    public List<Expense> getUserExpenseByCategory(User user, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found."));

        return expenseRepository.findByUserAndCategory(user, category);
    }

    public List<Expense> getUserExpenseByAmount(User user, BigDecimal amount) {
        return expenseRepository.findByUserAndAmount(user, amount);
    }

    // Filter user expenses
   public List<Expense> filterExpenses(User user, Integer categoryId, BigDecimal minAmount, BigDecimal maxAmount, LocalDate start, LocalDate end) {

        if (categoryId != null) {
            Category selectedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found."));

            if (start != null && end != null && minAmount != null && maxAmount != null) {
                return expenseRepository.findByUserAndCategoryAndExpenseDateBetweenAndAmountBetween(user, selectedCategory, start, end, minAmount, maxAmount);
            }

            if (start != null && end != null) {
                return expenseRepository.findByUserAndCategoryAndExpenseDateBetween(user, selectedCategory, start, end);
            }

            if (start != null) {
                return expenseRepository.findByUserAndCategoryAndExpenseDateAfter(user, selectedCategory, start);
            }

            if (end != null) {
                return expenseRepository.findByUserAndCategoryAndExpenseDateBefore(user, selectedCategory, end);
            }

            if ( minAmount != null && maxAmount != null) {
                return expenseRepository.findByUserAndCategoryAndAmountBetween(user, selectedCategory, minAmount, maxAmount);
            }

            return expenseRepository.findByUserAndCategory(user, selectedCategory);
        }
        else {
            if (start != null && end != null && minAmount != null && maxAmount != null) {
                return expenseRepository.findByUserAndExpenseDateBetweenAndAmountBetween(user, start, end, minAmount, maxAmount);
            }

            if (start != null && end != null) {
                return expenseRepository.findByUserAndExpenseDateBetween(user, start, end);
            }

            if (start != null) {
                return expenseRepository.findByUserAndExpenseDateAfter(user, start);
            }

            if (end != null) {
                return expenseRepository.findByUserAndExpenseDateBefore(user, end);
            }

            if (minAmount != null && maxAmount != null) {
                return expenseRepository.findByUserAndAmountBetween(user, minAmount, maxAmount);
            }
        }

        return getExpensesForUser(user);
    }


    // Update
    public Expense updateExpense(Integer expenseId, User user, String title, String description, BigDecimal amount, Integer categoryId, LocalDate expenseDate) {
        Expense expense = getUserExpenseById(user, expenseId);

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));

        expense.setTitle(title);
        expense.setDescription(description);
        expense.setExpenseDate(expenseDate);
        expense.setAmount(amount);
        expense.setCategory(category);

        return expenseRepository.save(expense);
    }

    // Delete
    public void deleteExpense(User user, Integer expenseId) {
        Expense expense = getUserExpenseById(user, expenseId);
        expenseRepository.delete(expense);
    }

    // Map entity to response
    private ExpenseResponse mapToResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getTitle(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory().getId(),
                expense.getExpenseDate()
                );
    }

    // Map request to entity
}
