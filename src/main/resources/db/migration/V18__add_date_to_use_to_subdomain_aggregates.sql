ALTER TABLE subdomain_aggregates ADD COLUMN date_to_use DATE;

UPDATE subdomain_aggregates SET date_to_use = DATE_TRUNC('month', created_at)::DATE;

ALTER TABLE subdomain_aggregates ALTER COLUMN date_to_use SET NOT NULL;

ALTER TABLE subdomain_aggregates DROP CONSTRAINT uk_subdomain_target_and_aggregate;

ALTER TABLE subdomain_aggregates
    ADD CONSTRAINT uk_subdomain_target_aggregate_and_date
        UNIQUE (subdomain_id, subdomain_aggregate_id, date_to_use);
