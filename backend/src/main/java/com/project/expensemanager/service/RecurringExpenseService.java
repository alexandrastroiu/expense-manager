package com.project.expensemanager.service;

import com.project.expensemanager.dto.recurringexpense.RecurringExpenseRequest;
import com.project.expensemanager.dto.recurringexpense.RecurringExpenseResponse;
import com.project.expensemanager.entity.Category;
import com.project.expensemanager.entity.Frequency;
import com.project.expensemanager.entity.RecurringExpense;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.exception.ResourceNotFoundException;
import com.project.expensemanager.repository.CategoryRepository;
import com.project.expensemanager.repository.RecurringExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RecurringExpenseService {
    // Inject repositories
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final CategoryRepository categoryRepository;

    public RecurringExpenseService(RecurringExpenseRepository recurringExpenseRepository, CategoryRepository categoryRepository) {
        this.recurringExpenseRepository = recurringExpenseRepository;
        this.categoryRepository = categoryRepository;
    }

    // Business logic
    // Create
    public RecurringExpense createRecurringExpense(RecurringExpense recurringExpense) {
        Category category = categoryRepository.findById(recurringExpense.getCategory().getId()).orElseThrow( () -> new ResourceNotFoundException("Category not found"));

        return recurringExpenseRepository.save(recurringExpense);
    }

    // Update
    public RecurringExpense updateRecurringExpense(Integer recurringExpenseId,User user, RecurringExpense updatedRecurringExpense) {
        RecurringExpense recurringExpense = getUserRecurringExpenseById(user, recurringExpenseId);

        Category category = categoryRepository.findById(recurringExpense.getCategory().getId()).orElseThrow( () -> new ResourceNotFoundException("Category not found"));

        recurringExpense.setTitle(updatedRecurringExpense.getTitle());
        recurringExpense.setDescription(updatedRecurringExpense.getDescription());
        recurringExpense.setAmount(updatedRecurringExpense.getAmount());
        recurringExpense.setCategory(updatedRecurringExpense.getCategory());
        recurringExpense.setStartDate(updatedRecurringExpense.getStartDate());
        recurringExpense.setEndDate(updatedRecurringExpense.getEndDate());
        recurringExpense.setFrequency(updatedRecurringExpense.getFrequency());

        return recurringExpenseRepository.save(recurringExpense);
    }

    // Read
    public RecurringExpense getUserRecurringExpenseById(User user, Integer recurringExpenseId) {
        return recurringExpenseRepository.findByUserAndId(user, recurringExpenseId);
    }

    public List<RecurringExpense> getUserRecurringExpenseByTitle(User user, String title) {
        return recurringExpenseRepository.findByUserAndTitle(user, title);
    }

    public List<RecurringExpense> getUserRecurringExpenseByCategory(User user, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow( () -> new ResourceNotFoundException("Category not found"));

        return recurringExpenseRepository.findByUserAndCategory(user, category);
    }

    public List<RecurringExpense> getUserRecurringExpenseByFrequency(User user, Frequency frequency) {
        return recurringExpenseRepository.findByUserAndFrequency(user, frequency);
    }

    public List<RecurringExpense> getAllUserRecurringExpenses(User user) {
        return recurringExpenseRepository.findByUser(user);
    }

    public List<RecurringExpense> searchRecurringExpenses(
            User user,
            String title,
            Integer categoryId,
            Frequency frequency
    ) {
        List<RecurringExpense> userRecurringExpenses;

        if (title != null) {
            userRecurringExpenses = getUserRecurringExpenseByTitle(user, title);
        }
        else if (categoryId != null) {
            userRecurringExpenses = getUserRecurringExpenseByCategory(user, categoryId);
        }
        else if (frequency != null) {
            userRecurringExpenses = getUserRecurringExpenseByFrequency(user, frequency);
        }
        else {
            userRecurringExpenses = getAllUserRecurringExpenses(user);
        }

        return userRecurringExpenses;
    }

    // Filter
    public List<RecurringExpense> filterRecurringExpensesByAmount(User user, BigDecimal minAmount, BigDecimal maxAmount) {
       return recurringExpenseRepository.findByUserAndAmountBetween(user, minAmount, maxAmount);
    }

    // Delete
    public void deleteRecurringExpense(User user, Integer recurringExpenseId) {
    RecurringExpense recurringExpense = getUserRecurringExpenseById(user, recurringExpenseId);
    recurringExpenseRepository.delete(recurringExpense);
    }
    
}
