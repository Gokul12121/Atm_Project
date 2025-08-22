package com.atm.demo;

import com.atm.demo.service.UserService;
import com.atm.demo.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.Console;
import java.util.Optional;

import java.util.Scanner;

@Component
public class AtmConsole implements CommandLineRunner {

    private final UserService userService;

    public AtmConsole(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Welcome to ATM Simulation ===");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter choice: ");

            String input = scanner.nextLine().trim();

            // Validate input
            if (!input.equals("1") && !input.equals("2")) {
                System.out.println("Invalid choice. Please enter 1 or 2.");
                continue; // Go back to menu
            }

            int choice = Integer.parseInt(input);

            if (choice == 2) {
                System.out.println("Exiting ATM. Goodbye!");
                break;
            }

            //  if choice == 1
            System.out.print("Enter account number: ");
            String accNumber = scanner.nextLine();

            String pin;

            Console console = System.console();
            if (console != null) {
                char[] pinArray = console.readPassword("Enter PIN: "); // input hidden
                pin = new String(pinArray);
            } else {
                // fallback when running in IDE
                System.out.print("Enter PIN (visible in IDE): ");
                pin = scanner.nextLine();
            }

            Optional<User> loggedInUser = userService.login(accNumber, pin);
            if (loggedInUser.isEmpty()) {
                System.out.println("Invalid account number or PIN.");
                continue;
            }

            User user = loggedInUser.get();

            // Role-based menu
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                showAdminMenu(scanner);
            } else {
                showUserMenu(scanner, user.getAccNumber());
            }
        }
    }

    private void showUserMenu(Scanner scanner, String accNumber) {
        boolean session = true;
        while (session) {
            System.out.println("\n--- ATM MENU ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Reset PIN");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");
            int atmChoice = scanner.nextInt();
            scanner.nextLine();

            switch (atmChoice) {
                case 1:
                    Double balance = userService.getBalance(accNumber);
                    System.out.println("Your balance is: " + balance);
                    break;

                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    Double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine();
                    try {
                        Double newBalance = userService.withdraw(accNumber, withdrawAmount);
                        System.out.println("Withdrawal successful! New balance: " + newBalance);
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.print("Enter amount to deposit: ");
                    Double depositAmount = scanner.nextDouble();
                    scanner.nextLine();
                    try {
                        Double newBalance = userService.deposit(accNumber, depositAmount);
                        System.out.println("Deposit successful! New balance: " + newBalance);
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Enter old PIN: ");
                    String oldPin = scanner.nextLine();
                    System.out.print("Enter new PIN: ");
                    String newPin = scanner.nextLine();
                    try {
                        System.out.println(userService.resetPin(accNumber, oldPin, newPin));
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 5:
                    session = false;
                    System.out.println("Logged out successfully.");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void showAdminMenu(Scanner scanner) {
        boolean adminSession = true;
        while (adminSession) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Add User");
            System.out.println("2. Delete User");
            System.out.println("3. Logout");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter new Account number: ");
                    String newCard = scanner.nextLine();
                    System.out.print("Enter PIN for new user: ");
                    String newPin = scanner.nextLine();
                    System.out.print("Enter initial balance: ");
                    Double balance = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println(userService.addUser(newCard, newPin, balance));
                    break;

                case 2:
                    System.out.print("Enter Account number to delete: ");
                    String deleteCard = scanner.nextLine();
                    userService.deleteUser(deleteCard); // We'll add this in service
                    System.out.println("User deleted successfully.");
                    break;

                case 3:
                    adminSession = false;
                    System.out.println("Admin logged out.");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
