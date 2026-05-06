package com.sdya.fund.repository;

import com.sdya.fund.entity.*;
import org.springframework.data.jpa.repository.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    @Query("select coalesce(sum(d.amount),0) from Donation d")
    BigDecimal totalDonation();

    @Query("select coalesce(sum(d.amount),0) from Donation d where d.paymentStatus = com.sdya.fund.entity.PaymentStatus.PAID")
    BigDecimal totalPaidDonation();

    @Query("select coalesce(sum(d.amount),0) from Donation d where d.paymentStatus = com.sdya.fund.entity.PaymentStatus.DUE")
    BigDecimal totalDueDonation();

    @Query("select coalesce(sum(d.amount),0) from Donation d where d.event.id = :eventId")
    BigDecimal totalByEvent(Long eventId);

    @Query("select coalesce(sum(d.amount),0) from Donation d where d.paymentStatus = com.sdya.fund.entity.PaymentStatus.PAID and d.paymentMode = :mode")
    BigDecimal totalPaidByMode(PaymentMode mode);

    List<Donation> findTop10ByOrderByDonatedAtDesc();
    Optional<Donation> findByReceiptNo(String receiptNo);
    List<Donation> findByPaymentStatusOrderByPromisedDateAsc(PaymentStatus status);
    List<Donation> findByPaymentStatusAndPaymentModeOrderByDonatedAtDesc(PaymentStatus status, PaymentMode mode);

    @Query("select d.event.name, coalesce(sum(d.amount),0) from Donation d group by d.event.name order by 2 desc")
    List<Object[]> eventTotals();

    @Query("select function('date',d.donatedAt), coalesce(sum(d.amount),0) from Donation d where d.donatedAt >= :from group by function('date',d.donatedAt) order by 1")
    List<Object[]> dailyTotals(LocalDateTime from);
}
