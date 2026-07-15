package com.project.expensemanager.controller;

import com.project.expensemanager.dto.expense.ExpenseRequest;
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
