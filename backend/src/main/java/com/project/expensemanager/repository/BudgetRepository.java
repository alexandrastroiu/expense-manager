package com.project.expensemanager.repository;

import com.project.expensemanager.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {

}
