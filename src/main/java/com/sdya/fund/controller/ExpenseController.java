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
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseRepository repo;
    private final EventRepository events;
    private final UserRepository users;
    private final AccessService access;

    @GetMapping
    String list(Model m, Authentication auth) {
        access.requireExpense(auth);
        m.addAttribute("expenses", repo.findAll());
        return "expenses/list";
    }

    @GetMapping("/new")
    String form(Model m, Authentication auth) {
        access.requireExpense(auth);
        Expense e = new Expense();
        e.setExpenseDate(LocalDate.now());
        e.setPaymentMode(PaymentMode.CASH);
        addFormData(m);
        m.addAttribute("expense", e);
        return "expenses/form";
    }

    @GetMapping("/{id}/edit")
    String edit(@PathVariable Long id, Model m, Authentication auth) {
        access.requireExpense(auth);
        m.addAttribute("expense", repo.findById(id).orElseThrow());
        addFormData(m);
        return "expenses/form";
    }

    @PostMapping
    String save(@Valid @ModelAttribute Expense e, BindingResult br, Model m, Authentication a) {
        access.requireExpense(a);
        if (br.hasErrors()) {
            addFormData(m);
            return "expenses/form";
        }
        if (e.getId() == null) {
            users.findByUsername(a.getName()).ifPresent(e::setEnteredBy);
        } else {
            repo.findById(e.getId()).ifPresent(old -> e.setEnteredBy(old.getEnteredBy()));
        }
        repo.save(e);
        return "redirect:/expenses";
    }

    private void addFormData(Model m) {
        m.addAttribute("events", events.findByActiveTrueOrderByStartDateDesc());
        m.addAttribute("modes", PaymentMode.values());
    }
}
