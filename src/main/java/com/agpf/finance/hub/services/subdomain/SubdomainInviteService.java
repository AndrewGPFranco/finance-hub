package com.agpf.finance.hub.services.subdomain;

import com.agpf.finance.hub.dtos.subdomain.invite.RegisterSubdomainInviteDTO;
import com.agpf.finance.hub.exceptions.NotFoundException;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.repositories.subdomains.SubdomainInviteRepository;
import com.agpf.finance.hub.repositories.subdomains.SubdomainRepository;
import com.agpf.finance.hub.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubdomainInviteService {

    private final UserRepository userRepository;
    private final SubdomainRepository subdomainRepository;
    private final SubdomainInviteRepository subdomainInviteRepository;

    @Transactional
    public void invite(RegisterSubdomainInviteDTO dto, User user) {
        var subdomain = subdomainRepository.findByIdAndUser(dto.idSubdomain(), user)
                .orElseThrow(() -> new NotFoundException("Subdomínio não encontrado!"));

        userRepository.findByEmail(dto.emailGuest())
                .orElseThrow(() -> new NotFoundException("O email informado não pertence a um usuário do sistema!"));

        var entity = RegisterSubdomainInviteDTO.toEntity(dto, user, subdomain);

        subdomainInviteRepository.save(entity);
    }

}
