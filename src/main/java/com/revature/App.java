package com.revature;

import com.revature.account.Account;
import com.revature.account.AccountDAOImpl;
import com.revature.exception.InvalidAmountException;
import com.revature.transaction.TransactionDAOImpl;
import com.revature.user.UserDAOImpl;
import com.revature.user.User;
import com.revature.user.UserRole;

import java.io.*;
import java.util.Scanner;

public class App {

    private static User visitor;
    private static final UserDAOImpl userDAO = new UserDAOImpl();
    private static final AccountDAOImpl accountDAO = new AccountDAOImpl();
    private static final TransactionDAOImpl transactionDAO = new TransactionDAOImpl();

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
    public static void customerMenu(User user) {
        int accountNumber;
        Account account;

        String option = " ";
        char optionChar = Character.toUpperCase(option.charAt(0));
        Scanner sc = new Scanner(System.in);
        while (optionChar != 'Q' && optionChar != 'X') {
            System.out.println("\n===== MAIN MENU ======\n" +
                    "A - Apply for a new account\n" +
                    "U - View user information\n" +
                    "V - View your accounts\n" +
                    "E - Edit one of your accounts\n" +
                    "D - Deposit into an account\n" +
                    "W - Withdraw from an account\n" +
                    "T - Transfer money to another account\n" +
                    "Q - Quit\n" +
                    "X - Log out and Exit\n");
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
                        String username = sc.next();
                        User otherUser = userDAO.getUser(username);
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
                case 'V':
                    user.printAccounts();
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
                    String description = sc.nextLine();
                    account.setDescription(description);
                    accountDAO.updateAccount(account);
                    break;
                case 'D':
                    System.out.println("Deposit!\n");
                    break;
                case 'W':
                    System.out.println("Withdraw!\n");
                    break;
                case 'T':
                    System.out.println("Transfer!\n");
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
        String option = " ";
        int id;
        Scanner sc = new Scanner(System.in);
        while (Character.toUpperCase(option.charAt(0)) != 'Q' && Character.toUpperCase(option.charAt(0)) != 'X') {
            System.out.println("\n===== EMPLOYEE MENU ======\n" +
                    "A - Apply for a new account\n" +
                    "OPTION - OPTION\n" +
                    "Q - Quit\n" +
                    "X - Log out and Exit\n");
            System.out.print("Please select an option: ");
            option = sc.next();
            sc.nextLine();
            if (option.length() > 1)
                option = " ";
            switch (Character.toUpperCase(option.charAt(0))) {
                case 'P':
                    System.out.println("Print!\n");
                    break;
                case 'F':
                    break;

                case 'Q':
                    System.out.println("Storage Manager is quitting, current storage is saved for next session");
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
        String option = " ";
        int id;
        Scanner sc = new Scanner(System.in);
        while (Character.toUpperCase(option.charAt(0)) != 'Q' && Character.toUpperCase(option.charAt(0)) != 'X') {
            System.out.println("\n===== ADMINISTRATOR MENU ======\n" +
                    "A - Apply for a new account\n" +
                    "OPTION - OPTION\n" +
                    "Q - Quit\n" +
                    "X - Log out and Exit\n");
            System.out.print("Please select an option: ");
            option = sc.next();
            sc.nextLine();
            if (option.length() > 1)
                option = " ";
            switch (Character.toUpperCase(option.charAt(0))) {
                case 'P':
                    System.out.println("Print!\n");
                    break;
                case 'F':
                    break;

                case 'Q':
                    System.out.println("Storage Manager is quitting, current storage is saved for next session");
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

        if (visitor.getRole() == UserRole.EMPLOYEE) {
            adminMenu(visitor);
        } else if (visitor.getRole() == UserRole.EMPLOYEE) {
            employeeMenu(visitor);
        } else {
            customerMenu(visitor);
        }
    }
}
