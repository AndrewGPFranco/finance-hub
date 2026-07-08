CREATE TABLE expenses
(
    id                 UUID PRIMARY KEY,
    title              VARCHAR(120)   NOT NULL,
    created_at         TIMESTAMP      NOT NULL,
    updated_at         TIMESTAMP,
    payment_date       DATE           NOT NULL,
    user_id            UUID           NOT NULL,
    amount             DECIMAL(12, 2) NOT NULL CHECK (amount > 0),
    due_date           DATE           NOT NULL,
    status             VARCHAR(30)    NOT NULL CHECK (status IN ('PAID', 'PENDING', 'OVERDUE', 'CANCELED')),
    category           VARCHAR(30)    NOT NULL CHECK (category IN
                                                      ('FOOD', 'HOUSING', 'TRANSPORT', 'HEALTH', 'EDUCATION',
                                                       'ENTERTAINMENT', 'OTHER')),
    payment_method     VARCHAR(30)    NOT NULL CHECK (payment_method IN
                                                      ('PIX', 'DEBIT_CARD', 'CREDIT_CARD', 'TRANSFER', 'OTHER')),
    recurring          BOOLEAN        NOT NULL,
    installment_number INTEGER,
    total_installments INTEGER
);

ALTER TABLE expenses
    ADD CONSTRAINT FK_EXPENSES_USER FOREIGN KEY (user_id) REFERENCES users (ID);