package com.agpf.finance.hub.repositories.subdomains;

import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.subdomain.SubdomainAggregated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubdomainAggregatedRepository extends JpaRepository<SubdomainAggregated, UUID> {

    List<SubdomainAggregated> findBySubdomainTarget(Subdomain subdominioAlvo);

    @Query(value = """
            SELECT EXISTS (
                        SELECT 1
                        FROM subdomain_aggregates sa
                        WHERE (sa.subdomain_id = :primeiroSub
                               AND sa.subdomain_aggregate_id = :segundoSub
                               AND sa.date_to_use = :dateToUse)
                           OR (sa.subdomain_id = :segundoSub
                               AND sa.subdomain_aggregate_id = :primeiroSub
                               AND sa.date_to_use = :dateToUse)
                        )
            """, nativeQuery = true)
    boolean verificaSeJaHaVinculoEntreSubdominios(@Param("primeiroSub") UUID primeiroSubId,
                                                  @Param("segundoSub") UUID segundoSubId,
                                                  @Param("dateToUse") LocalDate dateToUse);

    @Query(value = """
            SELECT *
            FROM subdomain_aggregates sa
            WHERE ((sa.subdomain_id = :primeiroSub AND sa.subdomain_aggregate_id = :segundoSub)
               OR (sa.subdomain_id = :segundoSub AND sa.subdomain_aggregate_id = :primeiroSub))
              AND sa.date_to_use = :dateToUse
            """, nativeQuery = true)
    Optional<SubdomainAggregated> obterAssociacaoEntreSubdominios(@Param("primeiroSub") UUID primeiroSubId,
                                                                  @Param("segundoSub") UUID segundoSubId,
                                                                  @Param("dateToUse") LocalDate dateToUse);

}
