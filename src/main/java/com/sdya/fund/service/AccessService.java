package com.sdya.fund.service;

import com.sdya.fund.entity.*;
import com.sdya.fund.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("access")
@RequiredArgsConstructor
public class AccessService {
    private final UserRepository users;

    public User current(Authentication auth) {
        if (auth == null || auth.getName() == null) throw new AccessDeniedException("Login required");
        return users.findByUsername(auth.getName()).orElseThrow(() -> new AccessDeniedException("User not found"));
    }

    public boolean isAdmin(User u) { return u != null && u.getRole() == Role.ADMIN; }
    public boolean isTreasurer(User u) { return u != null && u.getRole() == Role.TREASURER; }
    public boolean isVolunteer(User u) { return u != null && u.getRole() == Role.VOLUNTEER; }

    public boolean canDonation(Authentication auth) { var u=current(auth); return isAdmin(u) || u.isCanDonation(); }
    public boolean canExpense(Authentication auth) { var u=current(auth); return isAdmin(u) || u.isCanExpense(); }
    public boolean canAuction(Authentication auth) { var u=current(auth); return isAdmin(u) || u.isCanAuction(); }
    public boolean canEvents(Authentication auth) { var u=current(auth); return isAdmin(u) || u.isCanEvents(); }
    public boolean canUsers(Authentication auth) { var u=current(auth); return isAdmin(u) || u.isCanUsers(); }
    public boolean canTreasurer(Authentication auth) { var u=current(auth); return isAdmin(u) || u.isCanTreasurer(); }

    public boolean canDonationReport(Authentication auth) { var u=current(auth); return isAdmin(u) || u.isCanReports() || u.isCanDonationReport(); }
    public boolean canDonationDueReport(Authentication auth) { var u=current(auth); return isAdmin(u) || u.isCanReports() || u.isCanDonationDueReport(); }
    public boolean canUpiReport(Authentication auth) { var u=current(auth); return isAdmin(u) || u.isCanReports() || u.isCanUpiReport(); }
    public boolean canExpenseReport(Authentication auth) { var u=current(auth); return isAdmin(u) || u.isCanReports() || u.isCanExpenseReport(); }
    public boolean canCashPaidReport(Authentication auth) { var u=current(auth); return isAdmin(u) || u.isCanReports() || u.isCanCashPaidReport(); }
    public boolean canCashInHandReport(Authentication auth) { var u=current(auth); return isAdmin(u) || u.isCanReports() || u.isCanCashInHandReport(); }
    public boolean canAuctionReport(Authentication auth) { var u=current(auth); return isAdmin(u) || u.isCanReports() || u.isCanAuctionReport(); }
    public boolean canAnyReport(Authentication auth) {
        return canDonationReport(auth) || canDonationDueReport(auth) || canUpiReport(auth) || canExpenseReport(auth) || canCashPaidReport(auth) || canCashInHandReport(auth) || canAuctionReport(auth);
    }

    public void requireDonation(Authentication auth) { if (!canDonation(auth)) deny(); }
    public void requireExpense(Authentication auth) { if (!canExpense(auth)) deny(); }
    public void requireAuction(Authentication auth) { if (!canAuction(auth)) deny(); }
    public void requireReports(Authentication auth) { if (!canAnyReport(auth) && !canTreasurer(auth)) deny(); }
    public void requireTreasurer(Authentication auth) { if (!canTreasurer(auth)) deny(); }
    public void requireEvents(Authentication auth) { if (!canEvents(auth)) deny(); }
    public void requireUsers(Authentication auth) { if (!canUsers(auth)) deny(); }

    private void deny() { throw new AccessDeniedException("You do not have privilege for this screen. Contact admin."); }
}
