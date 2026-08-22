alter table expenses add column date_to_use date;

update expenses set date_to_use = date_trunc('month', expenses.due_date)::date;

alter table expenses alter column date_to_use set not null;

alter table expenses drop column month;
