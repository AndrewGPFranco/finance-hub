package com.agpf.finance.hub.dtos.wallet;

import java.math.BigDecimal;
import java.util.UUID;

public record OutputWalletDTO(
        UUID id,
        String name,
        BigDecimal balance,
        UUID subdomainId,
        String subdomainName
) {
}
