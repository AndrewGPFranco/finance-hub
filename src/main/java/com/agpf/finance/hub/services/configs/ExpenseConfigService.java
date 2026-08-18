package com.agpf.finance.hub.services.configs;

import com.agpf.finance.hub.dtos.configs.ExpenseConfigRegisterDTO;
import com.agpf.finance.hub.exceptions.NotFoundException;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.repositories.configs.ExpenseConfigRepository;
import com.agpf.finance.hub.repositories.subdomains.SubdomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseConfigService {

    private final SubdomainRepository subdomainRepository;
    private final ExpenseConfigRepository configRepository;

    @Transactional
    public void registrarConfig(ExpenseConfigRegisterDTO dto, User user) {
        var subdominio = subdomainRepository.findByIdAndUser(dto.idSubdominio(), user)
                .orElseThrow(() -> new NotFoundException("Subdomínio não encontrado!"));

        var config = dto.toEntity(user, subdominio);

        configRepository.save(config);
    }
}
