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
    public RecurringExpenseResponse createRecurringExpense(User user, RecurringExpenseRequest request) {
        Category category = categoryRepository.findById(request.categoryId()).orElseThrow( () -> new ResourceNotFoundException("Category not found"));
        RecurringExpense recurringExpense = mapToEntity(request, user, category);

        RecurringExpense newRecurringExpense = recurringExpenseRepository.save(recurringExpense);

        return mapToResponse(newRecurringExpense);
    }

    // Update
    public RecurringExpenseResponse updateRecurringExpense(Integer recurringExpenseId,User user, RecurringExpenseRequest request) {
        RecurringExpense recurringExpense = getUserRecurringExpenseEntityById(user, recurringExpenseId);

        Category category = categoryRepository.findById(recurringExpense.getCategory().getId()).orElseThrow( () -> new ResourceNotFoundException("Category not found"));

        recurringExpense.setTitle(request.title());
        recurringExpense.setDescription(request.description());
        recurringExpense.setAmount(request.amount());
        recurringExpense.setCategory(category);
        recurringExpense.setStartDate(request.startDate());
        recurringExpense.setEndDate(request.endDate());
        recurringExpense.setFrequency(request.frequency());

        RecurringExpense updatedRecurringExpense = recurringExpenseRepository.save(recurringExpense);

        return mapToResponse(updatedRecurringExpense);
    }

    // Read
    public RecurringExpenseResponse getUserRecurringExpenseById(User user, Integer recurringExpenseId) {
        RecurringExpense recurringExpense = recurringExpenseRepository.findByUserAndId(user, recurringExpenseId);

        return mapToResponse(recurringExpense);
    }

    public List<RecurringExpenseResponse> getUserRecurringExpenseByTitle(User user, String title) {
        List<RecurringExpense> recurringExpense = recurringExpenseRepository.findByUserAndTitle(user, title);

        return recurringExpense.stream().map(this::mapToResponse).toList();
    }

    public List<RecurringExpenseResponse> getUserRecurringExpenseByCategory(User user, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow( () -> new ResourceNotFoundException("Category not found"));

        List<RecurringExpense> recurringExpense = recurringExpenseRepository.findByUserAndCategory(user, category);

        return recurringExpense.stream().map(this::mapToResponse).toList();
    }

    public List<RecurringExpenseResponse> getUserRecurringExpenseByFrequency(User user, Frequency frequency) {
        List<RecurringExpense> recurringExpense = recurringExpenseRepository.findByUserAndFrequency(user, frequency);

        return recurringExpense.stream().map(this::mapToResponse).toList();
    }

    public List<RecurringExpenseResponse> getAllUserRecurringExpenses(User user) {
        List<RecurringExpense> recurringExpense = recurringExpenseRepository.findByUser(user);

        return recurringExpense.stream().map(this::mapToResponse).toList();
    }

    public List<RecurringExpenseResponse> searchRecurringExpenses(
            User user,
            String title,
            Integer categoryId,
            Frequency frequency
    ) {
        List<RecurringExpenseResponse> userRecurringExpenses;

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
    public List<RecurringExpenseResponse> filterRecurringExpensesByAmount(User user, BigDecimal minAmount, BigDecimal maxAmount) {
        List<RecurringExpense> recurringExpense = recurringExpenseRepository.findByUserAndAmountBetween(user, minAmount, maxAmount);

        return recurringExpense.stream().map(this::mapToResponse).toList();
    }

    // Delete
    public void deleteRecurringExpense(User user, Integer recurringExpenseId) {
    RecurringExpense recurringExpense = getUserRecurringExpenseEntityById(user, recurringExpenseId);
    recurringExpenseRepository.delete(recurringExpense);
    }

    // Helper methods
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

    private RecurringExpense getUserRecurringExpenseEntityById(User user, Integer recurringExpenseId) {
        return recurringExpenseRepository.findByUserAndId(user, recurringExpenseId);
    }
}
