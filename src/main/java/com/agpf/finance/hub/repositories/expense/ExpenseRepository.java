package com.agpf.finance.hub.repositories.expense;

import com.agpf.finance.hub.dtos.expense.OutputExpenseDTO;
import com.agpf.finance.hub.models.expense.Expense;
import com.agpf.finance.hub.models.user.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    @Query("""
            select new com.agpf.finance.hub.dtos.expense.OutputExpenseDTO(
                e.id, e.title, e.amount, e.dueDate, e.paymentDate,
                            e.status, e.category, e.paymentMethod, e.recurring,
                                        e.installmentNumber, e.totalInstallments
             ) from Expense e where e.user = :user and e.month = :month
                and e.subdomain.id = :subdomainId
            """
    )
    List<OutputExpenseDTO> findByUserAndSubdomainId(@Param("user") User user, @Param("subdomainId") UUID subdomainId,
                                                    Sort sort, @Param("month") Month month);

    @Query("""
            select new com.agpf.finance.hub.dtos.expense.OutputExpenseDTO(
                e.id, e.title, e.amount, e.dueDate, e.paymentDate,
                            e.status, e.category, e.paymentMethod, e.recurring,
                                        e.installmentNumber, e.totalInstallments
             ) from Expense e where e.user = :user and e.subdomain.id = :subdomainId and e.month = :month
            """
    )
    List<OutputExpenseDTO> findByUserAndSubdomainId(@Param("user") User user,
                                                    @Param("subdomainId") UUID subdomainId, @Param("month") Month month);

    Expense findByIdAndUser(UUID idExpense, User user);

    @Query("""
                select e
                from Expense e
                where e.id in :ids
                  and e.user = :user
            """)
    List<Expense> findAllByIdAndUser(@Param("ids") List<UUID> ids, @Param("user") User user);
}
