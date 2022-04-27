package com.revature.user;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTest {

    @Test
    public void createUser() {
        User user = new User(1, UserRole.CUSTOMER, "daniel", "123456",
                new Timestamp(System.currentTimeMillis()), "Daniel", "Ray", "daniel@email.com");

        UserDAOImpl userDAO = mock(UserDAOImpl.class);
        when(userDAO.createUser(user.getRole(), user.getUsername(), user.getPassword(),
                user.getFirstName(), user.getLastName(), user.getEmail())).thenReturn(user);

        User createdUser = userDAO.createUser(user.getRole(), user.getUsername(), user.getPassword(),
                user.getFirstName(), user.getLastName(), user.getEmail());

        assertEquals(user, createdUser);
    }

    @Test
    public void getCustomer() {
        User user = new User(1, UserRole.CUSTOMER, "daniel", "123456",
                new Timestamp(System.currentTimeMillis()), "Daniel", "Ray", "daniel@email.com");

        UserDAOImpl userDAO = mock(UserDAOImpl.class);
        when(userDAO.getCustomer("daniel")).thenReturn(user);

        assertEquals(user, userDAO.getCustomer("daniel"));
    }

    @Test
    public void getAllUsers() {
        User userOne = new User(1, UserRole.CUSTOMER, "daniel", "123456",
                new Timestamp(System.currentTimeMillis()), "Daniel", "Ray", "daniel@email.com");
        User userTwo = new User(2, UserRole.EMPLOYEE, "jason", "123456",
                new Timestamp(System.currentTimeMillis()), "Jason", "Ray", "daniel@email.com");
        User userThree = new User(3, UserRole.ADMIN, "admin", "admin",
                new Timestamp(System.currentTimeMillis()), "ADMIN", "ADMIN", "admin@email.com");
        ArrayList<User> databaseUsers = new ArrayList<>(Arrays.asList(userOne, userTwo, userThree));

        UserDAOImpl userDAO = mock(UserDAOImpl.class);
        when(userDAO.getAllUsers()).thenReturn(new ArrayList<>(Arrays.asList(userOne, userTwo, userThree)));

        assertEquals(databaseUsers, userDAO.getAllUsers());
    }
}
