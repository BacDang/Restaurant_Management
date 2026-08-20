package DAO;

import Model.Table;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DAOTable {

    public ArrayList<Table> getAllTable() {
        ArrayList<Table> list = new ArrayList<>();
        String sql = "SELECT * FROM tables ORDER BY table_name";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return list;
        try (Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                Table b = new Table(
                        rs.getString("table_name"),
                        rs.getString("table_status"),
                        rs.getString("table_cook"),
                        rs.getString("table_serve"),
                        rs.getString("table_pay"),
                        rs.getString("order_id"),
                        rs.getString("table_seat")
                );
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL!");
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return list;
    }

    public boolean addTable(Table a) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try {
            String checkSql = "SELECT table_name FROM tables WHERE table_name = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, a.getTableName());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        return false;
                    }
                }
            }
            String insertSql = "INSERT INTO tables (table_name, table_seat, table_status, table_cook, table_serve, table_pay, order_id) VALUES (?, ?, ?, 'Chưa nấu', 'Chưa phục vụ', 'Chưa thanh toán', null)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, a.getTableName());
                insertStmt.setString(2, a.getTableSeat());
                insertStmt.setString(3, a.getTableStatus());
                return insertStmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + ex.getMessage());
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public boolean updateTable(String tenBan, String soGhe) {
        String sqlUpdate = "UPDATE tables SET table_seat = ? WHERE table_name = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
            stmtUpdate.setString(1, soGhe);
            stmtUpdate.setString(2, tenBan);
            return stmtUpdate.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + ex.getMessage());
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public boolean deleteTable(String tenBan) {
        String sql = "DELETE FROM tables WHERE table_name = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenBan);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + ex.getMessage());
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public String getOrderId(String tenBan) {
        String maDon = null;
        String sql = "SELECT order_id FROM tables WHERE table_name = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return null;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, tenBan);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    maDon = rs.getString("order_id");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOTable.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.closeQuietly(conn);
        }
        return maDon;
    }

    public boolean addNewOrderId(String tenBan) {
        Random random = new Random();
        String maDon;
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try {
            String check1 = "SELECT table_name FROM tables WHERE order_id = ?";
            String check2 = "SELECT order_id FROM orders WHERE order_id = ?";
            String updateSql = "UPDATE tables SET order_id = ? WHERE table_name = ? LIMIT 1";

            while (true) {
                int so = random.nextInt(9999) + 1;
                maDon = String.format("ODR%05d", so);

                boolean exists = false;
                try (PreparedStatement stm1 = conn.prepareStatement(check1)) {
                    stm1.setString(1, maDon);
                    try (ResultSet rs1 = stm1.executeQuery()) {
                        if (rs1.next()) exists = true;
                    }
                }
                if (!exists) {
                    try (PreparedStatement stm2 = conn.prepareStatement(check2)) {
                        stm2.setString(1, maDon);
                        try (ResultSet rs2 = stm2.executeQuery()) {
                            if (rs2.next()) exists = true;
                        }
                    }
                }

                if (!exists) {
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setString(1, maDon);
                        updateStmt.setString(2, tenBan);
                        return updateStmt.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public boolean resetTable(String tenBan) {
        String sql = "UPDATE tables SET table_status = 'Trống', "
                + "table_cook = 'Chưa nấu', "
                + "table_serve = 'Chưa phục vụ', "
                + "table_pay = 'Chưa thanh toán', "
                + "order_id = null WHERE table_name = ? LIMIT 1";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, tenBan);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + ex.getMessage());
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public boolean updateStatus(String tenBan, String newValue) {
        String sql = "UPDATE tables SET table_status = ? WHERE table_name = ? LIMIT 1";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, newValue);
            stm.setString(2, tenBan);
            stm.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + ex.getMessage());
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public boolean updateServe(String tenBan, String newValue) {
        String sql = "UPDATE tables SET table_serve = ? WHERE table_name = ? LIMIT 1";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, newValue);
            stm.setString(2, tenBan);
            stm.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + ex.getMessage());
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }

    public boolean updateCook(String tenBan, String newValue) {
        String sql = "UPDATE tables SET table_cook = ? WHERE table_name = ? LIMIT 1";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, newValue);
            stm.setString(2, tenBan);
            stm.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + ex.getMessage());
            return false;
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }
    
    public void updatePaid(String name) {
        String sql = "UPDATE tables SET table_pay = 'Đã thanh toán' WHERE table_name = ? LIMIT 1";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, name);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
        } finally {
            DBConnection.closeQuietly(conn);
        }
    }
}
