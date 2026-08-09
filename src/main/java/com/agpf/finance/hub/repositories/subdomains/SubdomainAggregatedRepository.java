package com.agpf.finance.hub.repositories.subdomains;

import com.agpf.finance.hub.models.subdomain.SubdomainAggregated;
import com.agpf.finance.hub.models.subdomain.Subdomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubdomainAggregatedRepository extends JpaRepository<SubdomainAggregated, UUID> {

    List<SubdomainAggregated> findBySubdomainTarget(Subdomain subdominioAlvo);

}
