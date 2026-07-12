alter table expenses
    drop constraint FK_EXPENSES_SUBDOMAIN;

alter table expenses
    add constraint FK_EXPENSES_SUBDOMAIN
        foreign key (subdomain_id) references subdomains (id) on delete cascade;

alter table subdomain_member
    drop constraint FK_SUBDOMAIN_MEMBER_SUBDOMAIN;

alter table subdomain_member
    add constraint FK_SUBDOMAIN_MEMBER_SUBDOMAIN
        foreign key (subdomain_id) references subdomains (id) on delete cascade;

alter table subdomain_invite
    drop constraint FK_SUBDOMAIN_ID_SUBDOMAIN_INVITE;

alter table subdomain_invite
    add constraint FK_SUBDOMAIN_ID_SUBDOMAIN_INVITE
        foreign key (subdomain_id) references subdomains (id) on delete cascade;
