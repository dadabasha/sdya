package com.sdya.fund.controller;

import com.sdya.fund.entity.*;
import com.sdya.fund.repository.*;
import com.sdya.fund.service.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/treasurer")
public class TreasurerController {
    private final FundSettingsRepository settings;
    private final DonationRepository donations;
    private final ExpenseRepository expenses;
    private final AuctionRepository auctions;
    private final AccessService access;

    @GetMapping
    String screen(Model m, Authentication auth) {
        access.requireTreasurer(auth);
        FundSettings s = settings.findById(1L).orElse(FundSettings.builder().id(1L).lastYearAmountInHand(BigDecimal.ZERO).financialYear("2026-2027").build());
        BigDecimal donation = donations.totalPaidDonation();
        BigDecimal auction = auctions.totalPaidAuction();
        BigDecimal expense = expenses.totalExpense();
        BigDecimal currentHand = donation.add(auction).subtract(expense);
        m.addAttribute("settings", s);
        m.addAttribute("donation", donation);
        m.addAttribute("auction", auction);
        m.addAttribute("totalCollected", donation.add(auction));
        m.addAttribute("expense", expense);
        m.addAttribute("currentHand", currentHand);
        m.addAttribute("totalInHand", s.getLastYearAmountInHand().add(currentHand));
        m.addAttribute("donationDue", donations.totalDueDonation());
        m.addAttribute("auctionDue", auctions.totalDueAuction());
        m.addAttribute("totalDue", donations.totalDueDonation().add(auctions.totalDueAuction()));
        m.addAttribute("cashCollection", donations.totalPaidByMode(PaymentMode.CASH).add(auctions.totalPaidByMode(PaymentMode.CASH)));
        m.addAttribute("upiCollection", donations.totalPaidByMode(PaymentMode.UPI).add(auctions.totalPaidByMode(PaymentMode.UPI)));
        return "treasurer/index";
    }

    @PostMapping("/settings")
    String save(@ModelAttribute FundSettings s, Authentication auth) {
        access.requireTreasurer(auth);
        s.setId(1L);
        if (s.getLastYearAmountInHand() == null) s.setLastYearAmountInHand(BigDecimal.ZERO);
        settings.save(s);
        return "redirect:/treasurer";
    }
}
