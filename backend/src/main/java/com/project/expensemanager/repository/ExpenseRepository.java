package com.project.expensemanager.repository;

import com.project.expensemanager.entity.Category;
import com.project.expensemanager.entity.Expense;
import com.project.expensemanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    // Query methods
    List<Expense> findByUser(User user);

    List<Expense> findByUserAndExpenseDate(User user, LocalDate expenseDate);

    List<Expense> findByUserAndExpenseDateAfter(User user, LocalDate expenseDate);

    List<Expense> findByUserAndExpenseDateBefore(User user, LocalDate expenseDate);

    List<Expense> findByUserAndExpenseDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<Expense> findByUserAndTitle(User user, String title);

    List<Expense> findByUserAndCategory(User user, Category category);

    List<Expense> findByUserAndCategoryAndExpenseDateBetween(User user, Category category, LocalDate startDate, LocalDate endDate);

    List<Expense> findByUserAndCategoryAndExpenseDateBefore(User user, Category category, LocalDate endDate);

    List<Expense> findByUserAndCategoryAndExpenseDateAfter(User user, Category category, LocalDate startDate);

    List<Expense> findByUserAndCategoryAndExpenseDateBetweenAndAmountBetween(User user, Category category, LocalDate startDate, LocalDate endDate, BigDecimal minAmount, BigDecimal maxAmount);

    List<Expense> findByUserAndAmount(User user, BigDecimal amount);

    List<Expense> findByUserAndAmountBetween(User user, BigDecimal minAmount, BigDecimal maxAmount);

    List<Expense> findByUserAndCategoryAndAmountBetween(User user, Category category,BigDecimal minAmount, BigDecimal maxAmount);

    List<Expense> findByUserAndExpenseDateBetweenAndAmountBetween(User user, LocalDate startDate, LocalDate endDate, BigDecimal minAmount, BigDecimal maxAmount);
}
