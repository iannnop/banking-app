package com.revature.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {

    private static Connection connection;

    private final static String url = "jdbc:postgresql://heffalump.db.elephantsql.com:5432/pwglpqfu";
    private final static String username = "pwglpqfu";
    private final static String password = "gU8C96f3nEJN5n285gP5F9HM3-iUbdD5";

    private ConnectionManager() {}
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url, username, password);
            }
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
