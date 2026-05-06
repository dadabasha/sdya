package com.sdya.fund.service;

import com.sdya.fund.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DonationRepository donations;
    private final ExpenseRepository expenses;
    private final EventRepository events;
    private final AuctionRepository auctions;
    private final FundSettingsRepository settings;

    public Map<String,Object> data() {
        BigDecimal lastYear = settings.findById(1L).map(s -> s.getLastYearAmountInHand()).orElse(BigDecimal.ZERO);
        BigDecimal received = donations.totalPaidDonation();
        BigDecimal auctionReceived = auctions.totalPaidAuction();
        BigDecimal spent = expenses.totalExpense();
        BigDecimal currentYearInHand = received.add(auctionReceived).subtract(spent);
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("lastYearAmount", lastYear);
        m.put("received", received);
        m.put("auctionReceived", auctionReceived);
        m.put("grandReceived", received.add(auctionReceived));
        m.put("due", donations.totalDueDonation());
        m.put("auctionDue", auctions.totalDueAuction());
        m.put("spent", spent);
        m.put("balance", currentYearInHand);
        m.put("totalInHand", lastYear.add(currentYearInHand));
        m.put("eventCount", events.count());
        m.put("donationCount", donations.count());
        m.put("auctionCount", auctions.count());
        m.put("recentDonations", donations.findTop10ByOrderByDonatedAtDesc());
        m.put("recentExpenses", expenses.findTop10ByOrderByExpenseDateDesc());
        m.put("recentAuctions", auctions.findTop10ByOrderByAuctionDateDesc());
        m.put("eventTotals", donations.eventTotals());
        m.put("expenseTotals", expenses.categoryTotals());
        m.put("auctionTotals", auctions.eventAuctionTotals());
        m.put("dailyTotals", donations.dailyTotals(LocalDateTime.now().minusDays(30)));
        return m;
    }
}
