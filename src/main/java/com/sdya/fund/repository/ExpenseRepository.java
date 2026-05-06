package com.sdya.fund.repository;

import com.sdya.fund.entity.*;
import org.springframework.data.jpa.repository.*;
import java.math.BigDecimal;
import java.util.*;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("select coalesce(sum(e.amount),0) from Expense e")
    BigDecimal totalExpense();

    @Query("select coalesce(sum(e.amount),0) from Expense e where e.event.id = :eventId")
    BigDecimal totalByEvent(Long eventId);

    @Query("select coalesce(sum(e.amount),0) from Expense e where e.paymentMode = :mode")
    BigDecimal totalExpenseByMode(PaymentMode mode);

    List<Expense> findTop10ByOrderByExpenseDateDesc();
    List<Expense> findByPaymentModeOrderByExpenseDateDesc(PaymentMode mode);

    @Query("select e.category, coalesce(sum(e.amount),0) from Expense e group by e.category order by 2 desc")
    List<Object[]> categoryTotals();
}
