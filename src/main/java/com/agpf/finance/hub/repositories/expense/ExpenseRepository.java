package com.agpf.finance.hub.repositories.expense;

import com.agpf.finance.hub.dtos.expense.OutputExpenseDTO;
import com.agpf.finance.hub.enums.subdomain.PermissionSubdomainType;
import com.agpf.finance.hub.models.expense.Expense;
import com.agpf.finance.hub.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    @Query("""
            select distinct new com.agpf.finance.hub.dtos.expense.OutputExpenseDTO(
                e.id, e.title, e.amount, e.dueDate, e.paymentDate,
                            e.status, e.category, e.paymentMethod, e.recurring,
                                        e.installmentNumber, e.totalInstallments
             )
             from Expense e
             left join e.subdomain.subdomainMembers sm
             where e.month = :month
               and e.subdomain.id = :subdomainId
               and (
                    e.subdomain.user = :user
                    or (sm.user = :user and sm.ativo = true)
               )
            """
    )
    List<OutputExpenseDTO> findByUserAndSubdomainId(@Param("user") User user,
                                                    @Param("subdomainId") UUID subdomainId,
                                                    Sort sort,
                                                    @Param("month") Month month);

    @Query("""
            select distinct new com.agpf.finance.hub.dtos.expense.OutputExpenseDTO(
                e.id, e.title, e.amount, e.dueDate, e.paymentDate,
                            e.status, e.category, e.paymentMethod, e.recurring,
                                        e.installmentNumber, e.totalInstallments
             )
             from Expense e
             left join e.subdomain.subdomainMembers sm
             where e.month = :month
               and e.subdomain.id = :subdomainId
               and (
                    e.subdomain.user = :user
                    or (sm.user = :user and sm.ativo = true)
               )
            """
    )
    List<OutputExpenseDTO> findByUserAndSubdomainId(@Param("user") User user,
                                                    @Param("subdomainId") UUID subdomainId, @Param("month") Month month);

    @Query("""
                select distinct e
                from Expense e
                left join e.subdomain.subdomainMembers sm with sm.user = :user and sm.ativo = true
                where e.id = :idExpense
                  and (
                    e.subdomain.user = :user
                    or sm.user = :user
                  )
            """)
    Optional<Expense> findAccessibleByIdAndUser(@Param("idExpense") UUID idExpense, @Param("user") User user);

    @Query("""
                select distinct e
                from Expense e
                left join e.subdomain.subdomainMembers sm with sm.user = :user and sm.ativo = true
                where e.id = :idExpense
                  and (
                    e.subdomain.user = :user
                    or (sm.user = :user and sm.permission = :editorPermission)
                  )
            """)
    Optional<Expense> findManageableByIdAndUser(@Param("idExpense") UUID idExpense,
                                                @Param("user") User user,
                                                @Param("editorPermission") PermissionSubdomainType editorPermission);

    @Query("""
                select distinct e
                from Expense e
                left join e.subdomain.subdomainMembers sm with sm.user = :user and sm.ativo = true
                where e.id in :ids
                  and e.subdomain.id = :subdomainId
                  and (
                    e.subdomain.user = :user
                    or (sm.user = :user and sm.permission = :editorPermission)
                  )
            """)
    List<Expense> findAllManageableByIdAndSubdomainIdAndUser(@Param("ids") List<UUID> ids,
                                                             @Param("subdomainId") UUID subdomainId,
                                                             @Param("user") User user,
                                                             @Param("editorPermission") PermissionSubdomainType editorPermission);

    Page<Expense> findAllByDueDate(LocalDate dueDate, Pageable pageable);

}
