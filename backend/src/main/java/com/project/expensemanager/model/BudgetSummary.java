package com.project.expensemanager.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetSummary(
    Integer id,
    BigDecimal amount,
    LocalDate budgetPeriod,
    BigDecimal totalCurrentExpenses,
    BigDecimal totalMonthlyExpenses,
    BigDecimal remainingCurrentBudget,
    BigDecimal remainingMonthlyBudget,
    BigDecimal budgetPercentage
) {
}
