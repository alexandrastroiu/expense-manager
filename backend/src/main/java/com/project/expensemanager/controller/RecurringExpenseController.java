package com.project.expensemanager.controller;

import com.project.expensemanager.dto.recurringexpense.RecurringExpenseRequest;
import com.project.expensemanager.dto.recurringexpense.RecurringExpenseResponse;
import com.project.expensemanager.entity.Category;
import com.project.expensemanager.entity.Frequency;
import com.project.expensemanager.entity.RecurringExpense;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.mapper.RecurringExpenseMapper;
import com.project.expensemanager.service.CategoryService;
import com.project.expensemanager.service.RecurringExpenseService;
import com.project.expensemanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/recurringexpenses")  // Base URL
public class RecurringExpenseController {

    private final UserService userService;
    private final RecurringExpenseService recurringExpenseService;
    private final CategoryService categoryService;
    private final RecurringExpenseMapper recurringExpenseMapper;

    public RecurringExpenseController(UserService userService, RecurringExpenseService recurringExpenseService, CategoryService categoryService, RecurringExpenseMapper recurringExpenseMapper) {
        this.userService = userService;
        this.recurringExpenseService = recurringExpenseService;
        this.categoryService = categoryService;
        this.recurringExpenseMapper = recurringExpenseMapper;
    }

    // Create
    @PostMapping
    public ResponseEntity<RecurringExpenseResponse> createRecurringExpense(
            Authentication authentication,
            @Valid @RequestBody RecurringExpenseRequest request
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        Category category = categoryService.getCategoryById(request.categoryId());
        RecurringExpense recurringExpense = recurringExpenseMapper.mapToEntity(request, user, category);
        RecurringExpense savedRecurringExpense = recurringExpenseService.createRecurringExpense(recurringExpense);
        RecurringExpenseResponse response = recurringExpenseMapper.mapToResponse(savedRecurringExpense);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Update
    @PutMapping("/{recurringExpenseId}")
    public ResponseEntity<RecurringExpenseResponse> updateRecurringExpense(
            Authentication authentication,
            @PathVariable Integer recurringExpenseId,
            @Valid @RequestBody RecurringExpenseRequest request
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        Category category = categoryService.getCategoryById(request.categoryId());
        RecurringExpense recurringExpense = recurringExpenseMapper.mapToEntity(request, user, category);
        RecurringExpense updatedExpense = recurringExpenseService.updateRecurringExpense(recurringExpenseId, user, recurringExpense);
        RecurringExpenseResponse response = recurringExpenseMapper.mapToResponse(updatedExpense);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Get all user recurring expenses
    @GetMapping
    public ResponseEntity<List<RecurringExpenseResponse>> getAllRecurringExpenses(
            Authentication authentication
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        List<RecurringExpense> expenses = recurringExpenseService.getAllUserRecurringExpenses(user);
        List<RecurringExpenseResponse> response = expenses.stream().map(recurringExpenseMapper::mapToResponse).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Get recurring expense by ID
    @GetMapping("/{recurringExpenseId}")
    public ResponseEntity<RecurringExpenseResponse> getExpenseById(
            Authentication authentication,
            @PathVariable Integer recurringExpenseId
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        RecurringExpense expense = recurringExpenseService.getUserRecurringExpenseById(user, recurringExpenseId);
        RecurringExpenseResponse response = recurringExpenseMapper.mapToResponse(expense);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Search recurring expenses
    @GetMapping("/search")
    public ResponseEntity<List<RecurringExpenseResponse>> searchRecurringExpenses (
            Authentication authentication,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Frequency frequency,
            @RequestParam(required = false) Integer categoryId
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        List<RecurringExpense> expenses = recurringExpenseService.searchRecurringExpenses(
                user,
                title,
                categoryId,
                frequency
        );
        List<RecurringExpenseResponse> response = expenses.stream().map(recurringExpenseMapper::mapToResponse).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Filter recurring expenses by amount
    @GetMapping("/filter")
    public ResponseEntity<List<RecurringExpenseResponse>> filterRecurringExpenses (
            Authentication authentication,
            @RequestParam BigDecimal minAmount,
            @RequestParam BigDecimal maxAmount
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        List<RecurringExpense> expenses = recurringExpenseService.filterRecurringExpensesByAmount(user, minAmount, maxAmount);
        List<RecurringExpenseResponse> response = expenses.stream().map(recurringExpenseMapper::mapToResponse).toList();


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Delete
    @DeleteMapping("/{recurringExpenseId}")
    public ResponseEntity<Void> deleteRecurringExpense(
            Authentication authentication,
            @PathVariable Integer recurringExpenseId
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        recurringExpenseService.deleteRecurringExpense(user, recurringExpenseId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}