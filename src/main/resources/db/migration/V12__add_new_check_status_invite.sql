alter table subdomain_invite drop constraint subdomain_invite_status_check;

ALTER TABLE subdomain_invite
    ALTER COLUMN status TYPE VARCHAR(8),
    ALTER COLUMN status SET NOT NULL,
    ADD CONSTRAINT ck_subdomain_invite_status
        CHECK (status IN ('PENDING', 'ACCEPTED', 'EXPIRED', 'REVOKED', 'DECLINED'));