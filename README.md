# CashTrackPro

CashTrackPro is a Java application designed to help users track their financial transactions conveniently. This README provides an overview of the classes in the project and a guide on how to use the application.

## Classes Overview

### 1. `CashTrackPro`
- Main class responsible for initiating the application.
- Contains the `main` method to start the program.
- Initializes the `HomeScreen` class to display options to the user.

### 2. `HomeScreen`
- Displays the main options to the user.
- Handles user input and redirects to corresponding functionalities.
- Allows users to add deposits, make payments, view ledger, and exit the application.

### 3. `Transaction`
- Represents a financial transaction.
- Stores informations such as date, time, description, vendor, and amount.
- Provides methods to convert transactions to CSV format and to string for display purposes.

## How to Use CashTrackPro

### 1. Add Deposit
- Select the "D) Add Deposit" option from the home screen.
- Enter the deposit amount, description, and vendor when prompted.
- The deposit will be recorded with the current date and time.

### 2. Make Payment
- Choose the "P) Make Payment (Debit)" option from the home screen.
- Enter the payment amount (use negative value for payment), description, and vendor.
- The payment will be recorded with the current date and time.

### 3. View Ledger
- Select the "L) Ledger" option from the home screen.
- Choose the ledger type: All entries, Deposits, or Payments.
- The ledger will display transactions with details such as date, time, description, vendor, and amount.

### 4. Reports
- Within the Ledger screen, choose the "R) Reports" option.
- Select the type of report:
  - Month To Date
  - Previous Month
  - Year To Date
  - Previous Year
  - Search by Vendor

### 5. Exit
- Choose the "X) Exit" option from the home screen to exit the application.

## Conclusion
CashTrackPro provides a simple and efficient way to track financial transactions. By following the instructions above, users can easily manage their deposits, payments, and view transaction history.

Feel free to explore the source code for further understanding and customization.
