package com.sdya.fund.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Expense {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String category;

    @NotNull @DecimalMin("1.00")
    @Column(precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode = PaymentMode.CASH;

    private String referenceNo;
    private LocalDate expenseDate;
    private String vendorName;
    private String billNo;

    @ManyToOne(optional = false)
    private FestivalEvent event;

    @ManyToOne
    private User enteredBy;

    @Column(length = 700)
    private String notes;

    @PrePersist
    void init() {
        if (paymentMode == null) paymentMode = PaymentMode.CASH;
    }
}
