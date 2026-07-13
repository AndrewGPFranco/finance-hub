package com.agpf.finance.hub.repositories.wallet;

import com.agpf.finance.hub.dtos.wallet.OutputWalletDTO;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.models.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    @Query("""
                select distinct new com.agpf.finance.hub.dtos.wallet.OutputWalletDTO(
                    w.id,
                    w.name,
                    w.balance,
                    w.subdomain.id,
                    w.subdomain.name
                )
                from Wallet w
                left join w.subdomain.subdomainMembers sm with sm.user = :user and sm.ativo = true
                where w.subdomain.id = :idSubdomain
                  and (
                    w.user = :user
                    or sm.user = :user
                  )
            """)
    Optional<OutputWalletDTO> findAccessibleByUserAndSubdomain(@Param("user") User user,
                                                               @Param("idSubdomain") UUID idSubdomain);

    @Query("""
                select distinct w
                from Wallet w
                left join w.subdomain.subdomainMembers sm with sm.user = :user and sm.ativo = true
                where w.id = :idWallet
                  and (
                    w.user = :user
                    or sm.user = :user
                  )
            """)
    Optional<Wallet> findAccessibleEntityByIdAndUser(@Param("idWallet") UUID idWallet,
                                                     @Param("user") User user);
}
