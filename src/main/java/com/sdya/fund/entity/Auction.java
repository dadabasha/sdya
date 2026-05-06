package com.sdya.fund.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Auction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String itemName;

    @Column(length = 700)
    private String itemDescription;

    @NotBlank
    private String bidderName;

    private String bidderPhone;
    private String bidderAddress;

    @NotNull @DecimalMin("1.00")
    @Column(precision = 12, scale = 2)
    private BigDecimal winningAmount;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status = AuctionStatus.PAID;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode = PaymentMode.CASH;

    private String referenceNo;

    @ManyToOne(optional = false)
    private FestivalEvent event;

    @ManyToOne
    private User enteredBy;

    private LocalDateTime auctionDate;

    @Column(length = 700)
    private String notes;

    @PrePersist
    void init() {
        if (auctionDate == null) auctionDate = LocalDateTime.now();
        if (status == null) status = AuctionStatus.PAID;
        if (paymentMode == null) paymentMode = PaymentMode.CASH;
    }
}
