package com.sdya.fund.controller;

import com.sdya.fund.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService service;
    private final AccessService access;

    @GetMapping("/dashboard")
    String dashboard(Model model, Authentication auth) {
        model.addAllAttributes(service.data());
        model.addAttribute("currentUser", access.current(auth));
        model.addAttribute("showDonationReport", access.canDonationReport(auth));
        model.addAttribute("showDueReport", access.canDonationDueReport(auth));
        model.addAttribute("showUpiReport", access.canUpiReport(auth));
        model.addAttribute("showExpenseReport", access.canExpenseReport(auth));
        model.addAttribute("showCashPaidReport", access.canCashPaidReport(auth));
        model.addAttribute("showCashInHandReport", access.canCashInHandReport(auth));
        model.addAttribute("showAuctionReport", access.canAuctionReport(auth));
        model.addAttribute("showTreasurer", access.canTreasurer(auth));
        return "dashboard/index";
    }
}
