package com.project.expensemanager.entity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", nullable = false, precision = 9, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    // Default constructor
    protected Expense() {}

    // Parameterized constructor
    public Expense(User user, String title, String description, BigDecimal amount, Category category, LocalDate expenseDate) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.expenseDate = expenseDate;
    }
}