package com.project.expensemanager.service;

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
        Category selectedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category " + categoryId + " not found."));

        Expense newExpense = new Expense(user, title, description, amount, selectedCategory, expenseDate);

        return expenseRepository.save(newExpense);
    }

    public List<Expense> getExpensesForUser() {

    }

    public Expense updateExpense() {

    }

    public void deleteExpense() {}

}
