package com.agpf.finance.hub.repositories.configs;

import com.agpf.finance.hub.models.configurations.ExpenseConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseConfigRepository extends JpaRepository<ExpenseConfig, UUID> {

    @Query(value = """
            select *
              from expense_configs ec
             where ec.subdomain_id = :subdominio
               and ec.user_id = :user
               and ec.date_to_use = :dataDeUso
            """, nativeQuery = true
    )
    Optional<ExpenseConfig> buscarConfigPeloSubdominioEMes(@Param("subdominio") UUID idSubdominio,
                                                           @Param("user") UUID user,
                                                           @Param("dataDeUso") LocalDate dataDeUso);

    @Query(value = """
            select exists
               (select 1 from expense_configs ec 
               where ec.subdomain_id = :idSubdominio and ec.user_id = :idUsuario and ec.date_to_use = :dataDeUso
                           )
            """, nativeQuery = true)
    boolean possuiConfig(@Param("idUsuario") UUID idUsuario, @Param("idSubdominio") UUID idSubdominio, @Param("dataDeUso") LocalDate dataUso);
}
