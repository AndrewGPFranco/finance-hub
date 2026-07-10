package com.agpf.finance.hub.repositories.subdomains;

import com.agpf.finance.hub.dtos.subdomain.OutputSubdomain;
import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubdomainRepository extends JpaRepository<Subdomain, UUID> {

    Optional<Subdomain> findByNameAndUser(String name, User user);

    Optional<Subdomain> findByIdAndUser(UUID id, User user);

    @Query("""
            select new com.agpf.finance.hub.dtos.subdomain.OutputSubdomain(
                s.urlPhoto, s.id, s.name
            ) from Subdomain s where s.user.id = :idUser
            """)
    List<OutputSubdomain> subdomainsByUser(@Param("idUser") UUID idUser);
}
