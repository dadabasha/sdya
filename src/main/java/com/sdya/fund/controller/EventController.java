package com.sdya.fund.controller;

import com.sdya.fund.entity.*;
import com.sdya.fund.repository.EventRepository;
import com.sdya.fund.service.AccessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventRepository repo;
    private final AccessService access;

    @GetMapping
    String list(Model m, Authentication auth) {
        access.requireEvents(auth);
        m.addAttribute("events", repo.findAll());
        return "events/list";
    }

    @GetMapping("/new")
    String form(Model m, Authentication auth) {
        access.requireEvents(auth);
        m.addAttribute("event", new FestivalEvent());
        m.addAttribute("types", EventType.values());
        return "events/form";
    }

    @PostMapping
    String save(@Valid @ModelAttribute("event") FestivalEvent e, BindingResult br, Model m, Authentication auth) {
        access.requireEvents(auth);
        if (br.hasErrors()) {
            m.addAttribute("types", EventType.values());
            return "events/form";
        }
        repo.save(e);
        return "redirect:/events";
    }

    @GetMapping("/{id}/edit")
    String edit(@PathVariable Long id, Model m, Authentication auth) {
        access.requireEvents(auth);
        m.addAttribute("event", repo.findById(id).orElseThrow());
        m.addAttribute("types", EventType.values());
        return "events/form";
    }
}
