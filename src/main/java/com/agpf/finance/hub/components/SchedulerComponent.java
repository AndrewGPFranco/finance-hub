package com.agpf.finance.hub.components;

import com.agpf.finance.hub.dtos.expense.ExpenseRegisterDTO;
import com.agpf.finance.hub.models.expense.Expense;
import com.agpf.finance.hub.repositories.expense.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static com.agpf.finance.hub.utils.DateUtils.getLocalDateAmericaSP;

@Component
@RequiredArgsConstructor
public class SchedulerComponent {

    private final ExpenseRepository expenseRepository;

    /**
     * Esse schedule ficará inativo no momento até realizarmos o deploy para produção.
     */
    @Transactional
//    @Scheduled(cron = "0 0 1 * *")
    public void scheduledTask() {
        var date = getLocalDateAmericaSP().minusDays(1);

        var request = PageRequest.of(0, 20);
        var expensesByDate = expenseRepository.findAllByDueDate(date, request);

        var listExpensesToUpdate = new ArrayList<Expense>();

        for (var expense : expensesByDate) {
            if (expense.isRecurring()) {
                var dto = ExpenseRegisterDTO.cloneNextMonthFromEntity(expense);
                var entity = ExpenseRegisterDTO.toEntity(dto, expense.getUser(), expense.getSubdomain());
                listExpensesToUpdate.add(entity);
            }
        }

        expenseRepository.saveAll(listExpensesToUpdate);
    }

}
