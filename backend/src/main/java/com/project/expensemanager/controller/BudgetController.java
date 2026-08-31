package com.project.expensemanager.controller;

import com.project.expensemanager.dto.budget.BudgetRequest;
import com.project.expensemanager.dto.budget.BudgetResponse;
import com.project.expensemanager.dto.budget.BudgetSummaryResponse;
import com.project.expensemanager.entity.Budget;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.mapper.BudgetMapper;
import com.project.expensemanager.model.BudgetSummary;
import com.project.expensemanager.service.BudgetService;
import com.project.expensemanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/budgets")    // Base URL
public class BudgetController {
    private final BudgetService budgetService;
    private final UserService userService;
    private final BudgetMapper budgetMapper;

    public BudgetController(BudgetService budgetService, UserService userService, BudgetMapper budgetMapper) {
        this.budgetService = budgetService;
        this.userService = userService;
        this.budgetMapper = budgetMapper;
    }

    // Create
    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            Authentication authentication,
            @Valid @RequestBody BudgetRequest request
            ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        Budget budget = budgetMapper.mapToEntity(request, user);
        Budget savedBudget = budgetService.createBudget(user, budget);
        BudgetResponse response = budgetMapper.mapToResponse(savedBudget);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get budget by ID
    @GetMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> getBudgetById(
            Authentication authentication,
            @PathVariable Integer budgetId
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        Budget budget = budgetService.getUserBudgetById(user, budgetId);
        BudgetResponse response = budgetMapper.mapToResponse(budget);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Get budget by period
    @GetMapping
    public ResponseEntity<BudgetResponse> getBudgetByPeriod(
            Authentication authentication,
            @RequestParam @DateTimeFormat LocalDate period
            ) {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);
            Budget budget = budgetService.getUserBudgetByPeriod(user, period);
            BudgetResponse response = budgetMapper.mapToResponse(budget);

            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Budget summary
   @GetMapping("/summary")
   public ResponseEntity<BudgetSummaryResponse> getBudgetSummary(
           Authentication authentication,
           @RequestParam @DateTimeFormat LocalDate period
   ) {
       String username = authentication.getName();
       User user = userService.getUserByUsername(username);
       BudgetSummary summary = budgetService.getBudgetSummary(user, period);
       BudgetSummaryResponse  response = budgetMapper.mapSummaryToResponse(summary);

       return ResponseEntity.status(HttpStatus.OK).body(response);
   }

    // Update
    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> updateBudget(
            Authentication authentication,
            @PathVariable Integer budgetId,
            @Valid @RequestBody BudgetRequest request
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        Budget budget = budgetMapper.mapToEntity(request, user);
        Budget updatedBudget = budgetService.updateBudget(user, budgetId, budget);
        BudgetResponse response = budgetMapper.mapToResponse(updatedBudget);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Delete
    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(
            Authentication authentication,
            @PathVariable Integer budgetId
    ) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        budgetService.deleteBudget(user, budgetId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}