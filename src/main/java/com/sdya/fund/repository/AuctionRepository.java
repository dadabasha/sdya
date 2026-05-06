package com.sdya.fund.repository;

import com.sdya.fund.entity.*;
import org.springframework.data.jpa.repository.*;
import java.math.BigDecimal;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findTop10ByOrderByAuctionDateDesc();
    List<Auction> findByStatusOrderByAuctionDateDesc(AuctionStatus status);
    List<Auction> findByStatusAndPaymentModeOrderByAuctionDateDesc(AuctionStatus status, PaymentMode mode);

    @Query("select coalesce(sum(a.winningAmount),0) from Auction a where a.status = com.sdya.fund.entity.AuctionStatus.PAID")
    BigDecimal totalPaidAuction();

    @Query("select coalesce(sum(a.winningAmount),0) from Auction a where a.status = com.sdya.fund.entity.AuctionStatus.DUE")
    BigDecimal totalDueAuction();

    @Query("select coalesce(sum(a.winningAmount),0) from Auction a where a.status = com.sdya.fund.entity.AuctionStatus.PAID and a.paymentMode = :mode")
    BigDecimal totalPaidByMode(PaymentMode mode);

    @Query("select coalesce(sum(a.winningAmount),0) from Auction a where a.status <> com.sdya.fund.entity.AuctionStatus.CANCELLED")
    BigDecimal totalAuction();

    @Query("select a.event.name, coalesce(sum(a.winningAmount),0) from Auction a where a.status <> com.sdya.fund.entity.AuctionStatus.CANCELLED group by a.event.name order by 2 desc")
    List<Object[]> eventAuctionTotals();
}
