package com.project.expensemanager.mapper;

import com.project.expensemanager.dto.expense.ExpenseRequest;
import com.project.expensemanager.dto.expense.ExpenseResponse;
import com.project.expensemanager.entity.Category;
import com.project.expensemanager.entity.Expense;
import com.project.expensemanager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    // Map entity to response
    public ExpenseResponse mapToResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getTitle(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory().getId(),
                expense.getExpenseDate()
        );
    }

    // Map request to entity
    public Expense mapToEntity(ExpenseRequest request, User user, Category category) {
        return new Expense(
                user,
                request.title(),
                request.description(),
                request.amount(),
                category,
                request.expenseDate()
        );
    }
}