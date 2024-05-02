package com.pluralsight;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FinancialTracker {
    private static final String ADD_DEPOSIT_OPTION = "D";
    private static final String MAKE_PAYMENT_OPTION = "P";
    private static final String LEDGER_OPTION = "L";
    private static final String EXIT_OPTION = "X";
    private static final String TRANSACTION_FILE = "Files/transactions.csv";
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_DATE;
    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("kk:mm:ss");

    public static void main(String[] args) {
        HomeScreen homeScreen = new HomeScreen();
        homeScreen.displayOptions();
    }

    static class HomeScreen {
        public void displayOptions() {
            Scanner scanner = new Scanner(System.in);
            String choice;

            do {
                System.out.println("Home Screen:");
                System.out.println("D) Add Deposit");
                System.out.println("P) Make Payment (Debit)");
                System.out.println("L) Ledger");
                System.out.println("X) Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextLine().toUpperCase();

                switch (choice) {
                    case ADD_DEPOSIT_OPTION:
                        addDeposit();
                        break;
                    case MAKE_PAYMENT_OPTION:
                        makePayment();
                        break;
                    case LEDGER_OPTION:
                        displayLedger();
                        break;
                    case EXIT_OPTION:
                        System.out.println("Exiting application...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (!choice.equals(EXIT_OPTION));
        }
    }

    public static void addDeposit() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter deposit amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("Enter deposit description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor name: ");
            String vendor = scanner.nextLine();

            // Get current date and time
            LocalDate Date = LocalDate.now();
            LocalTime Time = LocalTime.now();

            // Create transaction object
            Transaction deposit = new Transaction(Date.format(dateFormat), Time.format(timeFormat), description, vendor, amount);

            // Save deposit to CSV file
            saveTransactionToCSV(deposit);

            System.out.println("Deposit added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding deposit: " + e.getMessage());
        }
    }

    public static void makePayment() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter payment amount: ");
            double amount = scanner.nextDouble();
            amount = amount*-1;
            scanner.nextLine(); // Consume newline character

            System.out.print("Enter payment description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor name: ");
            String vendor = scanner.nextLine();

            // Get current date and time
            LocalDate Date = LocalDate.now();
            LocalTime Time = LocalTime.now();

            // Create transaction object
            Transaction payment = new Transaction(Date.format(dateFormat), Time.format(timeFormat), description, vendor, amount);

            // Save payment to CSV file
            saveTransactionToCSV(payment);

            System.out.println("Payment made successfully.");
        } catch (Exception e) {
            System.out.println("Error making payment: " + e.getMessage());
        }
    }
    public static void displayLedger() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            System.out.println("Ledger Screen:");
            System.out.println("A) All ");
            System.out.println("D) Deposits ");
            System.out.println("P) Payments ");
            System.out.println("R) Reports ");
            System.out.println("0) Back ");
            System.out.print("Enter your choice: ");
            choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A":
                    displayAllEntries();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    System.out.println("Going to Reports screen...");
                    displayReportsMenu();
                    break;
                case "0":
                    System.out.println("Going back to Home screen...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (!choice.equals("0"));
    }


    public static void displayAllEntries() {
        try {
            String date = "Date";
            String time = "Time";
            String description = "Description";
            String vendor = "vendor";
            String amount = "amount";
            System.out.println("Ledger Screen:");
            System.out.printf("| %-20s | %-20s | %-20s | %-20s | %-20s \n", date, time, description, vendor, amount);
            System.out.println("-".repeat(105));
            List<Transaction> transactions = readTransactionsFromCSV();
            Collections.reverse(transactions); // Reverse list to show newest entries first
            for (Transaction transaction : transactions) {
                System.out.printf("| %-20s | %-20s | %-20s | %-20s | %-20.2f \n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            }
            System.out.println();
            displayReportsMenu();
        } catch (IOException e) {
            System.out.println("Error reading ledger: " + e.getMessage());
        }
    }

    private static void displayDeposits() {
        try {
            List<Transaction> transactions = readTransactionsFromCSV();
            List<Transaction> depositTransactions = filterDeposits(transactions);
            displayTransactions(depositTransactions);
        } catch (IOException e) {
            System.out.println("Error reading ledger: " + e.getMessage());
        }
    }
    private static List<Transaction> filterDeposits(List<Transaction> transactions) {
        List<Transaction> depositTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                depositTransactions.add(transaction);
            }
        }
        return depositTransactions;
    }

    private static void displayPayments() {
        try {
            List<Transaction> transactions = readTransactionsFromCSV();
            List<Transaction> paymentTransactions = filterPayments(transactions);
            displayTransactions(paymentTransactions);
        } catch (IOException e) {
            System.out.println("Error reading ledger: " + e.getMessage());
        }
    }
    private static List<Transaction> filterPayments(List<Transaction> transactions) {
        List<Transaction> paymentTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                paymentTransactions.add(transaction);
            }
        }
        return paymentTransactions;
    }

    private static void displayTransactions(List<Transaction> transactions) {
        System.out.printf("| %-12s | %-10s | %-20s | %-20s | %-8s |\n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(78));
        for (Transaction transaction : transactions) {
            System.out.printf("| %-12s | %-10s | %-20s | %-20s | %-8.2f |\n",
                    transaction.getDate(), transaction.getTime(),
                    transaction.getDescription(), transaction.getVendor(),
                    transaction.getAmount());
        }
        System.out.println();
    }



    private static void displayReportsMenu() {
        Scanner scanner = new Scanner(System.in);
        char choice;

        do {
            System.out.println("Reports Screen:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Enter your choice: ");
            choice = Character.toUpperCase(scanner.nextLine().charAt(0));

            switch (choice) {
                case '1':
                    runMonthToDateReport();
                    break;
                case '2':
                    runPreviousMonthReport();
                    break;
                case '3':
                    runYearToDateReport();
                    break;
                case '4':
                    runPreviousYearReport();
                    break;
                case '5':
                    searchByVendor();
                    break;
                case '0':
                    System.out.println("Going back to Ledger screen...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != '0');
    }

    private static void runMonthToDateReport() {
        // Implement Month To Date report
        System.out.println("Running Month To Date report...");
    }
    private static void runPreviousMonthReport() {
        // Implement Previous Month report
        System.out.println("Running Previous Month report...");
    }
    private static void runYearToDateReport() {
        // Implement Year To Date report
        System.out.println("Running Year To Date report...");
    }
    private static void runPreviousYearReport() {
        // Implement Previous Year report
        System.out.println("Running Previous Year report...");
    }
    private static void searchByVendor() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter vendor name: ");
        String vendor = scanner.nextLine();
        try {
            System.out.println("Entries for vendor '" + vendor + "':");
            List<Transaction> transactions = readTransactionsFromCSV();
            for (Transaction transaction : transactions) {
                if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                    System.out.println(transaction);
                }
            }
            System.out.println();
        } catch (IOException e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }
    }


    private static String getCurrentDateTime() {
        return LocalDateTime.now().toString();
    }

    private static void saveTransactionToCSV(Transaction transaction) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTION_FILE, true))) {
            writer.println(transaction.toCSVString());
        }
    }

    private static List<Transaction> readTransactionsFromCSV() throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String date = parts[0].trim();
                    String time = parts[1].trim();
                    String description = parts[2].trim();
                    String vendor = parts[3].trim();
                    double amount = Double.parseDouble(parts[4].trim());
                    transactions.add(new Transaction(date, time, description, vendor, amount));
                }
            }
        }
        return transactions;
    }

    static class Transaction {
        private final String date;
        private final String time;
        private final String description;
        private String vendor;
        private final double amount;

        public Transaction(String Date, String Time, String description, String vendor, double amount) {
            this.date = Date;
            this.time = Time;
            this.description = description;
            this.vendor = vendor;
            this.amount = amount;
        }

        public String toCSVString() {
            return String.format("%s          | %s          | %s          | %s          | %.2f", date, time, description, vendor, amount);
        }

        public String toString() {
            return String.format("%s          | %s          | %s          | %s          | %.2f", date, time, description, vendor, amount);
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getDescription() {
            return description;
        }


        public double getAmount() {
            return amount;
        }

        public String getVendor() {
            return vendor;
        }
    }
}