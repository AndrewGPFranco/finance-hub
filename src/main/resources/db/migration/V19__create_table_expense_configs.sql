CREATE TABLE expense_configs
(
    id             UUID NOT NULL,
    subdomain_id   UUID NOT NULL,
    user_id        UUID NOT NULL,
    date_to_use    date NOT NULL,
    created_at     TIMESTAMP(6) WITHOUT TIME ZONE,
    updated_at     TIMESTAMP(6) WITHOUT TIME ZONE,
    payment_date   date,
    amount         DECIMAL(12, 2),
    due_date       date,
    status         VARCHAR(255),
    category       VARCHAR(255),
    payment_method VARCHAR(255),
    CONSTRAINT pk_expense_configs PRIMARY KEY (id)
);

ALTER TABLE expense_configs
    ADD CONSTRAINT uk_user_subdomain_date_to_use UNIQUE (user_id, subdomain_id, date_to_use);

CREATE INDEX idx_user_subdomain_date_to_use ON expense_configs (user_id, subdomain_id, date_to_use);

ALTER TABLE expense_configs
    ADD CONSTRAINT FK_EXPENSE_CONFIGS_ON_SUBDOMAIN FOREIGN KEY (subdomain_id) REFERENCES subdomains (id);

ALTER TABLE expense_configs
    ADD CONSTRAINT FK_EXPENSE_CONFIGS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);
