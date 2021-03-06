package com.revature.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {

    private static Connection connection;

    private final static String url = "url";
    private final static String username = "username";
    private final static String password = "password";

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
