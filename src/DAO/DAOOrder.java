package DAO;

import Model.Order;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class DAOOrder {
    
    public boolean isExist(String maDon) {
        String sql = "SELECT order_id FROM orders WHERE order_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, maDon);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL!");
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return false;
    }
    
    public void addOrder(Order o) {
        String sql = "INSERT INTO orders (order_id, order_date, table_name, payment_status) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, o.getOrderId());
            stm.setString(2, o.getOrderDate());
            stm.setString(3, o.getTableName());
            stm.setString(4, o.getPaymentStatus());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }
    
    public ArrayList<Order> getAllOrderNotPay() {
        ArrayList<Order> res = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE payment_status = 'Chưa thanh toán' ORDER BY order_id";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return res;
        try (Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                String id = rs.getString("order_id");
                String date = rs.getString("order_date");
                String table = rs.getString("table_name");
                String pay = rs.getString("payment_status");
                Order a = new Order(id, date, table, pay);
                res.add(a);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL!");
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return res;
    }
    
    public void updatePaid(String id) {
        String sql = "UPDATE orders SET payment_status = 'Đã thanh toán' WHERE order_id = ? LIMIT 1";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }
    
    public ArrayList<String> getOrderIdByDate(String date) {
        ArrayList<String> res = new ArrayList<>();
        String sql = "SELECT order_id FROM orders WHERE order_date = ? AND payment_status = 'Đã thanh toán'";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return res;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, date);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    String a = rs.getString("order_id");
                    res.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL!");
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return res;
    }
}
