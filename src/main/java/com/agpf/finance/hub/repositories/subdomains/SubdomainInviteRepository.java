package com.agpf.finance.hub.repositories.subdomains;

import com.agpf.finance.hub.dtos.subdomain.invite.OutputInvitationDTO;
import com.agpf.finance.hub.models.subdomain.SubdomainInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubdomainInviteRepository extends JpaRepository<SubdomainInvite, UUID> {

    @Query(
            """
                    select new com.agpf.finance.hub.dtos.subdomain.invite.OutputInvitationDTO(
                        si.token, si.invitedBy.username, si.expiresAt, si.subdomain.name
                    ) from SubdomainInvite si where si.status = 'PENDING' and si.emailGuest = :email
                    """
    )
    List<OutputInvitationDTO> checkInvitations(@Param("email") String email);

}
