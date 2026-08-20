package UI_Admin;

import DAO.DAOFood;
import DAO.DAOOrderDetail;
import Model.Food;
import UIAdmin.Sup.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class UIDSMon extends JFrame {
    private static final long serialVersionUID = 1L;

    private DefaultTableModel modelDanhSach;
    private DefaultTableModel modelNhieuTien;
    private DefaultTableModel modelSoLuong;
    private JTable tableDanhSach;

    public UIDSMon() {
        initComponent();
        refreshTable();
        DAOOrderDetail daoOD = new DAOOrderDetail();
        ArrayList<String[]> a = daoOD.getMostCost();
        showMonNhieuTien(a);
        ArrayList<String[]> b = daoOD.getMostQuantity();
        showMonNhieuSoLuong(b);
    }

    private void initComponent() {
        setTitle("Danh sách món");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(200, 20, 1080, 780);
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);
        setBackground(new Color(240, 248, 255));

        JLabel title = new JLabel("Danh sách món");
        title.setBounds(24, 32, 500, 40);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 32));
        add(title);

        modelDanhSach = new DefaultTableModel(new Object[]{"STT", "Tên món", "Phân loại", "Giá tiền (VND)"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableDanhSach = new JTable(modelDanhSach);
        JScrollPane scrDanhSach = new JScrollPane(tableDanhSach);
        scrDanhSach.setBounds(24, 152, 1028, 296);
        JTableHeader header = tableDanhSach.getTableHeader();
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.white);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 50));
        tableDanhSach.setRowHeight(36);
        tableDanhSach.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        header.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        add(scrDanhSach);

        TableColumn colSTT = tableDanhSach.getColumnModel().getColumn(0);
        colSTT.setPreferredWidth(60);
        colSTT.setMaxWidth(80);
        colSTT.setMinWidth(40);

        TableColumn colPhanLoai = tableDanhSach.getColumnModel().getColumn(2);
        colPhanLoai.setPreferredWidth(140);
        colPhanLoai.setMaxWidth(150);
        colPhanLoai.setMinWidth(80);

        TableColumn colGiaTien = tableDanhSach.getColumnModel().getColumn(3);
        colGiaTien.setPreferredWidth(300);
        colGiaTien.setMaxWidth(450);
        colGiaTien.setMinWidth(80);

        // Nút thêm món:
        JButton addMon = new JButton("Thêm món");
        addMon.setBounds(24, 91, 150, 35);
        addMon.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        addMon.setBackground(new Color(52, 73, 94));
        addMon.setForeground(Color.white);
        add(addMon);
        addMon.addActionListener(e -> {
            int rowCount = modelDanhSach.getRowCount();
            new UIThemMon(rowCount, UIDSMon.this).setLocationRelativeTo(UIDSMon.this);
        });

        // Nút sửa món:
        JButton changeMon = new JButton("Sửa món");
        changeMon.setBounds(185, 91, 130, 35);
        changeMon.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        changeMon.setBackground(new Color(52, 73, 94));
        changeMon.setForeground(Color.white);
        add(changeMon);
        changeMon.addActionListener(e -> {
            int row = tableDanhSach.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(UIDSMon.this, "Vui lòng chọn món để sửa!");
                return;
            }
            String ma = tableDanhSach.getValueAt(row, 0).toString();
            String tenmon = tableDanhSach.getValueAt(row, 1).toString();
            String phanloai = tableDanhSach.getValueAt(row, 2).toString();
            String giaStr = tableDanhSach.getValueAt(row, 3).toString().replace(",", "").replace(" VND", "").trim();
            int giamon = Integer.parseInt(giaStr);
            new UISuaMon(ma, tenmon, phanloai, giamon, UIDSMon.this);
        });

        // Nút xoá món:
        JButton deleteMon = new JButton("Xóa món");
        deleteMon.setBounds(325, 91, 130, 35);
        deleteMon.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        deleteMon.setBackground(new Color(52, 73, 94));
        deleteMon.setForeground(Color.white);
        add(deleteMon);

        deleteMon.addActionListener(e -> {
            int row = tableDanhSach.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(UIDSMon.this, "Vui lòng chọn món để xoá!");
                return;
            }
            String tenmon = tableDanhSach.getValueAt(row, 1).toString();
            int cf = JOptionPane.showConfirmDialog(UIDSMon.this, "Bạn có chắc chắn muốn xóa món: " + tenmon + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (cf != JOptionPane.YES_OPTION) return;

            DAOFood daomon = new DAOFood();
            boolean checkDelete = daomon.deleteMon(tenmon);
            if (!checkDelete) {
                JOptionPane.showMessageDialog(UIDSMon.this, "Xóa không thành công.");
            } else {
                JOptionPane.showMessageDialog(UIDSMon.this, "Xóa món thành công!");
                refreshTable();
            }
        });

        // Nút tìm món:
        JTextField search = new JTextField();
        search.setBounds(680, 91, 230, 35);
        add(search);
        JButton searchMon = new JButton("Tìm");
        searchMon.setBounds(920, 91, 130, 35);
        searchMon.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        searchMon.setBackground(new Color(52, 73, 94));
        searchMon.setForeground(Color.white);
        add(searchMon);
        searchMon.addActionListener(e -> {
            String tenMon = search.getText().trim();
            if (tenMon.isEmpty()) {
                refreshTable();
            } else {
                DAOFood daomon = new DAOFood();
                ArrayList<Food> arl = daomon.searchTen(tenMon);
                showDS(arl);
            }
        });

        // Bảng món nhiều tiền
        modelNhieuTien = new DefaultTableModel(new Object[]{"Tên món", "Tổng tiền (VND)"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable tableNhieuTien = new JTable(modelNhieuTien);
        JScrollPane scrNhieuTien = new JScrollPane(tableNhieuTien);
        scrNhieuTien.setBounds(24, 488, 506, 220);
        scrNhieuTien.setBorder(BorderFactory.createTitledBorder("Top món theo doanh thu"));
        JTableHeader header1 = tableNhieuTien.getTableHeader();
        header1.setBackground(new Color(52, 73, 94));
        header1.setForeground(Color.white);
        header1.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
        tableNhieuTien.setRowHeight(32);
        tableNhieuTien.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        header1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
        add(scrNhieuTien);

        // Bảng món nhiều số lượng
        modelSoLuong = new DefaultTableModel(new Object[]{"Tên món", "Số lượng bán"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable tableSoLuong = new JTable(modelSoLuong);
        JScrollPane scrSoLuong = new JScrollPane(tableSoLuong);
        scrSoLuong.setBounds(550, 488, 502, 220);
        scrSoLuong.setBorder(BorderFactory.createTitledBorder("Top món theo số lượng gọi"));
        JTableHeader header2 = tableSoLuong.getTableHeader();
        header2.setBackground(new Color(52, 73, 94));
        header2.setForeground(Color.white);
        header2.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
        tableSoLuong.setRowHeight(32);
        tableSoLuong.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        header2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
        add(scrSoLuong);

        repaint();
    }

    public void refreshTable() {
        DAOFood daof = new DAOFood();
        ArrayList<Food> arl = daof.getAllFood();
        showDS(arl);
        DAOOrderDetail daoOD = new DAOOrderDetail();
        showMonNhieuTien(daoOD.getMostCost());
        showMonNhieuSoLuong(daoOD.getMostQuantity());
    }

    public void showDS(ArrayList<Food> arl) {
        modelDanhSach.setRowCount(0);
        for (Food a : arl) {
            modelDanhSach.addRow(new Object[]{
                a.getFoodId(),
                a.getFoodName(),
                a.getFoodCategory(),
                String.format("%,d", a.getFoodCost())
            });
        }
    }

    public void showMonNhieuTien(ArrayList<String[]> al) {
        modelNhieuTien.setRowCount(0);
        for (String[] x : al) {
            try {
                long tien = Long.parseLong(x[1]);
                modelNhieuTien.addRow(new Object[]{x[0], String.format("%,d", tien)});
            } catch (NumberFormatException ex) {
                modelNhieuTien.addRow(new Object[]{x[0], x[1]});
            }
        }
    }
    
    public void showMonNhieuSoLuong(ArrayList<String[]> al) {
        modelSoLuong.setRowCount(0);
        for (String[] x : al) {
            modelSoLuong.addRow(new Object[]{x[0], x[1]});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UIDSMon().setVisible(true));
    }
}
