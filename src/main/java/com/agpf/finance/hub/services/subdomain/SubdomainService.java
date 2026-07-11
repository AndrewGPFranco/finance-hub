package com.agpf.finance.hub.services.subdomain;

import com.agpf.finance.hub.dtos.subdomain.EditSubdomainDTO;
import com.agpf.finance.hub.dtos.subdomain.OutputSubdomainDTO;
import com.agpf.finance.hub.dtos.subdomain.RegisterSubdomainDTO;
import com.agpf.finance.hub.enums.subdomain.PermissionSubdomainType;
import com.agpf.finance.hub.exceptions.BusinessException;
import com.agpf.finance.hub.exceptions.NotFoundException;
import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.repositories.subdomains.SubdomainRepository;
import com.agpf.finance.hub.utils.CrudUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubdomainService {

    private final SubdomainRepository subdomainRepository;

    @Transactional
    public void register(RegisterSubdomainDTO dto, User user) {
        var subdomainOptional = subdomainRepository.findByNameAndUser(dto.name(), user);

        if (subdomainOptional.isPresent())
            throw new BusinessException("Já há um subdomínio com o nome informado!");

        var entity = RegisterSubdomainDTO.toEntity(dto, user);
        subdomainRepository.save(entity);
    }

    public List<OutputSubdomainDTO> subdomainsByUser(User user) {
        return subdomainRepository.subdomainsByUser(user.getId(), PermissionSubdomainType.EDITOR);
    }

    public boolean canManage(User user, UUID idSubdomain) {
        if (idSubdomain == null)
            return false;

        return subdomainRepository.findPermissionByIdAndUser(idSubdomain, user, PermissionSubdomainType.EDITOR)
                .filter(PermissionSubdomainType.EDITOR::equals).isPresent();
    }

    public UUID resolveSelectedSubdomainId(User user, UUID idSubdomain) {
        if (idSubdomain != null && subdomainRepository.findByIdAndUser(idSubdomain, user).isPresent())
            return idSubdomain;

        var subdomains = subdomainsByUser(user);

        if (subdomains.isEmpty())
            return null;

        return subdomains.stream().findFirst().map(OutputSubdomainDTO::id)
                .orElseThrow(() -> new NotFoundException("Subdomain não encontrado!"));
    }

    public OutputSubdomainDTO getSubdomainByIdAndUser(UUID idSubdomain, User user) {
        return OutputSubdomainDTO.fromEntity(resolve(user, idSubdomain), true);
    }

    @Transactional
    public void edit(EditSubdomainDTO dto, UUID idSubdomain, User user) {
        var subdomain = resolve(user, idSubdomain);

        CrudUtils.updateField(dto.name(), subdomain::setName);
        CrudUtils.updateField(dto.urlPhoto(), subdomain::setUrlPhoto);

        subdomainRepository.save(subdomain);
    }

    private Subdomain resolve(User user, UUID idSubdomain) {
        if (idSubdomain == null)
            throw new NotFoundException("Subdomínio não encontrado!");

        return subdomainRepository.findByIdAndUser(idSubdomain, user)
                .orElseThrow(() -> new NotFoundException("Subdomínio não encontrado!"));
    }
}
