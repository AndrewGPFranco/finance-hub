CREATE TABLE subdomain_aggregates
(
    id                     UUID                           NOT NULL,
    subdomain_id           UUID                           NOT NULL,
    subdomain_aggregate_id UUID                           NOT NULL,
    created_at             TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    updated_at             TIMESTAMP(6) WITHOUT TIME ZONE,
    CONSTRAINT pk_subdomain_aggregates PRIMARY KEY (id)
);

ALTER TABLE subdomain_aggregates
    ADD CONSTRAINT uk_subdomain_target_and_aggregate UNIQUE (subdomain_id, subdomain_aggregate_id);

ALTER TABLE subdomain_aggregates
    ADD CONSTRAINT FK_SUBDOMAIN_AGGREGATES_ON_SUBDOMAIN FOREIGN KEY (subdomain_id) REFERENCES subdomains (id);

CREATE INDEX idx_subdomain_target ON subdomain_aggregates (subdomain_id);

ALTER TABLE subdomain_aggregates
    ADD CONSTRAINT FK_SUBDOMAIN_AGGREGATES_ON_SUBDOMAIN_AGGREGATE FOREIGN KEY (subdomain_aggregate_id) REFERENCES subdomains (id);

CREATE INDEX idx_subdomain_aggregate ON subdomain_aggregates (subdomain_aggregate_id);