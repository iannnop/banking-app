package com.revature.user;

import java.util.ArrayList;

public interface UserDAO {

    User getUser(int id);
    User getUser(String username);
    User getUserFromAccount(int accountId);
    User getCustomer(String username);
    User getEmployee(String username);
    User getAdmin(String username);
    ArrayList<User> getAllUsers();
    ArrayList<User> getAllCustomers();
    ArrayList<User> getAllEmployees();
    ArrayList<User> getAllAdmins();
    User createUser(UserRole role, String username, String password, String firstName, String lastName, String email);
    void updateUser(User user);
    void deleteUser(User user);
}
