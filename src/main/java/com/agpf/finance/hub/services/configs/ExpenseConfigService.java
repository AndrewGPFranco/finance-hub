package com.agpf.finance.hub.services.configs;

import com.agpf.finance.hub.dtos.configs.ExpenseConfigRegisterDTO;
import com.agpf.finance.hub.exceptions.NotFoundException;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.repositories.configs.ExpenseConfigRepository;
import com.agpf.finance.hub.repositories.subdomains.SubdomainRepository;
import com.agpf.finance.hub.utils.CrudUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

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

    public boolean verificaSeUsuarioPossuiConfig(User user, UUID idSubdominio, LocalDate dataUso) {
        return configRepository.possuiConfig(user.getId(), idSubdominio, dataUso);
    }

    public Optional<ExpenseConfigRegisterDTO> getConfigDoUsuarioESubdominio(User user, UUID idSubdominio, LocalDate dataUso) {
        var config = configRepository.buscarConfigPeloSubdominioEMes(idSubdominio, user.getId(), dataUso);

        if (config.isEmpty()) return Optional.empty();

        return Optional.of(ExpenseConfigRegisterDTO.fromEntity(config.get(), idSubdominio));
    }

    @Transactional
    public void atualizaConfig(@Valid ExpenseConfigRegisterDTO dto, User user) {
        var config = configRepository.buscarConfigPeloSubdominioEMes(dto.idSubdominio(), user.getId(),
                dto.dataDeUso().atDay(1)).orElseThrow(() -> new NotFoundException("Configuração não encontrada!"));

        CrudUtils.updateField(dto.dataDeUso(), config::setDateToUse);
        CrudUtils.updateField(dto.dataDoPagamento(), config::setPaymentDate);
        CrudUtils.updateField(dto.valor(), config::setAmount);
        CrudUtils.updateField(dto.dataDeVencimento(), config::setDueDate);
        CrudUtils.updateField(dto.status(), config::setStatus);
        CrudUtils.updateField(dto.categoria(), config::setCategory);
        CrudUtils.updateField(dto.metodoDoPagamento(), config::setPaymentMethod);

        configRepository.save(config);
    }
}
