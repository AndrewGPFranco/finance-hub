create table if not exists wallets
(
    id           uuid                        not null,
    user_id      uuid                        not null,
    subdomain_id uuid                        not null,
    balance      decimal(12, 2)              not null,
    name         varchar(25)                 not null,
    created_at   timestamp without time zone not null,
    updated_at   timestamp without time zone,
    constraint pk_wallets primary key (id),
    constraint UK_WALLET_SUBDOMAIN unique (subdomain_id),
    constraint ck_wallets_balance_positive_or_zero check (balance >= 0)
);

alter table wallets
    add constraint fk_wallets_user_id foreign key (user_id) references users (id);

alter table wallets
    add constraint fk_wallets_subdomain_id
        foreign key (subdomain_id) references subdomains (id) on delete cascade;

create index IDX_WALLET_USER on wallets (user_id);