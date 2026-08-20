package DAO;

import Model.Food;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DAOFood {

    public ArrayList<Food> getAllFood() {
        ArrayList<Food> arlRes = new ArrayList<>();
        String sql = "SELECT * FROM food ORDER BY food_id";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return arlRes;
        try (Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                String id = rs.getString("food_id");
                String name = rs.getString("food_name");
                String category = rs.getString("food_category");
                int cost = rs.getInt("food_cost");
                Food a = new Food(id, name, category, cost);
                arlRes.add(a);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOFood.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL!");
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return arlRes;
    }

    public boolean addMon(Food a) {
        String sql = "INSERT INTO food (food_id, food_name, food_category, food_cost) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, a.getFoodId());
            stm.setString(2, a.getFoodName());
            stm.setString(3, a.getFoodCategory());
            stm.setInt(4, a.getFoodCost());
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + ex.getMessage());
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public boolean changeMon(Food a) {
        String updateSql = "UPDATE food SET food_name = ?, food_category = ?, food_cost = ? WHERE food_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement stm = conn.prepareStatement(updateSql)) {
            stm.setString(1, a.getFoodName());
            stm.setString(2, a.getFoodCategory());
            stm.setInt(3, a.getFoodCost());
            stm.setString(4, a.getFoodId());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public boolean deleteMon(String ten) {
        String sql = "DELETE FROM food WHERE food_name = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, ten);
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public ArrayList<Food> searchTen(String tenMon) {
        ArrayList<Food> arlRes = new ArrayList<>();
        String sql = "SELECT * FROM food WHERE food_name LIKE ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return arlRes;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, "%" + tenMon + "%");
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("food_id");
                    String ten = rs.getString("food_name");
                    String loaiMon = rs.getString("food_category");
                    int giaMon = rs.getInt("food_cost");
                    Food a = new Food(id, ten, loaiMon, giaMon);
                    arlRes.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return arlRes;
    }

    public int getCost(String name) {
        int res = 0;
        String sql = "SELECT food_cost FROM food WHERE food_name = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return res;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, name);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    res = rs.getInt("food_cost");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + ex.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return res;
    }
}
