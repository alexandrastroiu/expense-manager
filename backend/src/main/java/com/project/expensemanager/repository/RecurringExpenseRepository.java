package com.project.expensemanager.repository;

import com.project.expensemanager.entity.RecurringExpense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, Integer> {
}
