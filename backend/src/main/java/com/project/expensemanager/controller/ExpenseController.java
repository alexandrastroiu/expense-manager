package com.project.expensemanager.controller;

import com.project.expensemanager.dto.expense.ExpenseRequest;
import com.project.expensemanager.dto.expense.ExpenseResponse;
import com.project.expensemanager.entity.Category;
import com.project.expensemanager.entity.Expense;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.mapper.BudgetMapper;
import com.project.expensemanager.mapper.ExpenseMapper;
import com.project.expensemanager.service.CategoryService;
import com.project.expensemanager.service.ExpenseService;
import com.project.expensemanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")        // Base URL
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ExpenseMapper expenseMapper;

    public ExpenseController(ExpenseService expenseService, UserService userService, CategoryService categoryService, ExpenseMapper expenseMapper) {
        this.expenseService = expenseService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.expenseMapper = expenseMapper;
    }

    // Create
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            Authentication authentication,
            @Valid @RequestBody ExpenseRequest request
            ) {
                String username = authentication.getName();
                // Call the service
                User user = userService.getUserByUsername(username);
                Category category = categoryService.getCategoryById(request.categoryId());
                Expense expense = expenseMapper.mapToEntity(request, user, category);
                Expense  savedExpense = expenseService.createExpense(expense);
                ExpenseResponse response = expenseMapper.mapToResponse(savedExpense);

                // Return the response
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }

    // Get expense by ID
    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            Authentication authentication,
            @PathVariable Integer expenseId
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        Expense expense = expenseService.getUserExpenseById(user, expenseId);
        ExpenseResponse response = expenseMapper.mapToResponse(expense);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Filter expenses
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getExpenses(
            Authentication authentication,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end
            ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        List<Expense> filteredExpenses = expenseService.filterExpenses(user, categoryId, minAmount, maxAmount, start, end);
        List<ExpenseResponse> response = filteredExpenses.stream().map(expenseMapper::mapToResponse).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
        }

     // Search expenses
    @GetMapping("/search")
    public ResponseEntity<List<ExpenseResponse>> searchExpenses (
        Authentication authentication,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) LocalDate expenseDate,
        @RequestParam(required = false) BigDecimal amount,
        @RequestParam(required = false) Integer categoryId
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        List<Expense> expenses = expenseService.searchExpenses(
                user,
                expenseDate,
                title,
                categoryId,
                amount
        );
        List<ExpenseResponse> response = expenses.stream().map(expenseMapper::mapToResponse).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Update
    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            Authentication authentication,
            @PathVariable Integer expenseId,
            @Valid @RequestBody ExpenseRequest request
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        Category category = categoryService.getCategoryById(request.categoryId());
        Expense expense = expenseMapper.mapToEntity(request, user, category);
        Expense updatedExpense = expenseService.updateExpense(user, expenseId, expense);
        ExpenseResponse response = expenseMapper.mapToResponse(updatedExpense);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Delete
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            Authentication authentication,
            @PathVariable Integer expenseId
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        expenseService.deleteExpense(user, expenseId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}