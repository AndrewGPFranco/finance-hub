package com.agpf.finance.hub.services.wallet;

import com.agpf.finance.hub.dtos.wallet.InputWalletDTO;
import com.agpf.finance.hub.dtos.wallet.OutputWalletDTO;
import com.agpf.finance.hub.exceptions.BusinessException;
import com.agpf.finance.hub.exceptions.NotFoundException;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.repositories.subdomains.SubdomainRepository;
import com.agpf.finance.hub.repositories.wallet.WalletRepository;
import com.agpf.finance.hub.services.subdomain.SubdomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final SubdomainRepository subdomainRepository;
    private final SubdomainService subdomainService;

    public Optional<OutputWalletDTO> byUserAndSubdomain(User user, UUID idSubdomain) {
        return walletRepository.findAccessibleByUserAndSubdomain(user, idSubdomain);
    }

    public void register(User user, InputWalletDTO input) {
        if (!subdomainService.canManage(user, input.idSubdomain()))
            throw new BusinessException("Você não tem permissão para cadastrar carteira neste subdomínio.");

        var subdomain = subdomainRepository.findByIdAndUser(input.idSubdomain(), user)
                .orElseThrow(() -> new NotFoundException("Subdomínio não encontrado!"));

        var entity = InputWalletDTO.toEntity(input, subdomain.getUser(), subdomain);

        walletRepository.save(entity);
    }

    public UUID delete(UUID idWallet, User user) {
        var wallet = walletRepository.findAccessibleEntityByIdAndUser(idWallet, user)
                .orElseThrow(() -> new NotFoundException("Carteira não encontrada!"));

        if (!subdomainService.canManage(user, wallet.getSubdomain().getId()))
            throw new BusinessException("Você não tem permissão para excluir carteira neste subdomínio.");

        var idSubdomain = wallet.getSubdomain().getId();
        walletRepository.delete(wallet);

        return idSubdomain;
    }
}
