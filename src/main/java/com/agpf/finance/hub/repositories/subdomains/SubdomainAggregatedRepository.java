package com.agpf.finance.hub.repositories.subdomains;

import com.agpf.finance.hub.models.subdomain.AggregatedSubdomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubdomainAggregatedRepository extends JpaRepository<AggregatedSubdomain, UUID> {
}
