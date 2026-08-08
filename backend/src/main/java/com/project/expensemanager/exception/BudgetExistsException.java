package com.project.expensemanager.exception;

public class BudgetExistsException extends RuntimeException {
    public BudgetExistsException(String message) {
        super(message);
    }
}
