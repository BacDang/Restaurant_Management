package DAO;

import Model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DAOEmployee {

    public ArrayList<Employee> getAllEmployee() {
        ArrayList<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee ORDER BY employee_id";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Employee e = new Employee(
                    rs.getString("employee_id"),
                    rs.getString("employee_name"),
                    rs.getString("employee_birth"),
                    rs.getString("employee_role"),
                    rs.getString("employee_sex"),
                    rs.getString("employee_phone"));
                list.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL!");
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return list;
    }
    
    public boolean isExistId(String id) {
        String sql = "SELECT employee_id FROM employee WHERE employee_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, id);
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
    
    public boolean addEmployee(Employee a) {
        String sql = "INSERT INTO employee (employee_id, employee_name, employee_birth, employee_role, employee_sex, employee_phone) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, a.getId());
            stm.setString(2, a.getName());
            stm.setString(3, a.getBirth());
            stm.setString(4, a.getRole());
            stm.setString(5, a.getSex());
            stm.setString(6, a.getPhone());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }
    
    public boolean updateEmployee(Employee a) {
        String sql = "UPDATE employee SET employee_name = ?, employee_birth = ?, employee_role = ?, "
                + "employee_sex = ?, employee_phone = ? WHERE employee_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, a.getName());
            stm.setString(2, a.getBirth());
            stm.setString(3, a.getRole());
            stm.setString(4, a.getSex());
            stm.setString(5, a.getPhone());
            stm.setString(6, a.getId());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public void deleteEmployee(String id) {
        String sql = "DELETE FROM employee WHERE employee_id = ? LIMIT 1";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, id);
            stm.executeUpdate();
            JOptionPane.showMessageDialog(null, "Xoá thành công Nhân viên mã: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }
}
