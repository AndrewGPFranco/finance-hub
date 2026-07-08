package com.agpf.finance.hub.enums.expense;

import lombok.Getter;

@Getter
public enum FilterListExpenseType {

    TITLE("Título", "title"),
    PAYMENT_DATE("Data do Pagamento", "paymentDate"),
    AMOUNT("Valor", "amount"),
    DUE_DATE("Data de Vencimento", "dueDate"),
    STATUS("Status", "status"),
    CATEGORY("Categoria", "category"),
    PAYMENT_METHOD("Método de Pagamento", "paymentMethod"),
    RECURRING("Recorrente", "recurring"),
    INSTALLMENT_NUMBER("Número da Parcela", "installmentNumber"),
    TOTAL_INSTALLMENTS("Total de Parcelas", "totalInstallments");

    private final String fieldName;
    private final String description;

    FilterListExpenseType(String description, String fieldName) {
        this.description = description;
        this.fieldName = fieldName;
    }
}
