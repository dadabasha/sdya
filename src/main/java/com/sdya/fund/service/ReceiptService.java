package com.sdya.fund.service;

import com.sdya.fund.entity.Donation;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;

@Service
public class ReceiptService {
    public String nextReceiptNo(Long id) {
        return "SDYA-" + java.time.Year.now() + "-" + String.format("%06d", id);
    }

    public String receiptText(Donation d) {
        return "SDYA FESTIVAL FUND\n" +
                "Receipt: " + d.getReceiptNo() + "\n" +
                "Name: " + d.getDonorName() + "\n" +
                "Phone: " + (d.getPhone() == null ? "" : d.getPhone()) + "\n" +
                "Amount: Rs." + d.getAmount() + "\n" +
                "Status: " + d.getPaymentStatus() + "\n" +
                "Payment: " + d.getPaymentMode() + "\n" +
                "Event: " + d.getEvent().getName() + "\n" +
                "Volunteer: " + (d.getVolunteerName() == null ? "" : d.getVolunteerName()) + "\n" +
                "Work: " + (d.getVolunteerWork() == null ? "" : d.getVolunteerWork()) + "\n" +
                "Date: " + d.getDonatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\n" +
                "Thank you!\n";
    }
}
