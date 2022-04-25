package com.revature.user;

import com.revature.account.Account;
import com.revature.account.AccountDAOImpl;
import com.revature.transaction.TransactionDAOImpl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class User {
    private static final AccountDAOImpl accountDAO = new AccountDAOImpl();

    private final int id;
    private UserRole role;
    private String username;
    private String password;
    private final Timestamp userCreated;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private List<Account> accounts;

    /*
    * User constructor with all required fields
    * */
    public User(int id, UserRole role, String username, String password, Timestamp userCreated,
                String firstName, String lastName, String email) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.password = password;
        this.userCreated = userCreated;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accounts = accountDAO.getAllAccounts(id);
    }

    /*
    * User constructor with all fields
    * */
    public User(int id, UserRole role, String username, String password, Timestamp userCreated, String firstName, String lastName,
                String email, String phone, String address) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.password = password;
        this.userCreated = userCreated;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.accounts = accountDAO.getAllAccounts(id);
    }

    public int getId() {
        return id;
    }
    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Timestamp getUserCreated() {
        return userCreated;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public List<Account> getAccounts() {
        return accounts;
    }
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    } //TODO Questionable necessity

    public Account getAccount(int index) {
        return accounts.get(index);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role=" + role +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userCreated=" + userCreated +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", accounts=" + accounts +
                '}';
    }

    public void addAccount(double startingBalance) {
        accounts.add(accountDAO.createAccount(id, startingBalance));
    }
}
