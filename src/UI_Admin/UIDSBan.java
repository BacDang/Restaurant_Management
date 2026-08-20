package UI_Admin;

import DAO.DAOTable;
import Model.Table;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

public class UIDSBan extends JFrame {
    private static final long serialVersionUID = 1L;
    private DefaultTableModel model;
    
    public UIDSBan() {
        initComponent();
        DAOTable daoTable = new DAOTable();
        ArrayList<Table> a = daoTable.getAllTable();
        showListTable(a);
    }
    
    private void initComponent() {
        setTitle("Quản lý Bàn");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);
        
        JLabel tieuDe = new JLabel("DANH SÁCH BÀN", SwingConstants.CENTER);
        tieuDe.setBounds(250, 30, 300, 30);
        add(tieuDe);
        
        model = new DefaultTableModel(new Object[]{"Tên bàn", "Số ghế", "Trạng thái"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(50);
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(50, 75, 300, 400);
        add(jsp);
        
        // Panel chức năng
        JPanel chucNangPanel = new JPanel();
        chucNangPanel.setLayout(null); 
        chucNangPanel.setBounds(400, 75, 300, 400); 
        chucNangPanel.setBorder(BorderFactory.createTitledBorder("Chức năng"));
        add(chucNangPanel);
        
        JLabel tenBan = new JLabel("Tên bàn:");
        chucNangPanel.add(tenBan);
        tenBan.setBounds(25, 35, 60, 30);
        
        JTextField nhapTenBan = new JTextField("");
        chucNangPanel.add(nhapTenBan);
        nhapTenBan.setBounds(90, 40, 180, 25);
        
        JLabel soGhe = new JLabel("Số ghế:");
        chucNangPanel.add(soGhe);
        soGhe.setBounds(25, 75, 60, 30);
        
        JTextField nhapSoGhe = new JTextField("");
        chucNangPanel.add(nhapSoGhe);
        nhapSoGhe.setBounds(90, 80, 180, 25);
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) { 
                    String tb = (String) model.getValueAt(selectedRow, 0);
                    String sg = (String) model.getValueAt(selectedRow, 1);
                    nhapTenBan.setText(tb);
                    nhapSoGhe.setText(sg);
                }
            }
        });
        
        JButton them = new JButton("Thêm bàn");
        chucNangPanel.add(them);
        them.setBounds(25, 130, 110, 35);

        JButton sua = new JButton("Sửa bàn");
        chucNangPanel.add(sua);
        sua.setBounds(160, 130, 110, 35); 

        JButton xoa = new JButton("Xoá bàn");
        chucNangPanel.add(xoa);
        xoa.setBounds(25, 180, 110, 35);
        
        them.addActionListener(e -> {
            String tenBan1 = nhapTenBan.getText().trim();
            String soGhe1 = nhapSoGhe.getText().trim();
            if (tenBan1.isEmpty() || soGhe1.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên bàn hoặc số ghế không được để trống");
            } else if (!soGhe1.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Số ghế không được chứa chữ cái", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                Table a = new Table(tenBan1, "Trống", "Chưa nấu", "Chưa phục vụ", "Chưa thanh toán", null, soGhe1);
                DAOTable dao1 = new DAOTable();
                boolean check = dao1.addTable(a);
                if (!check) {
                    JOptionPane.showMessageDialog(this, "Thêm bàn thất bại! Có thể tên bàn đã tồn tại.");
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm bàn thành công");        
                }
                showListTable(dao1.getAllTable());
            }
        });
        
        sua.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn để sửa");
                return;
            }
            String tenBanMoi = nhapTenBan.getText().trim();
            String soGheMoi = nhapSoGhe.getText().trim();
            String tenBanCu = (String) model.getValueAt(selectedRow, 0);
            String soGheCu = (String) model.getValueAt(selectedRow, 1);
            String trangThaiCu = (String) model.getValueAt(selectedRow, 2);
            if (trangThaiCu.equals("Có khách")) {
                JOptionPane.showMessageDialog(this, "Bàn đang có khách, không thể sửa", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!tenBanCu.equals(tenBanMoi)) {
                JOptionPane.showMessageDialog(this, "Tên bàn không được thay đổi!");
                return;
            }
            if (tenBanMoi.isEmpty() || soGheMoi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên bàn hoặc số ghế không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (soGheCu.equals(soGheMoi)) {
                JOptionPane.showMessageDialog(this, "Số ghế không thay đổi");
                return;
            }
            DAOTable dao = new DAOTable();
            if (dao.updateTable(tenBanMoi, soGheMoi)) {
                JOptionPane.showMessageDialog(this, "Sửa bàn thành công");
                showListTable(dao.getAllTable());
            } else {
                JOptionPane.showMessageDialog(this, "Sửa bàn thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        xoa.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Bạn chưa chọn bàn để xoá");
                return;
            }
            String tenBanCanXoa = (String) model.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xoá " + tenBanCanXoa + " không?",
                "Xác nhận xoá",
                JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            DAOTable dao = new DAOTable();
            if (dao.deleteTable(tenBanCanXoa)) {
                JOptionPane.showMessageDialog(this, "Xoá thành công bàn " + tenBanCanXoa);
            } else {
                JOptionPane.showMessageDialog(this, "Xoá THẤT BẠI!");
            }
            showListTable(dao.getAllTable());
        });
        repaint();
    }

    public void showListTable(ArrayList<Table> arlTable) {
        model.setRowCount(0);
        for (Table x : arlTable) {
            model.addRow(new Object[]{
                x.getTableName(), x.getTableSeat(), x.getTableStatus()
            });
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UIDSBan().setVisible(true));
    }
}