package com.agpf.finance.hub.services.expense;

import com.agpf.finance.hub.dtos.expense.ExpenseRegisterDTO;
import com.agpf.finance.hub.dtos.expense.OutputExpenseDTO;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.repositories.expense.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public void register(ExpenseRegisterDTO dto, User user) {
        var entity = ExpenseRegisterDTO.toEntity(dto, user);

        expenseRepository.save(entity);
    }

    public List<OutputExpenseDTO> byUser(User user) {
        return expenseRepository.findByUser(user);
    }
}
