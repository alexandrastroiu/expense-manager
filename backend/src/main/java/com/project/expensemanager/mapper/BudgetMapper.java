package com.project.expensemanager.mapper;

import com.project.expensemanager.dto.budget.BudgetRequest;
import com.project.expensemanager.dto.budget.BudgetResponse;
import com.project.expensemanager.dto.budget.BudgetSummaryResponse;
import com.project.expensemanager.entity.Budget;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.model.BudgetSummary;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {

    // Map entity to response
    public BudgetResponse mapToResponse(Budget budget) {
        return new BudgetResponse(
                budget.getId(),
                budget.getAmount(),
                budget.getBudgetPeriod()
        );
    }

    // Map request to entity
    public Budget mapToEntity(BudgetRequest request, User user) {
        return new Budget(
                user,
                request.amount(),
                request.budgetPeriod()
        );
    }

    public BudgetSummaryResponse mapSummaryToResponse(BudgetSummary summary) {
        return new BudgetSummaryResponse(
                summary.id(),
                summary.amount(),
                summary.budgetPeriod(),
                summary.totalCurrentExpenses(),
                summary.totalMonthlyExpenses(),
                summary.remainingCurrentBudget(),
                summary.remainingMonthlyBudget(),
                summary.budgetPercentage()
        );
    }
}
