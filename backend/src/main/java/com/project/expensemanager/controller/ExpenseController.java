package com.project.expensemanager.controller;

import com.project.expensemanager.dto.expense.ExpenseRequest;
import com.project.expensemanager.dto.expense.ExpenseResponse;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.service.ExpenseService;
import com.project.expensemanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")        // Base URL
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;

    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    // Create
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @RequestParam Integer userId,
            @Valid @RequestBody ExpenseRequest request
            ) {
                // Call the service
                User user = userService.getUserById(userId);

                ExpenseResponse response = expenseService.createExpense(user, request);

                // Return the response
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }

    // Get all user expenses
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses(
            @RequestParam Integer userId
    ) {
        User user = userService.getUserById(userId);

        List<ExpenseResponse> expenses = expenseService.getExpensesForUser(user);

        return ResponseEntity.status(HttpStatus.OK).body(expenses);
    }

    // Get expense by ID
    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @RequestParam Integer userId,
            @PathVariable Integer expenseId
    ) {
        User user = userService.getUserById(userId);

        ExpenseResponse expense = expenseService.getUserExpenseById(user, expenseId);

        return ResponseEntity.status(HttpStatus.OK).body(expense);
    }

    // Filter expenses
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getExpenses(
            @RequestParam Integer userId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end
            ) {
        User user = userService.getUserById(userId);

        List<ExpenseResponse> filteredExpenses = expenseService.filterExpenses(user, categoryId, minAmount, maxAmount, start, end);

        return ResponseEntity.status(HttpStatus.OK).body(filteredExpenses);
        }

     // Search expenses
    @GetMapping("/search")
    public ResponseEntity<List<ExpenseResponse>> searchExpenses (
        @RequestParam Integer userId,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) LocalDate expenseDate,
        @RequestParam(required = false) BigDecimal amount,
        @RequestParam(required = false) Integer categoryId
    ) {
        User user = userService.getUserById(userId);

        List<ExpenseResponse> expenses = expenseService.searchExpenses(
                user,
                expenseDate,
                title,
                categoryId,
                amount
        );

        return ResponseEntity.status(HttpStatus.OK).body(expenses);
    }

    // Update
    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @RequestParam Integer userId,
            @PathVariable Integer expenseId,
            @Valid @RequestBody ExpenseRequest request
    ) {
        User user = userService.getUserById(userId);

        ExpenseResponse updatedExpense = expenseService.updateExpense(expenseId, user, request);

        return ResponseEntity.status(HttpStatus.OK).body(updatedExpense);
    }

    // Delete
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> deleteExpense(
            @RequestParam Integer userId,
            @PathVariable Integer expenseId
    ) {
        User user = userService.getUserById(userId);

        expenseService.deleteExpense(user, expenseId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
