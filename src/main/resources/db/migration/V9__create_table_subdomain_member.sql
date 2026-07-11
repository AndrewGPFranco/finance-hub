CREATE TABLE IF NOT EXISTS subdomain_member
(
    id           UUID                        NOT NULL,
    subdomain_id UUID                        NOT NULL,
    user_id      UUID                        NOT NULL,
    ativo        BOOLEAN                     NOT NULL,
    permission   VARCHAR(6)                  NOT NULL CHECK (permission in ('EDITOR', 'VIEWER')),
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_subdomain_member PRIMARY KEY (id)
);

ALTER TABLE subdomain_member
    ADD CONSTRAINT FK_SUBDOMAIN_MEMBER_SUBDOMAIN FOREIGN KEY (subdomain_id) references subdomains (id);

ALTER TABLE subdomain_member
    ADD CONSTRAINT FK_SUBDOMAIN_MEMBER_USER FOREIGN KEY (user_id) references users (id);