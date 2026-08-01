package com.agpf.finance.hub.dtos.subdomain;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateExpenseSubdomainDTO(
        @NotNull UUID idSubFrom,
        @NotNull UUID idSubTo,
        @NotNull UUID idExpense
) {
}
