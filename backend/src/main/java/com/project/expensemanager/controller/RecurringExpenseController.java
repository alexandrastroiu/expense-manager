package com.project.expensemanager.controller;

import com.project.expensemanager.dto.expense.ExpenseResponse;
import com.project.expensemanager.dto.recurringexpense.RecurringExpenseRequest;
import com.project.expensemanager.dto.recurringexpense.RecurringExpenseResponse;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.service.RecurringExpenseService;
import com.project.expensemanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurringexpenses")  // Base URL
public class RecurringExpenseController {

    private final UserService userService;
    private final RecurringExpenseService recurringExpenseService;

    public RecurringExpenseController(UserService userService, RecurringExpenseService recurringExpenseService) {
        this.userService = userService;
        this.recurringExpenseService = recurringExpenseService;
    }

    // Create
    @PostMapping
    public ResponseEntity<RecurringExpenseResponse> createRecurringExpense(
            @RequestParam Integer userId,
            @Valid @RequestBody RecurringExpenseRequest request
    ) {
        User user = userService.getUserById(userId);

        RecurringExpenseResponse recurringExpense = recurringExpenseService.createRecurringExpense(user, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(recurringExpense);
    }

    // Update
    @PutMapping("/{recurringExpenseId}")
    public ResponseEntity<RecurringExpenseResponse> updateRecurringExpense(
            @RequestParam Integer userId,
            @PathVariable Integer recurringExpenseId,
            @Valid @RequestBody RecurringExpenseRequest request
    ) {
        User user = userService.getUserById(userId);

        RecurringExpenseResponse updatedExpense = recurringExpenseService.updateRecurringExpense(recurringExpenseId, user, request);

        return ResponseEntity.status(HttpStatus.OK).body(updatedExpense);
    }

    // Get all user recurring expenses
    @GetMapping
    public ResponseEntity<List<RecurringExpenseResponse>> getAllRecurringExpenses(
            @RequestParam Integer userId
    ) {
        User user = userService.getUserById(userId);

        List<RecurringExpenseResponse> expenses = recurringExpenseService.getAllUserRecurringExpenses(user);

        return ResponseEntity.status(HttpStatus.OK).body(expenses);
    }

    // Get recurring expense by ID
    @GetMapping("/{recurringExpenseId}")
    public ResponseEntity<RecurringExpenseResponse> getExpenseById(
            @RequestParam Integer userId,
            @PathVariable Integer recurringExpenseId
    ) {
        User user = userService.getUserById(userId);

        RecurringExpenseResponse expense = recurringExpenseService.getUserRecurringExpenseById(user, recurringExpenseId);

        return ResponseEntity.status(HttpStatus.OK).body(expense);
    }

    // Search recurring expenses
    //@GetMapping("/search")

    // Delete
    @DeleteMapping("/{recurringExpenseId}")
    public ResponseEntity<Void> deleteRecurringExpense(
            @RequestParam Integer userId,
            @PathVariable Integer recurringExpenseId
    ) {
        User user = userService.getUserById(userId);

        recurringExpenseService.deleteRecurringExpense(user, recurringExpenseId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
