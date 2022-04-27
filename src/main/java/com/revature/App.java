package com.revature;

import com.revature.account.Account;
import com.revature.account.AccountDAOImpl;
import com.revature.exception.NegativeBalanceException;
import com.revature.transaction.TransactionDAOImpl;
import com.revature.user.UserDAOImpl;
import com.revature.user.User;
import com.revature.user.UserRole;

import java.io.*;
import java.util.Scanner;

public class App {

    private static User visitor;
    private static final UserDAOImpl userDAO = new UserDAOImpl();
    public static User login() {
        User visitor = null;
        String username;
        String password;
        String firstName;
        String lastName;
        String email;

        String option = " ";
        Scanner sc = new Scanner(System.in);
        System.out.println("Hello!\nPlease log in or register for an account");
        while (Character.toUpperCase(option.charAt(0)) != 'Q' && visitor == null) {
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
    public static void customerMenu() {
        String option = " ";
        int id;
        Scanner sc = new Scanner(System.in);
        System.out.printf("Hello %s, and welcome to Bank", visitor.getFirstName());
        while (Character.toUpperCase(option.charAt(0)) != 'Q' && Character.toUpperCase(option.charAt(0)) != 'X') {
            System.out.println("\n" +
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
    public static void employeeMenu() {
        String option = " ";
        int id;
        Scanner sc = new Scanner(System.in);
        System.out.printf("Hello %s, and welcome to Bank", visitor.getFirstName());
        while (Character.toUpperCase(option.charAt(0)) != 'Q' && Character.toUpperCase(option.charAt(0)) != 'X') {
            System.out.println("\n" +
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
    public static void adminMenu() {
        String option = " ";
        int id;
        Scanner sc = new Scanner(System.in);
        System.out.printf("Hello %s, and welcome to Bank", visitor.getFirstName());
        while (Character.toUpperCase(option.charAt(0)) != 'Q' && Character.toUpperCase(option.charAt(0)) != 'X') {
            System.out.println("\n" +
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
            inStream.close();
            System.out.printf("Welcome back, %s!%n", visitor.getUsername());
        } catch (FileNotFoundException e) {
            visitor = login();
        } catch (IOException e) {
            System.out.println("An IO error has occurred. Please restart the program.");
            File file = new File("./src/main/resources/user.obj");
            file.deleteOnExit();
            System.exit(0);
        } catch (ClassNotFoundException e) {
            System.out.println("Class could not be found.");
        }

        if (visitor.getRole() == UserRole.ADMIN) {
            adminMenu();
        } else if (visitor.getRole() == UserRole.EMPLOYEE) {
            employeeMenu();
        } else {
            customerMenu();
        }
    }
}
