package com.agpf.finance.hub.repositories.subdomains;

import com.agpf.finance.hub.dtos.subdomain.OutputSubdomainDTO;
import com.agpf.finance.hub.enums.subdomain.PermissionSubdomainType;
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

    @Query("""
                select distinct s
                from Subdomain s
                left join s.subdomainMembers sm with sm.user = :user and sm.ativo = true
                where s.id = :id
                  and (
                    s.user = :user
                    or sm.user = :user
                  )
            """)
    Optional<Subdomain> findByIdAndUser(@Param("id") UUID id, @Param("user") User user);

    @Query("""
                select
                    case when s.user = :user then :ownerPermission else sm.permission end
                from Subdomain s
                left join s.subdomainMembers sm with sm.user = :user and sm.ativo = true
                where s.id = :id
                  and (
                    s.user = :user
                    or sm.user = :user
                  )
            """)
    Optional<PermissionSubdomainType> findPermissionByIdAndUser(@Param("id") UUID id,
                                                               @Param("user") User user,
                                                               @Param("ownerPermission") PermissionSubdomainType ownerPermission);

    @Query("""
                select distinct new com.agpf.finance.hub.dtos.subdomain.OutputSubdomainDTO(
                    case when s.user.id = :idUser then true else false end,
                    s.urlPhoto,
                    s.id,
                    s.name,
                    case when s.user.id = :idUser then :ownerPermission else sm.permission end
                )
                from Subdomain s
                left join s.subdomainMembers sm with sm.user.id = :idUser and sm.ativo = true
                where s.user.id = :idUser or sm.user.id = :idUser
            """)
    List<OutputSubdomainDTO> subdomainsByUser(@Param("idUser") UUID idUser,
                                              @Param("ownerPermission") PermissionSubdomainType ownerPermission);
}
