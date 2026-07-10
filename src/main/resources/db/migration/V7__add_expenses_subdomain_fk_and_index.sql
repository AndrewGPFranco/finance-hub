alter table expenses
    add constraint FK_EXPENSES_SUBDOMAIN foreign key (subdomain_id) references subdomains (id);

create index idx_expenses_user_subdomain on expenses (user_id, subdomain_id);
