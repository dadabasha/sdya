package com.sdya.fund.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FundSettings {
    @Id
    private Long id;

    @Column(precision = 12, scale = 2)
    private BigDecimal lastYearAmountInHand = BigDecimal.ZERO;

    private String financialYear = "2026-2027";

    @Column(length = 500)
    private String notes;
}
