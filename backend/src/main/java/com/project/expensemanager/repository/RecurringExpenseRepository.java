package com.project.expensemanager.repository;

import com.project.expensemanager.entity.Frequency;
import com.project.expensemanager.entity.RecurringExpense;
import com.project.expensemanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.List;

public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, Integer> {

    // Query methods
    List<RecurringExpense> findByUser(User user);

    List<RecurringExpense> findByUserOrderByAmount(User user);

    List<RecurringExpense> findByUserAndTitle(User user, String title);

    List<RecurringExpense> findByUserAndAmountBetween(User user, BigDecimal startAmount, BigDecimal endAmount);

    List<RecurringExpense> findByUserAndFrequency(User user, Frequency frequency);
}
