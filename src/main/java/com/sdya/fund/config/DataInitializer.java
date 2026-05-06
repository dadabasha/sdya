package com.sdya.fund.config;

import com.sdya.fund.entity.*;
import com.sdya.fund.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner seed(UserRepository users, EventRepository events, FundSettingsRepository settings) {
        return args -> {
            if (!users.existsByUsername("admin")) {
                User admin = User.builder()
                        .username("admin").password(encoder.encode("Admin@123"))
                        .fullName("System Administrator").email("admin@sdya.local").phone("9999999999")
                        .role(Role.ADMIN).enabled(true)
                        .canDonation(true).canExpense(true).canAuction(true).canReports(true).canEvents(true).canUsers(true).canTreasurer(true).canDonationReport(true).canDonationDueReport(true).canUpiReport(true).canExpenseReport(true).canCashPaidReport(true).canCashInHandReport(true).canAuctionReport(true)
                        .build();
                users.save(admin);

                User treasurer = User.builder()
                        .username("treasurer").password(encoder.encode("Treasurer@123"))
                        .fullName("Treasurer").email("treasurer@sdya.local")
                        .role(Role.TREASURER).enabled(true)
                        .canDonation(true).canExpense(true).canAuction(true).canReports(false).canTreasurer(true).canDonationReport(false).canDonationDueReport(false).canUpiReport(true).canExpenseReport(true).canCashPaidReport(true).canCashInHandReport(true).canAuctionReport(false)
                        .build();
                users.save(treasurer);
            }
            if (events.count() == 0) {
                events.save(FestivalEvent.builder()
                        .name("Ganesh Festival 2026").type(EventType.GANESH)
                        .startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(10))
                        .targetAmount(new BigDecimal("250000"))
                        .description("Public festival fund collection").active(true).build());
            }
            if (!settings.existsById(1L)) {
                settings.save(FundSettings.builder().id(1L).lastYearAmountInHand(BigDecimal.ZERO).financialYear("2026-2027").build());
            }
        };
    }
}
