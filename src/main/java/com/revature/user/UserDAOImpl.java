package com.revature.user;

import com.revature.jdbc.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;

public class UserDAOImpl implements UserDAO {

    @Override
    public User getUser(int id) {
        Connection connection = ConnectionManager.getConnection();

        User user = null;
        try {
            String sql = "SELECT * FROM \"User\" WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                UserRole role = UserRole.valueOf(rs.getString("role"));
                String username = rs.getString("username");
                String password = rs.getString("password");
                Timestamp userCreated = rs.getTimestamp("user_created");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                user = new User(id, role, username, password, userCreated, firstName, lastName, email, phone, address);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User getUser(String username) {
        Connection connection = ConnectionManager.getConnection();

        User user = null;
        try {
            String sql = "SELECT * FROM \"User\" WHERE username = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                UserRole role = UserRole.valueOf(rs.getString("role"));
                String password = rs.getString("password");
                Timestamp userCreated = rs.getTimestamp("user_created");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                user = new User(id, role, username, password, userCreated, firstName, lastName, email, phone, address);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User getUserFromAccount(int accountId) {
        Connection connection = ConnectionManager.getConnection();

        User user = null;
        try {
            String sql = "SELECT * FROM \"UserAccount\" WHERE account_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = getUser(rs.getInt("user_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User getCustomer(String username) {
        Connection connection = ConnectionManager.getConnection();

        User user = null;
        try {
            String sql = "SELECT * FROM \"User\" WHERE username = ? AND role = 'CUSTOMER'";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                UserRole role = UserRole.valueOf(rs.getString("role"));
                String password = rs.getString("password");
                Timestamp userCreated = rs.getTimestamp("user_created");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                user = new User(id, role, username, password, userCreated, firstName, lastName, email, phone, address);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User getEmployee(String username) {
        Connection connection = ConnectionManager.getConnection();

        User user = null;
        try {
            String sql = "SELECT * FROM \"User\" WHERE username = ? AND role = 'EMPLOYEE'";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                UserRole role = UserRole.valueOf(rs.getString("role"));
                String password = rs.getString("password");
                Timestamp userCreated = rs.getTimestamp("user_created");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                user = new User(id, role, username, password, userCreated,
                        firstName, lastName, email, phone, address);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User getAdmin(String username) {
        Connection connection = ConnectionManager.getConnection();

        User user = null;
        try {
            String sql = "SELECT * FROM \"User\" WHERE username = ? AND role = 'ADMIN'";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                UserRole role = UserRole.valueOf(rs.getString("role"));
                String password = rs.getString("password");
                Timestamp userCreated = rs.getTimestamp("user_created");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                user = new User(id, role, username, password, userCreated, firstName, lastName, email, phone, address);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        Connection connection = ConnectionManager.getConnection();

        ArrayList<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"User\" ORDER");

            while (rs.next()) {
                int id = rs.getInt("id");
                UserRole role = UserRole.valueOf(rs.getString("role"));
                String username = rs.getString("username");
                String password = rs.getString("password");
                Timestamp userCreated = rs.getTimestamp("user_created");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                users.add(new User(id, role, username, password, userCreated,
                        firstName, lastName, email, phone, address));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public ArrayList<User> getAllCustomers() {
        Connection connection = ConnectionManager.getConnection();

        ArrayList<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"User\" WHERE role = 'CUSTOMER'");

            while (rs.next()) {
                int id = rs.getInt("id");
                UserRole role = UserRole.valueOf(rs.getString("role"));
                String username = rs.getString("username");
                String password = rs.getString("password");
                Timestamp userCreated = rs.getTimestamp("user_created");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                users.add(new User(id, role, username, password, userCreated,
                        firstName, lastName, email, phone, address));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public ArrayList<User> getAllEmployees() {
        Connection connection = ConnectionManager.getConnection();

        ArrayList<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"User\" WHERE role = 'EMPLOYEE'");

            while (rs.next()) {
                int id = rs.getInt("id");
                UserRole role = UserRole.valueOf(rs.getString("role"));
                String username = rs.getString("username");
                String password = rs.getString("password");
                Timestamp userCreated = rs.getTimestamp("user_created");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                users.add(new User(id, role, username, password, userCreated,
                        firstName, lastName, email, phone, address));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public ArrayList<User> getAllAdmins() {
        Connection connection = ConnectionManager.getConnection();

        ArrayList<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"User\" WHERE role = 'ADMIN'");

            while (rs.next()) {
                int id = rs.getInt("id");
                UserRole role = UserRole.valueOf(rs.getString("role"));
                String username = rs.getString("username");
                String password = rs.getString("password");
                Timestamp userCreated = rs.getTimestamp("user_created");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                users.add(new User(id, role, username, password, userCreated,
                        firstName, lastName, email, phone, address));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public User createUser(UserRole role, String username, String password,
                           String firstName, String lastName, String email) {
        Connection connection = ConnectionManager.getConnection();

        User user = null;
        final Timestamp CURRENT_TIME = new Timestamp(System.currentTimeMillis());
        try {
            String sql = "INSERT INTO \"User\" " +
                    "(role, username, password, user_created, first_name, last_name, email) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, String.valueOf(role));
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setTimestamp(4, CURRENT_TIME);
            pstmt.setString(5, firstName);
            pstmt.setString(6, lastName);
            pstmt.setString(7, email);

            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt("id");
                user = new User(id, role, username, password, CURRENT_TIME, firstName, lastName, email);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void updateUser(User user) {
        Connection connection = ConnectionManager.getConnection();

        try {
            String sql = "UPDATE \"User\" " +
                    "SET role = ?, username = ?, password = ?, " +
                    "first_name = ?, last_name = ?, " +
                    "email = ?, phone = ?, address = ? " +
                    "WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(user.getRole()));
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getFirstName());
            pstmt.setString(5, user.getLastName());
            pstmt.setString(6, user.getEmail());
            pstmt.setString(7, user.getPhone());
            pstmt.setString(8, user.getAddress());
            pstmt.setInt(9, user.getId());

            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(User user) {
        Connection connection = ConnectionManager.getConnection();

        try {
            String sql = "DELETE FROM \"User\" WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, user.getId());

            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
