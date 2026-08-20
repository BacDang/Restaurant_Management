package DAO;

import Model.OrderDetail;
import Model.OrderFood;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DAOOrderDetail {

    public void addOrderDetail(OrderDetail od) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;
        try {
            String checkSql = "SELECT food_quantity FROM order_detail WHERE order_id = ? AND food_name = ?";
            boolean exists = false;
            int currentQty = 0;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, od.getOrderId());
                checkStmt.setString(2, od.getFoodName());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        exists = true;
                        currentQty = rs.getInt("food_quantity");
                    }
                }
            }
            if (exists) {
                String updateSql = "UPDATE order_detail SET food_quantity = ? WHERE order_id = ? AND food_name = ? LIMIT 1";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, currentQty + od.getFoodQuantity());
                    updateStmt.setString(2, od.getOrderId());
                    updateStmt.setString(3, od.getFoodName());
                    updateStmt.executeUpdate();
                }
            } else {
                String insertSql = "INSERT INTO order_detail (order_id, food_name, food_quantity) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, od.getOrderId());
                    insertStmt.setString(2, od.getFoodName());
                    insertStmt.setInt(3, od.getFoodQuantity());
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public void decreaseQuantity(String id, String foodName) {
        String sql = "UPDATE order_detail SET food_quantity = food_quantity - 1 WHERE order_id = ? AND food_name = ? LIMIT 1";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, id);
            stm.setString(2, foodName);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public void deleteOrderDetail(String id, String foodName) {
        String sql = "DELETE FROM order_detail WHERE order_id = ? AND food_name = ? LIMIT 1";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, id);
            stm.setString(2, foodName);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public ArrayList<OrderFood> getOrderDetail(String id) {
        ArrayList<OrderFood> res = new ArrayList<>();
        String sql = "SELECT food_name, food_quantity FROM order_detail WHERE order_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return res;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("food_name");
                    int quantity = rs.getInt("food_quantity");
                    OrderFood oF = new OrderFood(name, quantity);
                    res.add(oF);
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

    public List<String[]> getDanhSachDon() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT o.order_id, o.table_name, COALESCE(SUM(od.food_quantity * f.food_cost), 0) AS tong_tien "
                   + "FROM orders o "
                   + "LEFT JOIN order_detail od ON o.order_id = od.order_id "
                   + "LEFT JOIN food f ON od.food_name = f.food_name "
                   + "GROUP BY o.order_id, o.table_name "
                   + "ORDER BY o.order_id";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("order_id"),
                    rs.getString("table_name"),
                    rs.getString("tong_tien")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return list;
    }

    public long getOrderCost(String orderId) {
        long res = 0;
        String sql = "SELECT SUM(o.food_quantity * f.food_cost) AS order_cost "
                + "FROM order_detail o JOIN food f ON o.food_name = f.food_name "
                + "WHERE o.order_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return res;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, orderId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    res = rs.getLong("order_cost");
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

    public ArrayList<String[]> getMostCost() {
        ArrayList<String[]> res = new ArrayList<>();
        String sql = "SELECT o.food_name, SUM(o.food_quantity * f.food_cost) AS tong "
                + "FROM order_detail o "
                + "JOIN food f ON o.food_name = f.food_name "
                + "GROUP BY o.food_name ORDER BY tong DESC";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return res;
        try (Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                String n = rs.getString("food_name");
                long tong = rs.getLong("tong");
                res.add(new String[]{n, String.valueOf(tong)});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL!");
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return res;
    }
    
    public ArrayList<String[]> getMostQuantity() {
        ArrayList<String[]> res = new ArrayList<>();
        String sql = "SELECT o.food_name, SUM(o.food_quantity) AS quantity "
                + "FROM order_detail o "
                + "GROUP BY o.food_name ORDER BY quantity DESC";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return res;
        try (Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                String n = rs.getString("food_name");
                long tong = rs.getLong("quantity");
                res.add(new String[]{n, String.valueOf(tong)});
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
