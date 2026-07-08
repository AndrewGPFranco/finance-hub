package com.agpf.finance.hub.services.expense;

import com.agpf.finance.hub.dtos.expense.ExpenseRegisterDTO;
import com.agpf.finance.hub.dtos.expense.FilterListExpenseType;
import com.agpf.finance.hub.dtos.expense.OutputExpenseDTO;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.repositories.expense.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public void register(ExpenseRegisterDTO dto, User user) {
        var entity = ExpenseRegisterDTO.toEntity(dto, user);

        expenseRepository.save(entity);
    }

    public List<OutputExpenseDTO> byUser(User user, FilterListExpenseType filter, Sort.Direction direction) {
        return expenseRepository.findByUser(user, Sort.by(direction, filter.getFieldName()));
    }

    public Map<FilterListExpenseType, String> getPossibleFilters() {
        return Arrays.stream(FilterListExpenseType.values())
                .collect(Collectors.toMap(Function.identity(), f -> f.getDescription()));
    }
}
