package com.project.expensemanager.repository;

import com.project.expensemanager.entity.Budget;
import com.project.expensemanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    // Query methods
    Optional<Budget> findByUserAndId(User user, Integer budgetId);

    Optional<Budget> findByUserAndPeriod(User user, LocalDate period);
}
