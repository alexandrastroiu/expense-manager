package com.project.expensemanager.service;

import com.project.expensemanager.dto.expense.ExpenseRequest;
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
    public ExpenseResponse createExpense(User user, ExpenseRequest request) {
        Category selectedCategory = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new RuntimeException("Category not found."));

        Expense expense = mapToEntity(request, user, selectedCategory);

        Expense newExpense = expenseRepository.save(expense);

        return mapToResponse(newExpense);
    }

    // Read
    public List<ExpenseResponse> getExpensesForUser(User user) {
        List<Expense> userExpenses = expenseRepository.findByUser(user);

        return userExpenses.stream().map(this::mapToResponse).toList();
    }

    public ExpenseResponse getUserExpenseById(User user, Integer expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new RuntimeException("Expense not found."));

        if (expense.getUser().getId().equals(user.getId())) {
            return mapToResponse(expense);
        }
        throw new RuntimeException("Cannot access this expense.");
    }

    public List<ExpenseResponse> getUserExpensesByDate(User user, LocalDate expenseDate) {
        List<Expense> userExpenses =  expenseRepository.findByUserAndExpenseDate(user, expenseDate);

        return userExpenses.stream().map(this::mapToResponse).toList();
    }

    public List<ExpenseResponse> getUserExpensesByTitle(User user, String title) {
        List<Expense> userExpenses =  expenseRepository.findByUserAndTitle(user, title);

        return userExpenses.stream().map(this::mapToResponse).toList();
    }

    public List<ExpenseResponse> getUserExpensesByCategory(User user, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found."));

        List<Expense> userExpenses = expenseRepository.findByUserAndCategory(user, category);

        return userExpenses.stream().map(this::mapToResponse).toList();
    }

    public List<ExpenseResponse> getUserExpensesByAmount(User user, BigDecimal amount) {
        List<Expense> userExpenses = expenseRepository.findByUserAndAmount(user, amount);

        return userExpenses.stream().map(this::mapToResponse).toList();
    }

    // Search expense by a criteria
    public List<ExpenseResponse> searchExpenses(
            User user,
            LocalDate expenseDate,
            String title,
            Integer categoryId,
            BigDecimal amount
    ) {
        List<ExpenseResponse> userExpenses;

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
   public List<ExpenseResponse> filterExpenses(User user, Integer categoryId, BigDecimal minAmount, BigDecimal maxAmount, LocalDate start, LocalDate end) {

        if (categoryId != null) {
            Category selectedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found."));

            if (start != null && end != null && minAmount != null && maxAmount != null) {
                return expenseRepository.findByUserAndCategoryAndExpenseDateBetweenAndAmountBetween(user, selectedCategory, start, end, minAmount, maxAmount).stream().map(this::mapToResponse).toList();
            }

            if (start != null && end != null) {
                return expenseRepository.findByUserAndCategoryAndExpenseDateBetween(user, selectedCategory, start, end).stream().map(this::mapToResponse).toList();
            }

            if (start != null) {
                return expenseRepository.findByUserAndCategoryAndExpenseDateAfter(user, selectedCategory, start).stream().map(this::mapToResponse).toList();
            }

            if (end != null) {
                return expenseRepository.findByUserAndCategoryAndExpenseDateBefore(user, selectedCategory, end).stream().map(this::mapToResponse).toList();
            }

            if ( minAmount != null && maxAmount != null) {
                return expenseRepository.findByUserAndCategoryAndAmountBetween(user, selectedCategory, minAmount, maxAmount).stream().map(this::mapToResponse).toList();
            }

            return expenseRepository.findByUserAndCategory(user, selectedCategory).stream().map(this::mapToResponse).toList();
        }
        else {
            if (start != null && end != null && minAmount != null && maxAmount != null) {
                return expenseRepository.findByUserAndExpenseDateBetweenAndAmountBetween(user, start, end, minAmount, maxAmount).stream().map(this::mapToResponse).toList();
            }

            if (start != null && end != null) {
                return expenseRepository.findByUserAndExpenseDateBetween(user, start, end).stream().map(this::mapToResponse).toList();
            }

            if (start != null) {
                return expenseRepository.findByUserAndExpenseDateAfter(user, start).stream().map(this::mapToResponse).toList();
            }

            if (end != null) {
                return expenseRepository.findByUserAndExpenseDateBefore(user, end).stream().map(this::mapToResponse).toList();
            }

            if (minAmount != null && maxAmount != null) {
                return expenseRepository.findByUserAndAmountBetween(user, minAmount, maxAmount).stream().map(this::mapToResponse).toList();
            }
        }

        return getExpensesForUser(user);
    }


    // Update
    public ExpenseResponse updateExpense(Integer expenseId, User user, ExpenseRequest request) {
        Expense expense = getUserExpenseEntityById(user, expenseId);

        Category category = categoryRepository.findById(expense.getCategory().getId()).orElseThrow(() -> new RuntimeException("Category not found"));

        expense.setTitle(request.title());
        expense.setDescription(request.description());
        expense.setExpenseDate(request.expenseDate());
        expense.setAmount(request.amount());
        expense.setCategory(category);

        Expense updatedExpense = expenseRepository.save(expense);

        return mapToResponse(updatedExpense);
    }

    // Delete
    public void deleteExpense(User user, Integer expenseId) {
        Expense expense = getUserExpenseEntityById(user, expenseId);
        expenseRepository.delete(expense);
    }

    //Helper methods
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
    private Expense mapToEntity(ExpenseRequest request, User user, Category category) {
        return new Expense(
                user,
                request.title(),
                request.description(),
                request.amount(),
                category,
                request.expenseDate()
                );
    }

    private Expense getUserExpenseEntityById(User user, Integer expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new RuntimeException("Expense not found."));

        if (expense.getUser().getId().equals(user.getId())) {
            return expense;
        }
        throw new RuntimeException("Cannot access this expense.");
    }
}