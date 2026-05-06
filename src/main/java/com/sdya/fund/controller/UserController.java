package com.sdya.fund.controller;

import com.sdya.fund.entity.*;
import com.sdya.fund.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @GetMapping
    String list(Model m) {
        m.addAttribute("users", repo.findAll());
        return "users/list";
    }

    @GetMapping("/new")
    String form(Model m) {
        m.addAttribute("user", new User());
        m.addAttribute("roles", Role.values());
        return "users/form";
    }

    @GetMapping("/{id}/edit")
    String edit(@PathVariable Long id, Model m) {
        m.addAttribute("user", repo.findById(id).orElseThrow());
        m.addAttribute("roles", Role.values());
        return "users/form";
    }

    @PostMapping
    String save(@ModelAttribute User u) {
        if (u.getId() != null && (u.getPassword() == null || u.getPassword().isBlank())) {
            repo.findById(u.getId()).ifPresent(old -> u.setPassword(old.getPassword()));
        } else if (u.getPassword() != null && !u.getPassword().startsWith("$2a$")) {
            u.setPassword(encoder.encode(u.getPassword()));
        }
        repo.save(u);
        return "redirect:/users";
    }
}
