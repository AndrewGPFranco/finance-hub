package com.agpf.finance.hub.dtos.wallet;

import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.models.wallet.Wallet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record InputWalletDTO(
        @NotNull(message = "É necessário informar o subdomínio.") UUID idSubdomain,
        @NotBlank(message = "É necessário informar um nome a carteira.") String name,
        @PositiveOrZero(message = "A quantidade deve ser 0 ou positivo.") BigDecimal balance
) {

    public static Wallet toEntity(InputWalletDTO input, User user, Subdomain subdomain) {
        return Wallet.builder().name(input.name()).user(user)
                .subdomain(subdomain).balance(input.balance()).build();
    }

}
