# SDYA Fund Enterprise

Production-ready Spring Boot 3 + MySQL web application for public event/festival fund collection, donation receipt printing, expense tracking, dashboard graphs, role-based security, and responsive mobile/tablet/desktop UI.

## Features
- Secure login with roles: ADMIN, TREASURER, VOLUNTEER
- Festival/public event master
- Donation entry with auto receipt number
- Printable donation receipt
- Experimental Web Bluetooth print button for supported thermal printers
- Expense tracking by category/vendor/bill number
- Dashboard: total received, total spent, cash left, event graph, expense graph
- Reports page with print support
- MySQL database
- Docker Compose deployment
- Responsive premium dark UI

## Default Login
- Username: `admin`
- Password: `Admin@123`

Change this immediately after first login.

## Local Run Using MySQL
1. Install Java 17, Maven, MySQL 8.
2. Create database:
```sql
CREATE DATABASE sdya_fund;
```
3. Update `src/main/resources/application.properties` if your MySQL password is different.
4. Run:
```bash
mvn spring-boot:run
```
5. Open:
```text
http://localhost:8080
```

## Run Using Docker
```bash
docker compose up --build
```
Open `http://localhost:8080`.

## Bluetooth Printing Note
Browser Bluetooth printing works only in supported browsers/devices and printers exposing compatible GATT services. For Android Chrome, use the `Bluetooth Print` button. If your thermal printer has a different UUID, update `src/main/resources/static/js/app.js`. Browser print works everywhere.

## Production Checklist
- Change default admin password
- Use strong `DB_PASSWORD`
- Set `spring.jpa.hibernate.ddl-auto=validate` after tables are finalized
- Enable HTTPS on hosting platform
- Backup MySQL daily
- Create separate users for treasurer/volunteers

## Added Features - Updated Build
- Admin user addition and role based security.
- Donation Paid/Due field with promised date.
- Due report under Reports, with Mark Paid/Edit shortcut.
- Update/Edit donation and expense screens.
- Donation date/time defaults to current date and time.
- Expense date defaults to current date.
- Logout fixed using secure POST logout.
- Thermal receipt layout and Bluetooth print button added for ESC/POS thermal printers.

## Bluetooth Thermal Printer Notes
1. Turn ON the DYNO/Coine thermal printer and pair it in Windows/Android Bluetooth settings.
2. Open the receipt page in Chrome or Edge.
3. Click Bluetooth Print and select the printer.
4. If Bluetooth service is blocked by the printer firmware/browser, use Browser Print after pairing/installing the printer driver.
5. Web Bluetooth works best from HTTPS hosting or localhost.

## Auction Module Added

This updated version includes a complete Auction module:

- Auction Entry: `/auctions/new`
- Auction List/Edit: `/auctions`
- Auction Report included in `/reports`
- Dashboard now includes Auction Received and Auction Collection Graph
- Auction fields: item name, item description, bidder name, phone, address, winning amount, status, payment mode, reference number, event and notes

When the app starts with `spring.jpa.hibernate.ddl-auto=update`, Hibernate will automatically create/update the `auction` table in MySQL.

## Latest update - privilege based dashboard and reports

This version shows menu items, dashboard cards and report sections based on the logged-in user's role and privileges.

- Volunteer example: Donation Report, UPI Amount Report and Donation Due Report.
- Treasurer example: Expense Report, UPI Amount Report, Cash Paid Report and Cash In Hand Report.
- Admin: all entry screens, reports, treasurer screen and user privilege management.

Admin can configure these from **Users & Privileges**.
