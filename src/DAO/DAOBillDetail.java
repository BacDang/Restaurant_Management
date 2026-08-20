package DAO;

import java.sql.*;
import javax.swing.JOptionPane;

public class DAOBillDetail {

    public long getBillCost(String billId) {
        long res = 0;
        String sql = "SELECT SUM(bd.food_quantity * f.food_cost) AS bill_cost "
                + "FROM order_detail bd JOIN food f ON bd.food_name = f.food_name "
                + "WHERE bd.order_id = ?";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return res;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, billId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    res = rs.getLong("bill_cost");
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
