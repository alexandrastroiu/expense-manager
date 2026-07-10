package com.project.expensemanager.dto.recurringexpense;

import com.project.expensemanager.entity.Frequency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecurringExpenseResponse(
        Integer id,
        String title,
        String description,
        BigDecimal amount,
        Integer categoryId,
        LocalDate startDate,
        LocalDate endDate,
        Frequency frequency
) {
}
