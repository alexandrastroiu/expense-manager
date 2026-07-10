package com.project.expensemanager.dto.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse(
        Integer id,
        String title,
        String description,
        BigDecimal amount,
        Integer categoryId,
        LocalDate expenseDate
) {
}
