-- =========================================================
-- DATABASE CREATION
-- =========================================================
CREATE DATABASE IF NOT EXISTS digital_wallet_db;
USE digital_wallet_db;

-- =========================================================
-- USERS TABLE
-- =========================================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'USER',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =========================================================
-- WALLET TABLE
-- =========================================================
CREATE TABLE wallet (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    currency VARCHAR(10) DEFAULT 'INR',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_wallet_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE RESTRICT
);


ALTER TABLE wallet
ADD CONSTRAINT uq_wallet_user UNIQUE (user_id);

-- =========================================================
-- CATEGORY TABLE
-- =========================================================
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL, -- EXPENSE / INCOME
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================================================
-- TRANSACTION TABLE
-- =========================================================
CREATE TABLE wallet_transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wallet_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL, -- CREDIT / DEBIT
    description VARCHAR(255),
    reference_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tx_wallet
        FOREIGN KEY (wallet_id)
        REFERENCES wallet(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_tx_category
        FOREIGN KEY (category_id)
        REFERENCES category(id)
        ON DELETE RESTRICT
);

ALTER TABLE wallet_transaction
ADD CONSTRAINT chk_tx_type
CHECK (transaction_type IN ('CREDIT', 'DEBIT'));


-- =========================================================
-- SAMPLE MASTER DATA (OPTIONAL FOR TRAINING)
-- =========================================================

-- Users
INSERT INTO users (username, full_name, email, role)
VALUES
('chirag', 'Chirag Tank', 'chirag@example.com', 'USER'),
('admin', 'System Admin', 'admin@example.com', 'ADMIN');

INSERT INTO users (username, full_name, email, role)
VALUES
('amit', 'Amit Sharma', 'amit@example.com', 'USER'),
('neha', 'Neha Verma', 'neha@example.com', 'USER'),
('rahul', 'Rahul Mehta', 'rahul@example.com', 'USER'),
('priya', 'Priya Singh', 'priya@example.com', 'USER'),
('suresh', 'Suresh Iyer', 'suresh@example.com', 'USER');


-- Wallets


INSERT INTO wallet (user_id, balance, currency)
VALUES
(1, 15000.00, 'INR'),
(2, 12000.00, 'INR'),
(3, 18000.00, 'INR'),
(4, 9000.00, 'INR'),
(5, 20000.00, 'INR'),
(6, 11000.00, 'INR'),
(7, 14000.00, 'INR');


-- Categories
INSERT INTO category (id, name, type) VALUES
(1, 'Food', 'EXPENSE'),
(2, 'Travel', 'EXPENSE'),
(3, 'Bills', 'EXPENSE'),
(4, 'Medical', 'EXPENSE'),
(5, 'Education', 'EXPENSE'),
(6, 'Investment', 'EXPENSE'),
(7, 'Salary', 'INCOME'),
(8, 'Refund', 'INCOME'),
(9, 'Interest', 'INCOME'),
(10, 'Gift', 'INCOME');


-- Transactions
INSERT INTO wallet_transaction
(wallet_id, category_id, amount, transaction_type, description, reference_id)
VALUES

-- ================= WALLET 1 =================
(1, 7, 50000, 'CREDIT', 'Monthly Salary', 'SAL-001'),
(1, 1, 800, 'DEBIT', 'Grocery Shopping', 'TXN-002'),
(1, 2, 1200, 'DEBIT', 'Cab Charges', 'TXN-003'),
(1, 9, 1100, 'CREDIT', 'Savings Interest', 'INT-004'),
(1, 6, 8000, 'DEBIT', 'Mutual Fund SIP', 'TXN-005'),
(1, 8, 2000, 'CREDIT', 'Refund Received', 'REF-006'),
(1, 4, 2500, 'DEBIT', 'Medical Bill', 'TXN-007'),

-- ================= WALLET 2 =================
(2, 7, 42000, 'CREDIT', 'Monthly Salary', 'SAL-008'),
(2, 1, 900, 'DEBIT', 'Daily Groceries', 'TXN-009'),
(2, 3, 3000, 'DEBIT', 'Electricity Bill', 'TXN-010'),
(2, 9, 950, 'CREDIT', 'Interest Credit', 'INT-011'),
(2, 2, 1800, 'DEBIT', 'Train Travel', 'TXN-012'),
(2, 8, 1500, 'CREDIT', 'Refund Credit', 'REF-013'),
(2, 6, 7000, 'DEBIT', 'Stock Investment', 'TXN-014'),

-- ================= WALLET 3 =================
(3, 7, 55000, 'CREDIT', 'Monthly Salary', 'SAL-015'),
(3, 1, 1200, 'DEBIT', 'Food Expense', 'TXN-016'),
(3, 5, 9000, 'DEBIT', 'Course Fees', 'TXN-017'),
(3, 9, 1300, 'CREDIT', 'Interest Earned', 'INT-018'),
(3, 2, 2200, 'DEBIT', 'Flight Ticket', 'TXN-019'),
(3, 8, 1800, 'CREDIT', 'Refund Received', 'REF-020'),
(3, 6, 10000, 'DEBIT', 'Equity Investment', 'TXN-021'),

-- ================= WALLET 4 =================
(4, 7, 38000, 'CREDIT', 'Monthly Salary', 'SAL-022'),
(4, 1, 700, 'DEBIT', 'Vegetable Purchase', 'TXN-023'),
(4, 3, 2600, 'DEBIT', 'Water Bill', 'TXN-024'),
(4, 9, 850, 'CREDIT', 'Interest Credit', 'INT-025'),
(4, 2, 1600, 'DEBIT', 'Bus Travel', 'TXN-026'),
(4, 8, 1200, 'CREDIT', 'Refund Credit', 'REF-027'),
(4, 4, 2800, 'DEBIT', 'Doctor Visit', 'TXN-028'),

-- ================= WALLET 5 =================
(5, 7, 60000, 'CREDIT', 'Monthly Salary', 'SAL-029'),
(5, 1, 1500, 'DEBIT', 'Supermarket Shopping', 'TXN-030'),
(5, 2, 2400, 'DEBIT', 'Cab Booking', 'TXN-031'),
(5, 9, 1400, 'CREDIT', 'Interest Earned', 'INT-032'),
(5, 6, 12000, 'DEBIT', 'Mutual Fund Investment', 'TXN-033'),
(5, 8, 2200, 'CREDIT', 'Refund Received', 'REF-034'),
(5, 10, 3000, 'CREDIT', 'Gift Received', 'GFT-035'),

-- ================= WALLET 6 =================
(6, 7, 45000, 'CREDIT', 'Monthly Salary', 'SAL-036'),
(6, 1, 950, 'DEBIT', 'Groceries', 'TXN-037'),
(6, 3, 2800, 'DEBIT', 'Internet Bill', 'TXN-038'),
(6, 9, 1000, 'CREDIT', 'Interest Credit', 'INT-039'),
(6, 2, 1900, 'DEBIT', 'Office Commute', 'TXN-040'),
(6, 8, 1700, 'CREDIT', 'Refund Credit', 'REF-041'),
(6, 5, 8000, 'DEBIT', 'Exam Fees', 'TXN-042'),

-- ================= WALLET 7 =================
(7, 7, 50000, 'CREDIT', 'Monthly Salary', 'SAL-043'),
(7, 1, 1100, 'DEBIT', 'Daily Food', 'TXN-044'),
(7, 2, 2100, 'DEBIT', 'Taxi Charges', 'TXN-045'),
(7, 9, 1200, 'CREDIT', 'Interest Earned', 'INT-046'),
(7, 6, 9000, 'DEBIT', 'Investment Payment', 'TXN-047'),
(7, 8, 1600, 'CREDIT', 'Refund Received', 'REF-048'),
(7, 4, 2600, 'DEBIT', 'Medical Expense', 'TXN-049'),
(7, 10, 3500, 'CREDIT', 'Gift Credit', 'GFT-050');

-- =========================================================
-- END OF FILE
-- =========================================================