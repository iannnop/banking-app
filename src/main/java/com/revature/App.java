package com.revature;

import com.revature.account.Account;
import com.revature.account.AccountDAOImpl;
import com.revature.account.AccountStatus;
import com.revature.exception.InvalidAmountException;
import com.revature.exception.NegativeBalanceException;
import com.revature.exception.UnauthorizedException;
import com.revature.user.UserDAOImpl;
import com.revature.user.User;
import com.revature.user.UserRole;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    private static User visitor;
    private static final UserDAOImpl userDAO = new UserDAOImpl();
    private static final AccountDAOImpl accountDAO = new AccountDAOImpl();

    public static User login() {
        User visitor = null;
        String username;
        String password;
        String firstName;
        String lastName;
        String email;

        String option;
        Scanner sc = new Scanner(System.in);
        System.out.println("Hello!\nPlease log in or register for an account");
        while (visitor == null) {
            System.out.println("\n===== LOGIN MENU ======\n" +
                    "L - Log In\n" +
                    "R - Register\n" +
                    "Q - Quit\n");
            System.out.print("Please select an option: ");
            option = sc.next();
            sc.nextLine();
            if (option.length() > 1)
                option = " ";
            System.out.println();
            switch (Character.toUpperCase(option.charAt(0))) {
                case 'L':
                    System.out.println("===== Login =====");
                    System.out.print("Username: ");
                    username = sc.next();
                    System.out.print("Password: ");
                    password = sc.next();
                    visitor = userDAO.getUser(username);
                    if (visitor == null) {
                        System.out.println("User could not be found");
                        break;
                    }
                    if (!visitor.getPassword().equals(password)) {
                        System.out.println("Incorrect password");
                        visitor = null;
                        break;
                    }
                    break;
                case 'R':
                    System.out.println("===== Register =====");
                    System.out.print("Username: ");
                    username = sc.next();
                    System.out.print("Password: ");
                    password = sc.next();
                    System.out.print("First Name: ");
                    firstName = sc.next();
                    System.out.print("Last Name: ");
                    lastName = sc.next();
                    System.out.print("Email: ");
                    email = sc.next();
                    visitor = userDAO.createUser(UserRole.CUSTOMER, username, password, firstName, lastName, email);
                    break;
                case 'Q':
                    System.out.println("Shutting down...");
                    File file = new File("user.obj");
                    file.deleteOnExit();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
        return visitor;
    }
    public static void adminAccessMenu(User user) {
        int accountNumber;
        User otherUser;
        Account account;
        Account otherAccount;
        double amount;
        String username;
        String description;

        String option = " ";
        char optionChar = Character.toUpperCase(option.charAt(0));
        Scanner sc = new Scanner(System.in);
        while (optionChar != 'X') {
            System.out.println("\n===== MAIN MENU FOR USER \""+user.getUsername()+"\" ======\n" +
                    "A - Apply for a new account\n" +
                    "U - View user information\n" +
                    "I - Edit user information\n" +
                    "V - View "+user.getUsername()+"'s accounts\n" +
                    "P - Print list of transactions for one of \""+user.getUsername()+"'s\" accounts\n" +
                    "E - Edit an account\n" +
                    "D - Deposit into an account\n" +
                    "W - Withdraw from an account\n" +
                    "T - Transfer money to another account\n" +
                    "X - Exit user menu\n");
            System.out.print("Please select an option: ");
            option = sc.next();
            sc.nextLine();
            if (option.length() > 1) {
                option = " ";
            }
            optionChar = Character.toUpperCase(option.charAt(0));
            switch (optionChar) {
                case 'A':
                    System.out.println("What type of account do you want to apply for?\n" +
                            "P - Personal account\n" +
                            "J - Joint account\n");
                    System.out.print("Option: ");
                    char choice = Character.toUpperCase(sc.next().charAt(0));
                    System.out.println("How much money will you deposit for your starting balance?");
                    double startingBalance = sc.nextDouble();
                    if (choice == 'P') {
                        try {
                            user.applyForAccount(startingBalance);
                        } catch (InvalidAmountException e) {
                            System.out.println(e.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (choice == 'J') {
                        System.out.println("What is the username of the other owner of the account?");
                        username = sc.next();
                        otherUser = userDAO.getUser(username);
                        if (otherUser == null) {
                            System.out.println("User \""+username+"\" was not found.\n" +
                                    "Returning to main menu...\n");
                            break;
                        } else if (user.getUsername().equals(otherUser.getUsername())) {
                            System.out.println("Other user cannot be yourself.\n" +
                                    "Returning to main menu...\n");
                            break;
                        }
                        try {
                            user.applyForJointAccount(startingBalance, otherUser);
                        } catch (InvalidAmountException e) {
                            System.out.println(e.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 'U':
                    user.printUserInfo();
                    break;
                case 'I':
                    System.out.println("=== Editing all user information ===");
                    System.out.print("Role (CUSTOMER, EMPLOYEE, ADMIN): ");
                    user.setRole(UserRole.valueOf(sc.next()));
                    System.out.print("First name: ");
                    user.setFirstName(sc.nextLine());
                    System.out.print("Last name: ");
                    user.setLastName(sc.nextLine());
                    System.out.print("Email: ");
                    user.setEmail(sc.nextLine());
                    System.out.print("Phone: ");
                    user.setPhone(sc.nextLine());
                    System.out.print("Address: ");
                    user.setAddress(sc.nextLine());
                    userDAO.updateUser(user);
                    break;
                case 'V':
                    user.printAccounts();
                    break;
                case 'P':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    System.out.println("Which account do you want to view?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    account.printTransactions();
                    break;
                case 'E':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    System.out.println("Which account do you want to edit?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("Enter a new description for Account "+accountNumber);
                    description = sc.nextLine();
                    account.setDescription(description);
                    accountDAO.updateAccount(account);
                    break;
                case 'D':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    System.out.println("Which account do you want to deposit to?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("How much would you like to deposit?");
                    amount = sc.nextDouble();
                    System.out.println("Please enter a deposit description:");
                    description = sc.nextLine();
                    try {
                        account.deposit(amount, description);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'W':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    System.out.println("Which account do you want to withdraw from?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("How much would you like to withdraw?");
                    amount = sc.nextDouble();
                    System.out.println("Please enter a withdraw description:");
                    description = sc.nextLine();
                    try {
                        account.withdraw(amount, description);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'T':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    System.out.println("Which account do you want to transfer from?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("Please enter the username of the user you would like to transfer to:");
                    username = sc.next();
                    otherUser = userDAO.getUser(username);
                    if (otherUser == null) {
                        System.out.println("User \""+username+"\" was not found.\n" +
                                "Returning to main menu...\n");
                        break;
                    } else if (otherUser.getAccounts().size() == 0) {
                        System.out.println("User \""+username+"\" has no accounts to transfer to\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    System.out.println("Which account would you like to transfer to?");
                    System.out.printf("Account Number (0 - %d): ", otherUser.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in \""+username+"\" accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    otherAccount = user.getAccount(accountNumber);
                    System.out.println("How much would you like to transfer?");
                    amount = sc.nextDouble();
                    System.out.println("Please enter a transfer description:");
                    description = sc.nextLine();
                    try {
                        account.transfer(otherAccount, amount, description);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'X':
                    System.out.println("Exiting user menu for \""+user.getUsername()+"\"...");
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
    public static void customerMenu(User user) {
        int accountNumber;
        User otherUser;
        Account account;
        Account otherAccount;
        double amount;
        String username;
        String description;

        String option = " ";
        char optionChar = Character.toUpperCase(option.charAt(0));
        Scanner sc = new Scanner(System.in);
        while (optionChar != 'Q' && optionChar != 'X') {
            System.out.println("\n===== MAIN MENU ======\n" +
                    "A - Apply for a new account\n" +
                    "U - View user information\n" +
                    "I - Edit user information\n" +
                    "V - View your accounts\n" +
                    "P - Print list of transactions for one of your accounts\n" +
                    "E - Edit one of your accounts\n" +
                    "D - Deposit into an account\n" +
                    "W - Withdraw from an account\n" +
                    "T - Transfer money to another account\n" +
                    "Q - Quit and save user login\n" +
                    "X - Log out and exit\n");
            System.out.print("Please select an option: ");
            option = sc.next();
            sc.nextLine();
            if (option.length() > 1) {
                option = " ";
            }
            optionChar = Character.toUpperCase(option.charAt(0));
            switch (optionChar) {
                case 'A':
                    System.out.println("What type of account do you want to apply for?\n" +
                            "P - Personal account\n" +
                            "J - Joint account\n");
                    System.out.print("Option: ");
                    char choice = Character.toUpperCase(sc.next().charAt(0));
                    System.out.println("How much money will you deposit for your starting balance?");
                    double startingBalance = sc.nextDouble();
                    if (choice == 'P') {
                        try {
                            user.applyForAccount(startingBalance);
                        } catch (InvalidAmountException e) {
                            System.out.println(e.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (choice == 'J') {
                        System.out.println("What is the username of the other owner of the account?");
                        username = sc.next();
                        otherUser = userDAO.getUser(username);
                        if (otherUser == null) {
                            System.out.println("User \""+username+"\" was not found.\n" +
                                    "Returning to main menu...\n");
                            break;
                        } else if (user.getUsername().equals(otherUser.getUsername())) {
                            System.out.println("Other user cannot be yourself.\n" +
                                    "Returning to main menu...\n");
                            break;
                        }
                        try {
                            user.applyForJointAccount(startingBalance, otherUser);
                        } catch (InvalidAmountException e) {
                            System.out.println(e.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 'U':
                    user.printUserInfo();
                    break;
                case 'I':
                    System.out.println("=== Editing personal user information ===");
                    System.out.print("Please enter your first name: ");
                    user.setFirstName(sc.nextLine());
                    System.out.print("Please enter your last name: ");
                    user.setLastName(sc.nextLine());
                    System.out.print("Please enter your email: ");
                    user.setEmail(sc.nextLine());
                    System.out.print("Please enter your phone number: ");
                    user.setPhone(sc.nextLine());
                    System.out.print("Please enter your address: ");
                    user.setAddress(sc.nextLine());
                    userDAO.updateUser(user);
                    break;
                case 'V':
                    user.printAccounts();
                    break;
                case 'P':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to view?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    account.printTransactions();
                    break;
                case 'E':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to edit?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("Enter a new description for Account "+accountNumber);
                    description = sc.nextLine();
                    account.setDescription(description);
                    accountDAO.updateAccount(account);
                    break;
                case 'D':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to deposit to?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("How much would you like to deposit?");
                    amount = sc.nextDouble();
                    System.out.println("Please enter a deposit description:");
                    description = sc.nextLine();
                    try {
                        account.deposit(amount, description);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'W':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to withdraw from?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("How much would you like to withdraw?");
                    amount = sc.nextDouble();
                    System.out.println("Please enter a withdraw description:");
                    description = sc.nextLine();
                    try {
                        account.withdraw(amount, description);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'T':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to transfer from?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("Please enter the username of the user you would like to transfer to:");
                    username = sc.next();
                    otherUser = userDAO.getUser(username);
                    if (otherUser == null) {
                        System.out.println("User \""+username+"\" was not found.\n" +
                                "Returning to main menu...\n");
                        break;
                    } else if (otherUser.getAccounts().size() == 0) {
                        System.out.println("User \""+username+"\" has no accounts to transfer to\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    System.out.println("Which account would you like to transfer to?");
                    System.out.printf("Account Number (0 - %d): ", otherUser.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in \""+username+"\" accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    otherAccount = user.getAccount(accountNumber);
                    System.out.println("How much would you like to transfer?");
                    amount = sc.nextDouble();
                    System.out.println("Please enter a transfer description:");
                    description = sc.nextLine();
                    try {
                        account.transfer(otherAccount, amount, description);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'Q':
                    System.out.println("Quitting Simple Bank System...\nUser login will be saved for next session");
                    try {
                        FileOutputStream file = new FileOutputStream("./src/main/resources/user.obj");
                        ObjectOutputStream outStream = new ObjectOutputStream(file);
                        outStream.writeObject(visitor);
                        outStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 'X':
                    System.out.println("Logging out...");
                    File file = new File("./src/main/resources/user.obj");
                    file.deleteOnExit();
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
    public static void employeeMenu(User user) {
        int accountNumber;
        int accountId;
        User otherUser;
        Account account;
        Account otherAccount;
        double amount;
        String username;
        String description;

        String option = " ";
        char optionChar = Character.toUpperCase(option.charAt(0));
        Scanner sc = new Scanner(System.in);
        while (optionChar != 'Q' && optionChar != 'X') {
            System.out.println("\n===== MAIN MENU ======\n" +
                    "Account Management:\n" +
                    "1 - View all pending accounts\n" +
                    "2 - Approve an account\n" +
                    "3 - Deny an account\n" +
                    "4 - View an account\n" +
                    "\n" +
                    "User Management:\n" +
                    "6 - View all accounts for a user\n" +
                    "7 - View a user's information\n" +
                    "\n" +
                    "Personal Account Management:\n" +
                    "A - Apply for a new account\n" +
                    "U - View user information\n" +
                    "I - Edit user information\n" +
                    "V - View your accounts\n" +
                    "P - Print list of transactions for one of your accounts\n" +
                    "E - Edit one of your accounts\n" +
                    "D - Deposit into an account\n" +
                    "W - Withdraw from an account\n" +
                    "T - Transfer money to another account\n" +
                    "Q - Quit and save user login\n" +
                    "X - Log out and exit\n");
            System.out.print("Please select an option: ");
            option = sc.next();
            sc.nextLine();
            if (option.length() > 1) {
                option = " ";
            }
            optionChar = Character.toUpperCase(option.charAt(0));
            switch (optionChar) {
                case '1':
                    ArrayList<Account> pendingAccounts = accountDAO.getAllAccountsOfType(AccountStatus.PENDING_APPROVAL);
                    for (Account a : pendingAccounts) {
                        System.out.println(a);
                    }
                    break;
                case '2':
                    System.out.println("Please enter the account_id of the account you would like to approve:");
                    accountId = sc.nextInt();
                    account = accountDAO.getAccount(accountId);
                    if (account == null) {
                        System.out.println("Account with account_id \""+accountId+"\" could not be found\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    try {
                        visitor.approveAccount(account);
                    } catch (UnauthorizedException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case '3':
                    System.out.println("Please enter the account_id of the account you would like to approve:");
                    accountId = sc.nextInt();
                    account = accountDAO.getAccount(accountId);
                    if (account == null) {
                        System.out.println("Account with account_id \""+accountId+"\" could not be found\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    try {
                        visitor.denyAccount(account);
                    } catch (UnauthorizedException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case '4':
                    System.out.print("Please enter the account_id of the account you would like to view: ");
                    accountId = sc.nextInt();
                    account = accountDAO.getAccount(accountId);
                    if (account == null) {
                        System.out.println("Account with account_id \""+accountId+"\" could not be found\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    System.out.println(account);
                    break;
                case '6':
                    System.out.println("Please enter the username of the user you would like to view:");
                    username = sc.next();
                    otherUser = userDAO.getUser(username);
                    if (otherUser == null) {
                        System.out.println("User \""+username+"\" was not found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    otherUser.printAccounts();
                    break;
                case '7':
                    System.out.println("Please enter the username of the user you would like to view:");
                    username = sc.next();
                    otherUser = userDAO.getUser(username);
                    if (otherUser == null) {
                        System.out.println("User \""+username+"\" was not found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    otherUser.printUserInfo();
                    break;
                case 'A':
                    System.out.println("What type of account do you want to apply for?\n" +
                            "P - Personal account\n" +
                            "J - Joint account\n");
                    System.out.print("Option: ");
                    char choice = Character.toUpperCase(sc.next().charAt(0));
                    System.out.println("How much money will you deposit for your starting balance?");
                    double startingBalance = sc.nextDouble();
                    if (choice == 'P') {
                        try {
                            user.applyForAccount(startingBalance);
                        } catch (InvalidAmountException e) {
                            System.out.println(e.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (choice == 'J') {
                        System.out.println("What is the username of the other owner of the account?");
                        username = sc.next();
                        otherUser = userDAO.getUser(username);
                        if (otherUser == null) {
                            System.out.println("User \""+username+"\" was not found.\n" +
                                    "Returning to main menu...\n");
                            break;
                        } else if (user.getUsername().equals(otherUser.getUsername())) {
                            System.out.println("Other user cannot be yourself.\n" +
                                    "Returning to main menu...\n");
                            break;
                        }
                        try {
                            user.applyForJointAccount(startingBalance, otherUser);
                        } catch (InvalidAmountException e) {
                            System.out.println(e.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 'U':
                    user.printUserInfo();
                    break;
                case 'I':
                    System.out.println("=== Editing personal user information ===");
                    System.out.print("Please enter your first name: ");
                    user.setFirstName(sc.nextLine());
                    System.out.print("Please enter your last name: ");
                    user.setLastName(sc.nextLine());
                    System.out.print("Please enter your email: ");
                    user.setEmail(sc.nextLine());
                    System.out.print("Please enter your phone number: ");
                    user.setPhone(sc.nextLine());
                    System.out.print("Please enter your address: ");
                    user.setAddress(sc.nextLine());
                    userDAO.updateUser(user);
                    break;
                case 'V':
                    user.printAccounts();
                    break;
                case 'P':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to view?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    account.printTransactions();
                    break;
                case 'E':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to edit?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("Enter a new description for Account "+accountNumber);
                    description = sc.nextLine();
                    account.setDescription(description);
                    accountDAO.updateAccount(account);
                    break;
                case 'D':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to deposit to?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("How much would you like to deposit?");
                    amount = sc.nextDouble();
                    System.out.println("Please enter a deposit description:");
                    description = sc.nextLine();
                    try {
                        account.deposit(amount, description);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'W':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to withdraw from?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("How much would you like to withdraw?");
                    amount = sc.nextDouble();
                    System.out.println("Please enter a withdraw description:");
                    description = sc.nextLine();
                    try {
                        account.withdraw(amount, description);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'T':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to transfer from?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("Please enter the username of the user you would like to transfer to:");
                    username = sc.next();
                    otherUser = userDAO.getUser(username);
                    if (otherUser == null) {
                        System.out.println("User \""+username+"\" was not found.\n" +
                                "Returning to main menu...\n");
                        break;
                    } else if (otherUser.getAccounts().size() == 0) {
                        System.out.println("User \""+username+"\" has no accounts to transfer to\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    System.out.println("Which account would you like to transfer to?");
                    System.out.printf("Account Number (0 - %d): ", otherUser.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in \""+username+"\" accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    otherAccount = user.getAccount(accountNumber);
                    System.out.println("How much would you like to transfer?");
                    amount = sc.nextDouble();
                    System.out.println("Please enter a transfer description:");
                    description = sc.nextLine();
                    try {
                        account.transfer(otherAccount, amount, description);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'Q':
                    System.out.println("Quitting Simple Bank System...\nUser login will be saved for next session");
                    try {
                        FileOutputStream file = new FileOutputStream("./src/main/resources/user.obj");
                        ObjectOutputStream outStream = new ObjectOutputStream(file);
                        outStream.writeObject(visitor);
                        outStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 'X':
                    System.out.println("Logging out...");
                    File file = new File("./src/main/resources/user.obj");
                    file.deleteOnExit();
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
    public static void adminMenu(User user) {
        int accountNumber;
        int accountId;
        User otherUser;
        Account account;
        Account otherAccount;
        ArrayList<Account> accounts;
        double amount;
        String username;
        String description;

        char choice;
        String option = " ";
        char optionChar = Character.toUpperCase(option.charAt(0));
        Scanner sc = new Scanner(System.in);
        while (optionChar != 'Q' && optionChar != 'X') {
            System.out.println("\n===== MAIN MENU ======\n" +
                    "User-Account Management:\n" +
                    "0 - View all accounts with a given status\n" +
                    "1 - Approve an account\n" +
                    "2 - Deny an account\n" +
                    "3 - Cancel an account\n" +
                    "4 - View an account\n" +
                    "5 - Edit an account\n" +
                    "\n" +
                    "User Management:\n" +
                    "6 - View all accounts for a user\n" +
                    "7 - View a user's information\n" +
                    "8 - Access a user's account menu\n" +
                    "9 - Delete a user's account\n" +
                    "\n" +
                    "Account-Transaction Management:\n" +
                    "A - Apply for a new account\n" +
                    "U - View user information\n" +
                    "I - Edit user information\n" +
                    "V - View your accounts\n" +
                    "P - Print list of transactions for one of your accounts\n" +
                    "E - Edit one of your accounts\n" +
                    "D - Deposit into an account\n" +
                    "W - Withdraw from an account\n" +
                    "T - Transfer money to another account\n" +
                    "Q - Quit and save user login\n" +
                    "X - Log out and exit\n");
            System.out.print("Please select an option: ");
            option = sc.next();
            sc.nextLine();
            if (option.length() > 1) {
                option = " ";
            }
            optionChar = Character.toUpperCase(option.charAt(0));
            switch (optionChar) {
                case '0':
                    System.out.println("1 - View PENDING_APPROVAL accounts\n" +
                            "2 - View ACTIVE accounts\n" +
                            "3 - View DENIED accounts\n" +
                            "4 - View CANCELLED accounts\n");
                    System.out.print("Please select an option: ");
                    choice = sc.next().charAt(0);
                    if (choice == '1') {
                        accounts = accountDAO.getAllAccountsOfType(AccountStatus.PENDING_APPROVAL);
                    } else if (choice == '2') {
                        accounts = accountDAO.getAllAccountsOfType(AccountStatus.ACTIVE);
                    } else if (choice == '3') {
                        accounts = accountDAO.getAllAccountsOfType(AccountStatus.DENIED);
                    } else if (choice == '4') {
                        accounts = accountDAO.getAllAccountsOfType(AccountStatus.CANCELLED);
                    } else {
                        System.out.println("Invalid option.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    if (accounts.size() == 0) {
                        System.out.println("NO ACCOUNTS FOUND\n" +
                                "Returning to main menu...\n");
                    }
                    for (Account a : accounts) {
                        System.out.println(a);
                    }
                    break;
                case '1':
                    System.out.println("Please enter the account_id of the account you would like to approve:");
                    accountId = sc.nextInt();
                    account = accountDAO.getAccount(accountId);
                    if (account == null) {
                        System.out.println("Account with account_id \""+accountId+"\" could not be found\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    try {
                        visitor.approveAccount(account);
                    } catch (UnauthorizedException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case '2':
                    System.out.println("Please enter the account_id of the account you would like to approve:");
                    accountId = sc.nextInt();
                    account = accountDAO.getAccount(accountId);
                    if (account == null) {
                        System.out.println("Account with account_id \""+accountId+"\" could not be found\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    try {
                        visitor.denyAccount(account);
                    } catch (UnauthorizedException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case '3':
                    System.out.println("Please enter the account_id of the account you would like to cancel:");
                    accountId = sc.nextInt();
                    account = accountDAO.getAccount(accountId);
                    if (account == null) {
                        System.out.println("Account with account_id \""+accountId+"\" could not be found\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    try {
                        visitor.cancelAccount(account);
                    } catch (UnauthorizedException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case '4':
                    System.out.print("Please enter the account_id of the account you would like to view: ");
                    accountId = sc.nextInt();
                    account = accountDAO.getAccount(accountId);
                    if (account == null) {
                        System.out.println("Account with account_id \""+accountId+"\" could not be found\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    System.out.println(account);
                    break;
                case '5':
                    System.out.print("Please enter the account_id of the account you would like to edit: ");
                    accountId = sc.nextInt();
                    account = accountDAO.getAccount(accountId);
                    if (account == null) {
                        System.out.println("Account with account_id \""+accountId+"\" could not be found\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    System.out.println("What would you like to do?\n" +
                            "1 - Edit account status, account balance, and account description\n" +
                            "2 - Delete account (WARNING: THIS WILL DELETE ALL ACCOUNT AND RELATED TRANSACTIONS)\n");
                    System.out.println("Please select an option: ");
                    choice = sc.next().charAt(0);
                    if (choice == '1') {
                        System.out.println("Enter new account status (PENDING_APPROVAL, ACTIVE, DENIED, CANCELLED)");
                        account.setStatus(AccountStatus.valueOf(sc.next()));
                        System.out.println("Enter new account balance:");
                        try {
                            account.setBalance(sc.nextDouble());
                        } catch (NegativeBalanceException e) {
                            System.out.println(e.getMessage());
                            break;
                        }
                        System.out.println("Enter new account description:");
                        account.setDescription(sc.nextLine());
                        accountDAO.updateAccount(account);
                    }
                    if (choice == '2') {
                        accountDAO.deleteAccount(account);
                    } else {
                        System.out.println("Invalid option\n" +
                                "Returning to main menu...\n");
                    }
                    break;
                case '6':
                    System.out.println("Please enter the username of the user you would like to view:");
                    username = sc.next();
                    otherUser = userDAO.getUser(username);
                    if (otherUser == null) {
                        System.out.println("User \""+username+"\" was not found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    otherUser.printAccounts();
                    break;
                case '7':
                    System.out.println("Please enter the username of the user you would like to view:");
                    username = sc.next();
                    otherUser = userDAO.getUser(username);
                    if (otherUser == null) {
                        System.out.println("User \""+username+"\" was not found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    otherUser.printUserInfo();
                    break;
                case '8':
                    System.out.println("Please enter the username of the user you would like to manage:");
                    username = sc.next();
                    otherUser = userDAO.getUser(username);
                    if (otherUser == null) {
                        System.out.println("User \""+username+"\" was not found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    System.out.printf("Entering the account management menu for %s \"%s\"", otherUser.getRole(), otherUser.getUsername());
                    adminAccessMenu(otherUser);
                    break;
                case '9':
                    System.out.println("Please enter the username of the user you would like to delete:");
                    username = sc.next();
                    otherUser = userDAO.getUser(username);
                    if (otherUser == null) {
                        System.out.println("User \""+username+"\" was not found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    otherUser.printUserInfo();
                    System.out.println("WARNING: DELETING A USER WILL ALSO DELETE ALL THE USER ACCOUNTS AND ALL RELATED TRANSACTIONS\n" +
                            "ARE YOU SURE?\n" +
                            "1 - Yes, I want to delete the user and all accounts and transactions made by the user\n" +
                            "2 - No, take me back to the main menu");
                    choice = sc.next().charAt(0);
                    if (choice == '1') {
                        userDAO.deleteUser(otherUser);
                    }
                    break;
                case 'A':
                    System.out.println("What type of account do you want to apply for?\n" +
                            "P - Personal account\n" +
                            "J - Joint account\n");
                    System.out.print("Option: ");
                    choice = Character.toUpperCase(sc.next().charAt(0));
                    System.out.println("How much money will you deposit for your starting balance?");
                    double startingBalance = sc.nextDouble();
                    if (choice == 'P') {
                        try {
                            user.applyForAccount(startingBalance);
                        } catch (InvalidAmountException e) {
                            System.out.println(e.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (choice == 'J') {
                        System.out.println("What is the username of the other owner of the account?");
                        username = sc.next();
                        otherUser = userDAO.getUser(username);
                        if (otherUser == null) {
                            System.out.println("User \""+username+"\" was not found.\n" +
                                    "Returning to main menu...\n");
                            break;
                        } else if (user.getUsername().equals(otherUser.getUsername())) {
                            System.out.println("Other user cannot be yourself.\n" +
                                    "Returning to main menu...\n");
                            break;
                        }
                        try {
                            user.applyForJointAccount(startingBalance, otherUser);
                        } catch (InvalidAmountException e) {
                            System.out.println(e.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 'U':
                    user.printUserInfo();
                    break;
                case 'I':
                    System.out.println("=== Editing personal user information ===");
                    System.out.print("Please enter your first name: ");
                    user.setFirstName(sc.nextLine());
                    System.out.print("Please enter your last name: ");
                    user.setLastName(sc.nextLine());
                    System.out.print("Please enter your email: ");
                    user.setEmail(sc.nextLine());
                    System.out.print("Please enter your phone number: ");
                    user.setPhone(sc.nextLine());
                    System.out.print("Please enter your address: ");
                    user.setAddress(sc.nextLine());
                    userDAO.updateUser(user);
                    break;
                case 'V':
                    user.printAccounts();
                    break;
                case 'P':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to view?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    account.printTransactions();
                    break;
                case 'E':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to edit?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("Enter a new description for Account "+accountNumber);
                    description = sc.nextLine();
                    account.setDescription(description);
                    accountDAO.updateAccount(account);
                    break;
                case 'D':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to deposit to?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("How much would you like to deposit?");
                    amount = sc.nextDouble();
                    System.out.println("Please enter a deposit description:");
                    description = sc.nextLine();
                    try {
                        account.deposit(amount, description);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'W':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to withdraw from?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("How much would you like to withdraw?");
                    amount = sc.nextDouble();
                    System.out.println("Please enter a withdraw description:");
                    description = sc.nextLine();
                    try {
                        account.withdraw(amount, description);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'T':
                    if (user.getAccounts().size() == 0) {
                        System.out.println("No accounts found.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    user.printAccounts();
                    System.out.println("Which account do you want to transfer from?");
                    System.out.printf("Account Number (0 - %d): ", user.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in your accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    account = user.getAccount(accountNumber);
                    System.out.println("Please enter the username of the user you would like to transfer to:");
                    username = sc.next();
                    otherUser = userDAO.getUser(username);
                    if (otherUser == null) {
                        System.out.println("User \""+username+"\" was not found.\n" +
                                "Returning to main menu...\n");
                        break;
                    } else if (otherUser.getAccounts().size() == 0) {
                        System.out.println("User \""+username+"\" has no accounts to transfer to\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    otherUser.printAccounts();
                    System.out.println("Which account would you like to transfer to?");
                    System.out.printf("Account Number (0 - %d): ", otherUser.getAccounts().size());
                    accountNumber = sc.nextInt();
                    if (accountNumber > user.getAccounts().size() || accountNumber < 0) {
                        System.out.println("Account could not be found in \""+username+"\" accounts.\n" +
                                "Returning to main menu...\n");
                        break;
                    }
                    otherAccount = user.getAccount(accountNumber);
                    System.out.println("How much would you like to transfer?");
                    amount = sc.nextDouble();
                    System.out.println("Please enter a transfer description:");
                    description = sc.nextLine();
                    try {
                        account.transfer(otherAccount, amount, description);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'Q':
                    System.out.println("Quitting Simple Bank System...\nUser login will be saved for next session");
                    try {
                        FileOutputStream file = new FileOutputStream("./src/main/resources/user.obj");
                        ObjectOutputStream outStream = new ObjectOutputStream(file);
                        outStream.writeObject(visitor);
                        outStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 'X':
                    System.out.println("Logging out...");
                    File file = new File("./src/main/resources/user.obj");
                    file.deleteOnExit();
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
    public static void main(String[] args) {
        try {
            FileInputStream file = new FileInputStream("./src/main/resources/user.obj");
            ObjectInputStream inStream = new ObjectInputStream(file);
            visitor = (User) inStream.readObject();
            visitor = userDAO.getUser(visitor.getId());
            inStream.close();
            System.out.printf("Welcome back, %s!%n", visitor.getUsername());
        } catch (FileNotFoundException e) {
            visitor = login();
            System.out.printf("%n%nHello %s! Welcome to Bank Application%n", visitor.getFirstName());
        } catch (IOException e) {
            System.out.println("An IO error has occurred. Please restart the program.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (visitor.getRole() == UserRole.ADMIN) {
            adminMenu(visitor);
        } else if (visitor.getRole() == UserRole.EMPLOYEE) {
            employeeMenu(visitor);
        } else {
            customerMenu(visitor);
        }
    }
}
