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
            @RequestParam Integer userId,
            @Valid @RequestBody ExpenseRequest request
            ) {
                // Call the service
                User user = userService.getUserById(userId);
                Category category = categoryService.getCategoryById(request.categoryId());
                Expense expense = expenseMapper.mapToEntity(request, user, category);
                Expense  savedExpense = expenseService.createExpense(expense);
                ExpenseResponse response = expenseMapper.mapToResponse(savedExpense);

                // Return the response
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }

    // Get all user expenses
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses(
            @RequestParam Integer userId
    ) {
        User user = userService.getUserById(userId);
        List<Expense> expenses = expenseService.getExpensesForUser(user);
        List<ExpenseResponse> response = expenses.stream().map(expenseMapper::mapToResponse).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Get expense by ID
    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @RequestParam Integer userId,
            @PathVariable Integer expenseId
    ) {
        User user = userService.getUserById(userId);
        Expense expense = expenseService.getUserExpenseById(user, expenseId);
        ExpenseResponse response = expenseMapper.mapToResponse(expense);

        return ResponseEntity.status(HttpStatus.OK).body(response);
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
        List<Expense> filteredExpenses = expenseService.filterExpenses(user, categoryId, minAmount, maxAmount, start, end);
        List<ExpenseResponse> response = filteredExpenses.stream().map(expenseMapper::mapToResponse).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
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
            @RequestParam Integer userId,
            @PathVariable Integer expenseId,
            @Valid @RequestBody ExpenseRequest request
    ) {
        User user = userService.getUserById(userId);
        Category category = categoryService.getCategoryById(request.categoryId());
        Expense expense = expenseMapper.mapToEntity(request, user, category);
        Expense updatedExpense = expenseService.updateExpense(user, expenseId, expense);
        ExpenseResponse response = expenseMapper.mapToResponse(updatedExpense);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Delete
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            @RequestParam Integer userId,
            @PathVariable Integer expenseId
    ) {
        User user = userService.getUserById(userId);
        expenseService.deleteExpense(user, expenseId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}