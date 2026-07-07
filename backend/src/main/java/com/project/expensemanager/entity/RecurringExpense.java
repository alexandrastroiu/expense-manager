package com.project.expensemanager.entity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "recurring_expenses")
public class RecurringExpense {

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

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false, length = 50)
    private Frequency frequency;

    // Default constructor
    protected RecurringExpense() {}

    // Parameterized constructor
    public RecurringExpense(User user, String title, String description, BigDecimal amount, Category category, LocalDate startDate, LocalDate endDate, Frequency frequency) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.frequency = frequency;
    }
}