package com.revature.db;

import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertNotNull;

public class ConnectionManagerTest {

    @Test
    public void connectionTest() {
        Connection connection = ConnectionManager.connect();
        assertNotNull(connection);
    }
}