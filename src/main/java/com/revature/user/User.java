package com.revature.user;

import com.revature.account.Account;
import com.revature.account.AccountDAOImpl;
import com.revature.account.AccountStatus;
import com.revature.exception.InvalidInputException;
import com.revature.exception.UnauthorizedException;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class User implements Serializable {
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
    private ArrayList<Account> accounts;

    /*
    * User constructor with all required fields
    * */
    public User(int id, UserRole role, String username, String password, Timestamp userCreated,
                String firstName, String lastName, String email) {
        AccountDAOImpl accountDAO = new AccountDAOImpl();

        this.id = id;
        this.role = role;
        this.username = username;
        this.password = password;
        this.userCreated = userCreated;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accounts = accountDAO.getUserAccounts(id);
    }

    /*
    * User constructor with all fields
    * */
    public User(int id, UserRole role, String username, String password, Timestamp userCreated, String firstName, String lastName,
                String email, String phone, String address) {
        AccountDAOImpl accountDAO = new AccountDAOImpl();

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
        this.accounts = accountDAO.getUserAccounts(id);
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
    public ArrayList<Account> getAccounts() {
        return accounts;
    }
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

    public void applyForAccount(double startingBalance) throws InvalidInputException {
        if (startingBalance <= 0) {
            throw new InvalidInputException("Starting balance cannot be less than or equal to 0");
        }
        AccountDAOImpl accountDAO = new AccountDAOImpl();

        Account account = accountDAO.createAccount(id);
        account.deposit(startingBalance, "Starting balance deposit");
        accounts.add(account);
    }
    public void applyForJointAccount(double startingBalance, User otherUser) throws InvalidInputException {
        if (startingBalance <= 0) {
            throw new InvalidInputException("Starting balance cannot be less than or equal to 0");
        }
        AccountDAOImpl accountDAO = new AccountDAOImpl();

        Account account = accountDAO.createJointAccount(id, otherUser.getId());
        account.deposit(startingBalance, "Starting balance deposit");

        ArrayList<Account> otherUserAccounts = otherUser.getAccounts();
        accounts.add(account);
        otherUserAccounts.add(account);
    }
    public void cancelAccount(Account account) throws UnauthorizedException {
        if (role.compareTo(UserRole.ADMIN) < 0) {
            throw new UnauthorizedException("User not authorized to cancel accounts");
        }
        AccountDAOImpl accountDAO = new AccountDAOImpl();

        account.setStatus(AccountStatus.CANCELLED);
        accountDAO.updateAccount(account);
    }
    public void deleteAccount(Account account) throws UnauthorizedException {
        if (role.compareTo(UserRole.ADMIN) < 0) {
            throw new UnauthorizedException("User not authorized to delete accounts");
        }
        AccountDAOImpl accountDAO = new AccountDAOImpl();

        accountDAO.deleteAccount(account);
        accounts.remove(account);
    }
    public void approveAccount(Account account) throws UnauthorizedException {
        if (role.compareTo(UserRole.EMPLOYEE) < 0) {
            throw new UnauthorizedException("User not authorized to approve accounts");
        }
        AccountDAOImpl accountDAO = new AccountDAOImpl();

        account.setStatus(AccountStatus.ACTIVE);
        accountDAO.updateAccount(account);
    }
    public void denyAccount(Account account) throws UnauthorizedException {
        if (role.compareTo(UserRole.EMPLOYEE) < 0) {
            throw new UnauthorizedException("User not authorized to deny accounts");
        }
        AccountDAOImpl accountDAO = new AccountDAOImpl();

        account.setStatus(AccountStatus.DENIED);
        accountDAO.updateAccount(account);
    }
}
