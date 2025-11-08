package dao;

import db.DatabaseManager;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User validateUser(String username, String password) throws SQLException {
        String sql = "SELECT user_id, username, role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("role"));
                }
            }
        }
        return null;
    }

    // METHOD for registration
    public void addUser(String username, String password) throws SQLException {
        // First, check if username already exists
        String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Username '" + username + "' already exists.");
            }
        }
        // If not, insert the new user with 'USER' role by default
        String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'USER')";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.executeUpdate();
        }
    }

    // NEW METHOD: Get all users
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, role FROM users ORDER BY username";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        }
        return users;
    }

    // NEW METHOD: Update user role
    public void updateUserRole(int userId, String newRole) throws SQLException {
        // Check if the role is valid (ADMIN, USER, TRACKER)
        if (!("ADMIN".equalsIgnoreCase(newRole) || "USER".equalsIgnoreCase(newRole) || "TRACKER".equalsIgnoreCase(newRole))) {
            throw new IllegalArgumentException("Invalid role specified: " + newRole);
        }
        String sql = "UPDATE users SET role = ? WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newRole);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
    }

    // NEW METHOD: Delete user
    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }
}