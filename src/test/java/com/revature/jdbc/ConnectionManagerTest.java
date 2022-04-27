package com.revature.jdbc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class ConnectionManagerTest {

    @Test
    public void connectionTest() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }
}