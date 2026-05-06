package com.sdya.fund.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 3, max = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Email @Column(unique = true)
    private String email;

    @NotBlank
    private String fullName;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled = true;

    private boolean canDonation = true;
    private boolean canExpense = false;
    private boolean canAuction = false;
    private boolean canReports = false;

    // Granular report/dashboard privileges. Admin gets all automatically.
    private boolean canDonationReport = true;
    private boolean canDonationDueReport = true;
    private boolean canUpiReport = true;
    private boolean canExpenseReport = false;
    private boolean canCashPaidReport = false;
    private boolean canCashInHandReport = false;
    private boolean canAuctionReport = false;

    private boolean canEvents = false;
    private boolean canUsers = false;
    private boolean canTreasurer = false;

    private LocalDateTime createdAt;

    @PrePersist
    void init() {
        createdAt = LocalDateTime.now();
        if (role == null) role = Role.VOLUNTEER;
    }
}
