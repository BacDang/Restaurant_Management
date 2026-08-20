package DAO;

import Model.Customer;
import java.util.ArrayList;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DAOCustomer {

    public boolean isExist(String sdt) {
        String sql = "SELECT cus_phone FROM customer WHERE cus_phone = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, sdt);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL!");
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return false;
    }

    public void updateCus(String sdt, int tongTien) {
        String sql = "UPDATE customer SET cus_amount = cus_amount + ?, cus_orders = cus_orders + 1 WHERE cus_phone = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, tongTien);
            stm.setString(2, sdt);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public void addCus(Customer a) {
        String sql = "INSERT INTO customer (cus_name, cus_phone, cus_amount, cus_orders) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, a.getTenKhach());
            stm.setString(2, a.getSoDienThoai());
            stm.setInt(3, a.getTongChi());
            stm.setInt(4, a.getSoLuongDon());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public ArrayList<Customer> getAllCus() {
        ArrayList<Customer> dsKhach = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return dsKhach;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String tenKhach = rs.getString("cus_name");
                String soDienThoai = rs.getString("cus_phone");
                int tongChi = rs.getInt("cus_amount");
                int soLuongDon = rs.getInt("cus_orders");
                Customer a = new Customer(tenKhach, soDienThoai, tongChi, soLuongDon);
                dsKhach.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL!");
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return dsKhach;
    }

    public ArrayList<Customer> findCusByName(String ten) {
        ArrayList<Customer> dsKhach = new ArrayList<>();
        String sql = "SELECT * FROM customer WHERE cus_name LIKE ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return dsKhach;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, "%" + ten + "%");
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Customer b = new Customer(
                            rs.getString("cus_name"),
                            rs.getString("cus_phone"),
                            rs.getInt("cus_amount"),
                            rs.getInt("cus_orders")
                    );
                    dsKhach.add(b);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return dsKhach;
    }
}
