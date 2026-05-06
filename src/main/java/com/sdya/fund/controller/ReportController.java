package com.sdya.fund.controller;

import com.sdya.fund.entity.*;
import com.sdya.fund.repository.*;
import com.sdya.fund.service.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final DonationRepository donations;
    private final ExpenseRepository expenses;
    private final AuctionRepository auctions;
    private final FundSettingsRepository settings;
    private final AccessService access;

    @GetMapping("/reports")
    String reports(Model m, Authentication auth) {
        access.requireReports(auth);
        m.addAttribute("currentUser", access.current(auth));
        m.addAttribute("showDonationReport", access.canDonationReport(auth));
        m.addAttribute("showDueReport", access.canDonationDueReport(auth));
        m.addAttribute("showUpiReport", access.canUpiReport(auth));
        m.addAttribute("showExpenseReport", access.canExpenseReport(auth));
        m.addAttribute("showCashPaidReport", access.canCashPaidReport(auth));
        m.addAttribute("showCashInHandReport", access.canCashInHandReport(auth));
        m.addAttribute("showAuctionReport", access.canAuctionReport(auth));
        BigDecimal lastYear = settings.findById(1L).map(FundSettings::getLastYearAmountInHand).orElse(BigDecimal.ZERO);
        BigDecimal received = donations.totalPaidDonation();
        BigDecimal spent = expenses.totalExpense();
        BigDecimal auctionPaid = auctions.totalPaidAuction();
        BigDecimal auctionDue = auctions.totalDueAuction();
        BigDecimal currentInHand = received.add(auctionPaid).subtract(spent);
        BigDecimal cashCollection = donations.totalPaidByMode(PaymentMode.CASH).add(auctions.totalPaidByMode(PaymentMode.CASH));
        BigDecimal upiCollection = donations.totalPaidByMode(PaymentMode.UPI).add(auctions.totalPaidByMode(PaymentMode.UPI));

        m.addAttribute("donations", donations.findAll());
        m.addAttribute("expenses", expenses.findAll());
        m.addAttribute("auctions", auctions.findAll());
        m.addAttribute("dueDonations", donations.findByPaymentStatusOrderByPromisedDateAsc(PaymentStatus.DUE));
        m.addAttribute("dueAuctions", auctions.findByStatusOrderByAuctionDateDesc(AuctionStatus.DUE));
        m.addAttribute("cashDonations", donations.findByPaymentStatusAndPaymentModeOrderByDonatedAtDesc(PaymentStatus.PAID, PaymentMode.CASH));
        m.addAttribute("upiDonations", donations.findByPaymentStatusAndPaymentModeOrderByDonatedAtDesc(PaymentStatus.PAID, PaymentMode.UPI));
        m.addAttribute("cashAuctions", auctions.findByStatusAndPaymentModeOrderByAuctionDateDesc(AuctionStatus.PAID, PaymentMode.CASH));
        m.addAttribute("upiAuctions", auctions.findByStatusAndPaymentModeOrderByAuctionDateDesc(AuctionStatus.PAID, PaymentMode.UPI));
        m.addAttribute("cashExpenses", expenses.findByPaymentModeOrderByExpenseDateDesc(PaymentMode.CASH));
        m.addAttribute("upiExpenses", expenses.findByPaymentModeOrderByExpenseDateDesc(PaymentMode.UPI));
        m.addAttribute("lastYearAmount", lastYear);
        m.addAttribute("received", received);
        m.addAttribute("due", donations.totalDueDonation());
        m.addAttribute("spent", spent);
        m.addAttribute("auctionPaid", auctionPaid);
        m.addAttribute("auctionDue", auctionDue);
        m.addAttribute("auctionTotals", auctions.eventAuctionTotals());
        m.addAttribute("grandReceived", received.add(auctionPaid));
        m.addAttribute("cashLeft", currentInHand);
        m.addAttribute("totalInHand", lastYear.add(currentInHand));
        m.addAttribute("cashCollection", cashCollection);
        m.addAttribute("upiCollection", upiCollection);
        m.addAttribute("cashExpenseTotal", expenses.totalExpenseByMode(PaymentMode.CASH));
        m.addAttribute("upiExpenseTotal", expenses.totalExpenseByMode(PaymentMode.UPI));
        return "reports/index";
    }
}
