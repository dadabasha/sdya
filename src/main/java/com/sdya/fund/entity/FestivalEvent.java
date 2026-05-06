package com.sdya.fund.entity;
import jakarta.persistence.*;import jakarta.validation.constraints.*;import lombok.*;import java.math.BigDecimal;import java.time.LocalDate;
@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FestivalEvent{ @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @NotBlank private String name; @Enumerated(EnumType.STRING) private EventType type; private LocalDate startDate; private LocalDate endDate; @Column(precision=12,scale=2) private BigDecimal targetAmount; @Column(length=500) private String description; private boolean active=true;}
