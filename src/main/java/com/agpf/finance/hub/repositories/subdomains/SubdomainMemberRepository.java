package com.agpf.finance.hub.repositories.subdomains;

import com.agpf.finance.hub.models.subdomain.SubdomainMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubdomainMemberRepository extends JpaRepository<SubdomainMember, UUID> {
}
