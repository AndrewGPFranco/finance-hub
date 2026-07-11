package com.agpf.finance.hub.services.subdomain;

import com.agpf.finance.hub.dtos.subdomain.invite.OutputInvitationDTO;
import com.agpf.finance.hub.dtos.subdomain.invite.RegisterSubdomainInviteDTO;
import com.agpf.finance.hub.dtos.subdomain.invite.ResponseInvitationDTO;
import com.agpf.finance.hub.enums.subdomain.ResponseInvitationType;
import com.agpf.finance.hub.enums.subdomain.StatusInviteSubdomain;
import com.agpf.finance.hub.exceptions.NotFoundException;
import com.agpf.finance.hub.models.subdomain.SubdomainInvite;
import com.agpf.finance.hub.models.subdomain.SubdomainMember;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.repositories.subdomains.SubdomainInviteRepository;
import com.agpf.finance.hub.repositories.subdomains.SubdomainMemberRepository;
import com.agpf.finance.hub.repositories.subdomains.SubdomainRepository;
import com.agpf.finance.hub.repositories.user.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubdomainInviteService {

    private final UserRepository userRepository;
    private final SubdomainRepository subdomainRepository;
    private final SubdomainInviteRepository subdomainInviteRepository;
    private final SubdomainMemberRepository subdomainMemberRepository;

    @Transactional
    public void invite(RegisterSubdomainInviteDTO dto, User user) {
        var subdomain = subdomainRepository.findByIdAndUser(dto.idSubdomain(), user)
                .orElseThrow(() -> new NotFoundException("Subdomínio não encontrado!"));

        userRepository.findByEmail(dto.emailGuest())
                .orElseThrow(() -> new NotFoundException("O email informado não pertence a um usuário do sistema!"));

        var entity = RegisterSubdomainInviteDTO.toEntity(dto, user, subdomain);

        subdomainInviteRepository.save(entity);
    }

    public List<OutputInvitationDTO> checkInvitations(User user) {
        return subdomainInviteRepository.checkInvitations(user.getEmail());
    }

    @Transactional
    public void responseInvitation(ResponseInvitationDTO response, User user) {
        var invitation = subdomainInviteRepository.findByTokenAndEmail(response.token(), user.getEmail());

        if (invitation == null) throw new NotFoundException("Convite não encontrado!");

        var status = getResponse(response.response());

        invitation.setStatus(status);
        invitation.setAnsweredAt(Instant.now());

        if (status.equals(StatusInviteSubdomain.ACCEPTED)) {
            var member = SubdomainMember.builder()
                    .subdomain(invitation.getSubdomain()).user(user)
                    .ativo(true).permission(invitation.getPermission()).build();

            var savedMember = subdomainMemberRepository.save(member);

            invitation.getSubdomain().getSubdomainMembers().add(savedMember);

            subdomainInviteRepository.save(invitation);
        }
    }

    private @NotNull(message = "O status é obrigatório.") StatusInviteSubdomain getResponse(@NotNull ResponseInvitationType response) {
        return ResponseInvitationType.ACCEPT.equals(response) ? StatusInviteSubdomain.ACCEPTED : StatusInviteSubdomain.DECLINED;
    }
}
