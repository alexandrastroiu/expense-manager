package com.project.expensemanager.dto.budget;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetResponse(
        Integer id,
        BigDecimal amount,
        LocalDate budgetPeriod
) {
}
