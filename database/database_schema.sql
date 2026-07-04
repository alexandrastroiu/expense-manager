/** Expense Manager Database Schema **/

CREATE DATABASE IF NOT EXISTS expense_manager;

-- USERS
CREATE TABLE users(
    id  SERIAL PRIMARY KEY NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- EXPENSES
CREATE TABLE expenses(
    id  SERIAL PRIMARY KEY NOT NULL,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    amount NUMERIC(9, 2) NOT NULL CHECK (amount > 0),
    category_id INT NOT NULL REFERENCES categories(id),
    expense_date DATE NOT NULL
);

-- RECURRING EXPENSES
CREATE TABLE recurring_expenses(
    id SERIAL PRIMARY KEY NOT NULL,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    amount NUMERIC(9,2) NOT NULL CHECK (amount > 0),
    category_id INT NOT NULL REFERENCES categories(id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    frequency VARCHAR(50) NOT NULL
);

-- BUDGETS
CREATE TABLE budgets(
    id SERIAL PRIMARY KEY NOT NULL,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    amount NUMERIC(9, 2) NOT NULL CHECK (amount >= 0),
    budget_date DATE NOT NULL
);

-- CATEGORIES
CREATE TABLE categories(
    id SERIAL PRIMARY KEY NOT NULL,
    category_name VARCHAR(50) NOT NULL
);