package UI_NhanVien.ThuNgan;

import DAO.DAOFood;
import DAO.DAOOrderDetail;
import Model.OrderFood;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;

public class UIOrderDetail extends JFrame {
    
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTong;
    private final String tenBan;
    private final String maDon;
    
    public UIOrderDetail(String maDon, String tenBan) {
        this.maDon = maDon;
        this.tenBan = tenBan;       
        setTitle("Chi Tiết Đơn - " + maDon);
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 247, 250));

        add(createTitle(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);        
       
        loadData();
    }
    
    private JLabel createTitle() {
        JLabel title = new JLabel("HÓA ĐƠN BÀN: " + tenBan, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(52, 73, 94));
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        return title;
    }
    
    private JPanel createMainPanel() {
        model = new DefaultTableModel(
            new Object[]{"Tên món", "Số lượng", "Giá", "Thành tiền"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        table = new JTable(model);
        styleTable(table);   
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JPanel panel = new JPanel(new BorderLayout()); 
        JScrollPane sp = new JScrollPane(table);
    
        sp.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        sp.getViewport().setBackground(Color.WHITE);    
        panel.add(sp, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(236, 240, 241));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblTong = new JLabel("Tổng: 0 VND", SwingConstants.RIGHT);
        lblTong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTong.setForeground(new Color(231, 76, 60));

        footer.add(lblTong, BorderLayout.EAST);

        return footer;
    }
    
    private void styleTable(JTable t) {
        t.setRowHeight(36);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setSelectionBackground(new Color(52, 152, 219));
        t.setSelectionForeground(Color.WHITE);
        t.setGridColor(new Color(230, 230, 230));
        t.setShowGrid(true);

        JTableHeader header = t.getTableHeader();
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        t.setDefaultRenderer(Object.class, center);
    }       

    private void loadData() {
        long tong = 0;
        model.setRowCount(0);
        ArrayList<OrderFood> arl = new DAOOrderDetail().getOrderDetail(maDon);
        DAOFood daoFood = new DAOFood();
        for (OrderFood o : arl) {
            String tenMon = o.getTenMon();
            int soLuong = o.getSoLuongMon();
            int gia = daoFood.getCost(tenMon);
            long thanhTien = (long) soLuong * gia;
            tong += thanhTien;
            model.addRow(new Object[]{
                tenMon,
                String.valueOf(soLuong),
                String.format("%,d VND", gia),
                String.format("%,d VND", thanhTien)
            });
        }
        lblTong.setText("Tổng: " + String.format("%,d VND", tong));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UIOrderDetail("ODR001", "B001").setVisible(true));
    }
}
