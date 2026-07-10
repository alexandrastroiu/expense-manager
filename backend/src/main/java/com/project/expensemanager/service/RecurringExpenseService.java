package com.project.expensemanager.service;

import com.project.expensemanager.dto.recurringexpense.RecurringExpenseRequest;
import com.project.expensemanager.dto.recurringexpense.RecurringExpenseResponse;
import com.project.expensemanager.entity.Category;
import com.project.expensemanager.entity.Frequency;
import com.project.expensemanager.entity.RecurringExpense;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.repository.CategoryRepository;
import com.project.expensemanager.repository.RecurringExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    public RecurringExpense createRecurringExpense(User user, String title, String description, BigDecimal amount, Integer categoryId, LocalDate startDate, LocalDate endDate, Frequency frequency) {
        Category category = categoryRepository.findById(categoryId).orElseThrow( () -> new RuntimeException("Category not found"));
        RecurringExpense recurringExpense = new RecurringExpense(user, title, description, amount, category, startDate, endDate, frequency);

        return recurringExpenseRepository.save(recurringExpense);
    }

    // Update
    public RecurringExpense updateRecurringExpense(Integer recurringExpenseId,User user, String title, String description, BigDecimal amount, Integer categoryId, LocalDate startDate, LocalDate endDate, Frequency frequency) {
        RecurringExpense recurringExpense = getUserRecurringExpenseById(user, recurringExpenseId);

        Category category = categoryRepository.findById(categoryId).orElseThrow( () -> new RuntimeException("Category not found"));

        recurringExpense.setTitle(title);
        recurringExpense.setDescription(description);
        recurringExpense.setAmount(amount);
        recurringExpense.setCategory(category);
        recurringExpense.setStartDate(startDate);
        recurringExpense.setEndDate(endDate);
        recurringExpense.setFrequency(frequency);

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
        Category category = categoryRepository.findById(categoryId).orElseThrow( () -> new RuntimeException("Category not found"));

        return recurringExpenseRepository.findByUserAndCategory(user, category);
    }

    public List<RecurringExpense> getUserRecurringExpenseByFrequency(User user, Frequency frequency) {
        return recurringExpenseRepository.findByUserAndFrequency(user, frequency);
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

    // Map entity to response
    private RecurringExpenseResponse mapToResponse(RecurringExpense recurringExpense) {
        return new RecurringExpenseResponse(
                recurringExpense.getId(),
                recurringExpense.getTitle(),
                recurringExpense.getDescription(),
                recurringExpense.getAmount(),
                recurringExpense.getCategory().getId(),
                recurringExpense.getStartDate(),
                recurringExpense.getEndDate(),
                recurringExpense.getFrequency()
        );
    }

    // Map request to entity
    private RecurringExpense mapToEntity(RecurringExpenseRequest request, User user, Category category) {
        return new RecurringExpense(
                user,
                request.title(),
                request.description(),
                request.amount(),
                category,
                request.startDate(),
                request.endDate(),
                request.frequency()
                );
    }
}
