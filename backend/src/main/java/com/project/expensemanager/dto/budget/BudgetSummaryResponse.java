package com.project.expensemanager.dto.budget;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetSummaryResponse(
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