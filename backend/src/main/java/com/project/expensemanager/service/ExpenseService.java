package com.project.expensemanager.service;

import com.project.expensemanager.dto.expense.ExpenseRequest;
import com.project.expensemanager.dto.expense.ExpenseResponse;
import com.project.expensemanager.entity.Category;
import com.project.expensemanager.entity.Expense;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.exception.InvalidRequestException;
import com.project.expensemanager.exception.ResourceNotFoundException;
import com.project.expensemanager.exception.UnauthorizedAccessException;
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
    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    // Read
    public List<Expense> getExpensesForUser(User user) {
        return expenseRepository.findByUser(user);
    }

    public Expense getUserExpenseById(User user, Integer expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new ResourceNotFoundException("Expense not found."));

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

    public List<Expense> getUserExpensesByCategory(User user, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        return expenseRepository.findByUserAndCategory(user, category);
    }

    public List<Expense> getUserExpensesByAmount(User user, BigDecimal amount) {
        return expenseRepository.findByUserAndAmount(user, amount);
    }

    // Search expense by a criteria
    public List<Expense> searchExpenses(
            User user,
            LocalDate expenseDate,
            String title,
            Integer categoryId,
            BigDecimal amount
    ) {
        List<Expense> userExpenses;

        if (title != null) {
            userExpenses = getUserExpensesByTitle(user, title);
        }
        else if (expenseDate != null) {
            userExpenses = getUserExpensesByDate(user, expenseDate);
        }
        else if (amount != null) {
            userExpenses = getUserExpensesByAmount(user, amount);
        }
        else if (categoryId != null) {
            userExpenses = getUserExpensesByCategory(user, categoryId);
        }
        else {
            userExpenses = getExpensesForUser(user);
        }

        return userExpenses;
    }

    // Filter user expenses
   public List<Expense> filterExpenses(User user, Integer categoryId, BigDecimal minAmount, BigDecimal maxAmount, LocalDate start, LocalDate end) {

        if (categoryId != null) {
            Category selectedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found."));

            if (start != null && end != null && minAmount != null && maxAmount != null) {
                validateDate(end, start);
                validateAmount(minAmount, maxAmount);
                return expenseRepository.findByUserAndCategoryAndExpenseDateBetweenAndAmountBetween(user, selectedCategory, start, end, minAmount, maxAmount);
            }

            if (start != null && end != null) {
                validateDate(end, start);
                validateAmount(minAmount, maxAmount);
                return expenseRepository.findByUserAndCategoryAndExpenseDateBetween(user, selectedCategory, start, end);
            }

            if (start != null) {
                return expenseRepository.findByUserAndCategoryAndExpenseDateAfter(user, selectedCategory, start);
            }

            if (end != null) {
                return expenseRepository.findByUserAndCategoryAndExpenseDateBefore(user, selectedCategory, end);
            }

            if ( minAmount != null && maxAmount != null) {
                validateAmount(minAmount, maxAmount);
                return expenseRepository.findByUserAndCategoryAndAmountBetween(user, selectedCategory, minAmount, maxAmount);
            }

            return expenseRepository.findByUserAndCategory(user, selectedCategory);
        }
        else {
            if (start != null && end != null && minAmount != null && maxAmount != null) {
                validateDate(end, start);
                validateAmount(minAmount, maxAmount);
                return expenseRepository.findByUserAndExpenseDateBetweenAndAmountBetween(user, start, end, minAmount, maxAmount);
            }

            if (start != null && end != null) {
                validateDate(end, start);
                return expenseRepository.findByUserAndExpenseDateBetween(user, start, end);
            }

            if (start != null) {
                return expenseRepository.findByUserAndExpenseDateAfter(user, start);
            }

            if (end != null) {
                return expenseRepository.findByUserAndExpenseDateBefore(user, end);
            }

            if (minAmount != null && maxAmount != null) {
                validateAmount(minAmount, maxAmount);
                return expenseRepository.findByUserAndAmountBetween(user, minAmount, maxAmount);
            }
        }

        return getExpensesForUser(user);
    }


    // Update
    public Expense updateExpense(User user, Integer expenseId, Expense updatedExpense) {
        Expense expense = getUserExpenseById(user, expenseId);

        Category category = categoryRepository.findById(expense.getCategory().getId()).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        expense.setTitle(updatedExpense.getTitle());
        expense.setDescription(updatedExpense.getDescription());
        expense.setExpenseDate(updatedExpense.getExpenseDate());
        expense.setAmount(updatedExpense.getAmount());
        expense.setCategory(category);

        return expenseRepository.save(expense);
    }

    // Delete
    public void deleteExpense(User user, Integer expenseId) {
        Expense expense = getUserExpenseById(user, expenseId);
        expenseRepository.delete(expense);
    }

    // Helper methods
    private void validateDate(LocalDate endDate, LocalDate startDate) {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new InvalidRequestException("End date cannot be before start date");
        }
    }

    private void validateAmount(BigDecimal minAmount, BigDecimal maxAmount) {
        if (minAmount != null && maxAmount != null && minAmount.compareTo(maxAmount) > 0) {
            throw new InvalidRequestException("Minimum amount cannot be greater than maximum amount");
        }
    }
}