package com.sdya.fund.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Donation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String receiptNo;

    @NotBlank
    private String donorName;

    private String phone;
    private String address;

    @NotNull @DecimalMin("1.00")
    @Column(precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode = PaymentMode.CASH;

    private String referenceNo;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PAID;

    private LocalDate promisedDate;

    @ManyToOne(optional = false)
    private FestivalEvent event;

    @ManyToOne
    private User collectedBy;

    private String volunteerName;

    @Column(length = 700)
    private String volunteerWork;

    private LocalDateTime donatedAt;

    @Column(length = 500)
    private String notes;

    @PrePersist
    void init() {
        if (donatedAt == null) donatedAt = LocalDateTime.now();
        if (paymentStatus == null) paymentStatus = PaymentStatus.PAID;
        if (paymentMode == null) paymentMode = PaymentMode.CASH;
    }
}
