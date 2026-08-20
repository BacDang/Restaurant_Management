package DAO;

import Model.User;
import java.sql.*;
import javax.swing.JOptionPane;

public class DAOUser {

    public User login(String username, String password) {
        User u = null;
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL!");
            return null;
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    u = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return u;
    }
    
    public boolean addUser(User a) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL!");
            return false;
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getUsername());
            stmt.setString(2, a.getPassword());
            stmt.setString(3, a.getRole());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ee) {
            ee.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + ee.getMessage());
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public void deleteUser(String id) {
        String sql = "DELETE FROM users WHERE username = ? LIMIT 1";
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL!");
            return;
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Xoá thành công Tài khoản: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }
}