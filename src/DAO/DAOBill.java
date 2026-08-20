package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DAOBill {

    public ArrayList<String> getBillIdByDate(String date) {
        ArrayList<String> res = new ArrayList<>();
        String sql = "SELECT order_id FROM orders WHERE order_date = ?";
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

    public List<String[]> getChiTietDon(String maDon) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT od.food_name, od.food_quantity, f.food_cost, (od.food_quantity * f.food_cost) AS thanh_tien "
                   + "FROM order_detail od "
                   + "JOIN food f ON od.food_name = f.food_name "
                   + "WHERE od.order_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new String[]{
                        rs.getString("food_name"),
                        rs.getString("food_quantity"),
                        rs.getString("food_cost"),
                        rs.getString("thanh_tien")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return list;
    }

    public List<String[]> getChiTietDonTheoBan(String tenBan) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT o.order_id, od.food_name, od.food_quantity, f.food_cost, (od.food_quantity * f.food_cost) AS thanh_tien "
                   + "FROM orders o "
                   + "JOIN order_detail od ON o.order_id = od.order_id "
                   + "JOIN food f ON od.food_name = f.food_name "
                   + "WHERE o.table_name = ? "
                   + "ORDER BY o.order_id";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenBan);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new String[]{
                        rs.getString("order_id"),
                        rs.getString("food_name"),
                        rs.getString("food_quantity"),
                        rs.getString("food_cost"),
                        rs.getString("thanh_tien")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return list;
    }

    public String getDate(String id) {
        String res = "";
        String sql = "SELECT order_date FROM orders WHERE order_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return res;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    res = rs.getString("order_date");
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
