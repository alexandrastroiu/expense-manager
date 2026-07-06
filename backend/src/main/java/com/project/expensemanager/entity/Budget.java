package com.project.expensemanager.entity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false, precision = 9, scale = 2)
    private BigDecimal amount;

    @Column(name = "budget_period", nullable = false)
    private LocalDate budgetPeriod;
}