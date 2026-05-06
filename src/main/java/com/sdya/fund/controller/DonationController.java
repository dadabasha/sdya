package com.sdya.fund.controller;

import com.sdya.fund.entity.*;
import com.sdya.fund.repository.*;
import com.sdya.fund.service.*;
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
@RequestMapping("/donations")
public class DonationController {
    private final DonationRepository repo;
    private final EventRepository events;
    private final UserRepository users;
    private final ReceiptService receipt;
    private final AccessService access;

    @GetMapping
    String list(Model m, Authentication auth) {
        access.requireDonation(auth);
        m.addAttribute("donations", repo.findAll());
        return "donations/list";
    }

    @GetMapping("/new")
    String form(Model m, Authentication auth) {
        access.requireDonation(auth);
        Donation d = new Donation();
        d.setDonatedAt(LocalDateTime.now());
        d.setPaymentStatus(PaymentStatus.PAID);
        d.setPaymentMode(PaymentMode.CASH);
        users.findByUsername(auth.getName()).ifPresent(u -> d.setVolunteerName(u.getFullName()));
        addFormData(m);
        m.addAttribute("donation", d);
        return "donations/form";
    }

    @GetMapping("/{id}/edit")
    String edit(@PathVariable Long id, Model m, Authentication auth) {
        access.requireDonation(auth);
        m.addAttribute("donation", repo.findById(id).orElseThrow());
        addFormData(m);
        return "donations/form";
    }

    @PostMapping
    String save(@Valid @ModelAttribute Donation d, BindingResult br, Model m, Authentication a) {
        access.requireDonation(a);
        if (br.hasErrors()) {
            addFormData(m);
            return "donations/form";
        }
        if (d.getId() == null) {
            users.findByUsername(a.getName()).ifPresent(d::setCollectedBy);
        } else {
            repo.findById(d.getId()).ifPresent(old -> d.setCollectedBy(old.getCollectedBy()));
        }
        Donation saved = repo.save(d);
        if (saved.getReceiptNo() == null || saved.getReceiptNo().isBlank()) {
            saved.setReceiptNo(receipt.nextReceiptNo(saved.getId()));
            repo.save(saved);
        }
        return "redirect:/receipts/" + saved.getReceiptNo();
    }

    private void addFormData(Model m) {
        m.addAttribute("events", events.findByActiveTrueOrderByStartDateDesc());
        m.addAttribute("modes", PaymentMode.values());
        m.addAttribute("statuses", PaymentStatus.values());
    }
}
