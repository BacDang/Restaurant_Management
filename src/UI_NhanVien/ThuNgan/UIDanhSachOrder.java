package UI_NhanVien.ThuNgan;

import List_bill.Components.ButtonEditor;
import List_bill.Components.ButtonRenderer;
import List_Table.Components.ButtonStyle;
import DAO.DAOOrder;
import DAO.DAOOrderDetail;
import Model.Order;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;

public class UIDanhSachOrder extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel model;

    public UIDanhSachOrder() {       
        setTitle("Danh Sách Đơn Hàng - Thu Ngân");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 247, 250));
        
        add(createTitle(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        loadData();
    }
    
    private JLabel createTitle() {
        JLabel title = new JLabel("DANH SÁCH ĐƠN HÀNG", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(52, 73, 94));
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        return title;
    }
    
    private JPanel createMainPanel() {
        model = new DefaultTableModel(new Object[]{"Mã đơn", "Bàn", "Tổng tiền (VND)", "Chi tiết", "Thanh toán"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 3 || col == 4;
            }
        };

        table = new JTable(model);
        styleTable(table);     
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < 3; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumn("Chi tiết").setCellRenderer(new ButtonRenderer("Xem"));
        table.getColumn("Chi tiết").setCellEditor(new ButtonEditor(new JCheckBox(), "Xem", table, model, this));
        table.getColumn("Thanh toán").setCellRenderer(new ButtonRenderer("Thanh toán"));
        table.getColumn("Thanh toán").setCellEditor(new ButtonEditor(new JCheckBox(), "Thanh toán", table, model, this));

        JPanel panel = new JPanel(new BorderLayout()); 
        JScrollPane sp = new JScrollPane(table);
    
        sp.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        sp.getViewport().setBackground(Color.WHITE);    
        panel.add(sp, BorderLayout.CENTER);
        
        return panel;
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
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(245, 247, 250));
        footer.setBorder(new EmptyBorder(5, 15, 5, 15));
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        JButton refreshBtn = ButtonStyle.create("Làm mới", new Color(46, 204, 113));
        refreshBtn.addActionListener(e -> loadData());
        rightPanel.add(refreshBtn);
    
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
    
        JButton xemBanBtn = ButtonStyle.create("<-  Xem ds Bàn", new Color(46, 204, 113)); 
        xemBanBtn.addActionListener(e -> {
            new UI_ThuNgan().setVisible(true);
            dispose();
        });
    
        leftPanel.add(xemBanBtn);

        footer.add(leftPanel, BorderLayout.WEST);
        footer.add(rightPanel, BorderLayout.EAST); 

        return footer;
    }
    
    public void loadData() {
        model.setRowCount(0);
        ArrayList<Order> arl = new DAOOrder().getAllOrderNotPay();
        DAOOrderDetail daoOD = new DAOOrderDetail();
        for (Order r : arl) {
            String maDon = r.getOrderId();
            String tenBan = r.getTableName();
            long tongTien = daoOD.getOrderCost(maDon);
            model.addRow(new Object[]{maDon, tenBan, String.valueOf(tongTien), "Xem", "Thanh toán"});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UIDanhSachOrder().setVisible(true));
    }
}
