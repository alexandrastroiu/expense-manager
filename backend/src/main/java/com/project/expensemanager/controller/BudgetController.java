package com.project.expensemanager.controller;

import com.project.expensemanager.dto.budget.BudgetRequest;
import com.project.expensemanager.dto.budget.BudgetResponse;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.repository.BudgetRepository;
import com.project.expensemanager.service.BudgetService;
import com.project.expensemanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")    // Base URL
public class BudgetController {
    private final BudgetService budgetService;
    private final UserService userService;

    public BudgetController(BudgetService budgetService, UserService userService) {
        this.budgetService = budgetService;
        this.userService = userService;
    }

    // Create
    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @RequestParam Integer userId,
            @Valid @RequestBody BudgetRequest request
            ) {
        User user = userService.getUserById(userId);

        BudgetResponse budget = budgetService.createBudget(user, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(budget);
    }

    // Update
    @PutMapping("/{budgetId}")
    public ResponseEntity<> updateBudget(
            @RequestParam Integer userId,
            @PathVariable Integer budgetId,
            @Valid @RequestBody BudgetRequest request
    ) {
        User user = userService.getUserById(userId);

        BudgetResponse budget = budgetService.updateBudget(user, budgetId, request);

        return ResponseEntity.status(HttpStatus.OK).body(budget);
    }

    // Delete
    @DeleteMapping("/{budgetId}")
    public ResponseEntity<> deleteBudget(
            @RequestParam Integer userId,
            @PathVariable Integer budgetId
    ) {
        User user = userService.getUserById(userId);

        budgetService.deleteBudget(user, budgetId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
