package com.sdya.fund.controller;

import com.sdya.fund.entity.*;
import com.sdya.fund.repository.*;
import com.sdya.fund.service.AccessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auctions")
public class AuctionController {
    private final AuctionRepository repo;
    private final EventRepository events;
    private final UserRepository users;
    private final AccessService access;

    @GetMapping
    String list(Model model, Authentication auth) {
        access.requireAuction(auth);
        model.addAttribute("auctions", repo.findAll());
        model.addAttribute("paidTotal", repo.totalPaidAuction());
        model.addAttribute("dueTotal", repo.totalDueAuction());
        return "auctions/list";
    }

    @GetMapping("/new")
    String form(Model model, Authentication auth) {
        access.requireAuction(auth);
        Auction auction = new Auction();
        auction.setAuctionDate(LocalDateTime.now());
        auction.setStatus(AuctionStatus.PAID);
        auction.setPaymentMode(PaymentMode.CASH);
        addFormData(model);
        model.addAttribute("auction", auction);
        return "auctions/form";
    }

    @GetMapping("/{id}/edit")
    String edit(@PathVariable Long id, Model model, Authentication auth) {
        access.requireAuction(auth);
        model.addAttribute("auction", repo.findById(id).orElseThrow());
        addFormData(model);
        return "auctions/form";
    }

    @PostMapping
    String save(@Valid @ModelAttribute Auction auction, BindingResult br, Model model, Authentication auth) {
        access.requireAuction(auth);
        if (br.hasErrors()) {
            addFormData(model);
            return "auctions/form";
        }
        if (auction.getId() == null) {
            users.findByUsername(auth.getName()).ifPresent(auction::setEnteredBy);
        } else {
            repo.findById(auction.getId()).ifPresent(old -> auction.setEnteredBy(old.getEnteredBy()));
        }
        repo.save(auction);
        return "redirect:/auctions";
    }

    private void addFormData(Model model) {
        model.addAttribute("events", events.findByActiveTrueOrderByStartDateDesc());
        model.addAttribute("statuses", AuctionStatus.values());
        model.addAttribute("modes", PaymentMode.values());
    }
}
