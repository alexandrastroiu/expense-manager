package com.project.expensemanager.mapper;

import com.project.expensemanager.dto.recurringexpense.RecurringExpenseRequest;
import com.project.expensemanager.dto.recurringexpense.RecurringExpenseResponse;
import com.project.expensemanager.entity.Category;
import com.project.expensemanager.entity.RecurringExpense;
import com.project.expensemanager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RecurringExpenseMapper {

    // Map entity to response
    public RecurringExpenseResponse mapToResponse(RecurringExpense recurringExpense) {
        return new RecurringExpenseResponse(
                recurringExpense.getId(),
                recurringExpense.getTitle(),
                recurringExpense.getDescription(),
                recurringExpense.getAmount(),
                recurringExpense.getCategory().getId(),
                recurringExpense.getStartDate(),
                recurringExpense.getEndDate(),
                recurringExpense.getFrequency()
        );
    }

    // Map request to entity
    public RecurringExpense mapToEntity(RecurringExpenseRequest request, User user, Category category) {
        return new RecurringExpense(
                user,
                request.title(),
                request.description(),
                request.amount(),
                category,
                request.startDate(),
                request.endDate(),
                request.frequency()
        );
    }
}
