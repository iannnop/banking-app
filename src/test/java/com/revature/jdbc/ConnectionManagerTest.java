package com.revature.jdbc;

import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertNotNull;

public class ConnectionManagerTest {

    @Test
    public void connectionTest() {
        Connection connection = ConnectionManager.getConnection();
        assertNotNull(connection);
    }
}