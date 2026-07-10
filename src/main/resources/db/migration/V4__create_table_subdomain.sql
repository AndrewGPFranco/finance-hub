CREATE TABLE subdomains
(
    id         UUID                        NOT NULL,
    user_id    UUID                        NOT NULL,
    name       VARCHAR(255)                NOT NULL,
    url_photo  VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_subdomains PRIMARY KEY (id)
);

ALTER TABLE subdomains
    ADD CONSTRAINT name_user_unique UNIQUE (name, user_id);

CREATE INDEX idx_name_user ON subdomains (user_id, name);

ALTER TABLE subdomains
    ADD CONSTRAINT FK_SUBDOMAINS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);