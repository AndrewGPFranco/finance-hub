create table if not exists subdomain_invite
(
    id            UUID                        not null,
    invited_by_id UUID                        not null,
    subdomain_id  UUID                        not null,
    email_guest   VARCHAR(255)                NOT NULL,
    status        VARCHAR(8)                  NOT NULL CHECK (status in ('PENDING', 'ACCEPTED', 'EXPIRED', 'REVOKED')),
    created_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at    TIMESTAMP WITHOUT TIME ZONE,
    token         UUID                        NOT NULL,
    expires_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    accepted_at   TIMESTAMP WITHOUT TIME ZONE,
    permission    VARCHAR(6)                  NOT NULL CHECK (permission in ('EDITOR', 'VIEWER')),
    CONSTRAINT pk_subdomain_invite PRIMARY KEY (id)
);

alter table subdomain_invite
    add constraint FK_INVITED_BY_ID_SUBDOMAIN_INVITE foreign key (invited_by_id) references users (id);
alter table subdomain_invite
    add constraint FK_SUBDOMAIN_ID_SUBDOMAIN_INVITE foreign key (subdomain_id) references subdomains (id);