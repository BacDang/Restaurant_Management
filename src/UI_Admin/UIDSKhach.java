package UI_Admin;

import DAO.DAOCustomer;
import Model.Customer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UIDSKhach extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public UIDSKhach() {
        initComponent();
        loadAllData();
    }
    
    private void initComponent() {
        setTitle("Quản lý Khách hàng");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel tieude = new JLabel("DANH SÁCH KHÁCH HÀNG");
        tieude.setBounds(0, 10, 800, 30);
        tieude.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tieude.setHorizontalAlignment(JLabel.CENTER);
        add(tieude);

        // Panel Tìm kiếm
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(null);
        searchPanel.setBounds(50, 50, 700, 60);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm khách hàng"));
        add(searchPanel);

        JLabel tenKhachLabel = new JLabel("Tên khách:");
        tenKhachLabel.setBounds(20, 25, 70, 25);
        searchPanel.add(tenKhachLabel);

        JTextField tenKhachField = new JTextField();
        tenKhachField.setBounds(90, 25, 380, 25);
        searchPanel.add(tenKhachField);

        JButton timButton = new JButton("Tìm");
        timButton.setBounds(480, 25, 95, 25);
        searchPanel.add(timButton);

        JButton lamMoiButton = new JButton("Tất cả");
        lamMoiButton.setBounds(585, 25, 95, 25);
        searchPanel.add(lamMoiButton);
        
        model = new DefaultTableModel(new Object[]{"Tên khách", "Số điện thoại", "Tổng chi (VND)", "Số lượng đơn"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(50, 120, 700, 420);
        add(jsp);
        table.setRowHeight(30);
        
        timButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tenTimKiem = tenKhachField.getText().trim();
                DAOCustomer dao = new DAOCustomer();
                if (tenTimKiem.isEmpty()) {
                    loadAllData();
                    return;
                }
                ArrayList<Customer> dsKhach = dao.findCusByName(tenTimKiem);
                if (dsKhach.isEmpty()) {
                    JOptionPane.showMessageDialog(table, "Không tìm thấy khách hàng: " + tenTimKiem, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    showListCus(new ArrayList<>());
                } else {
                    showListCus(dsKhach);
                }
            }
        });

        lamMoiButton.addActionListener(e -> {
            tenKhachField.setText("");
            loadAllData();
        });

        repaint();
    }
    
    public void loadAllData() {
        DAOCustomer daokhach = new DAOCustomer();
        ArrayList<Customer> arl = daokhach.getAllCus();
        showListCus(arl);
    }

    public void showListCus(ArrayList<Customer> dsKhach) {
        model.setRowCount(0);
        for (Customer a : dsKhach) {
            model.addRow(new Object[]{
                a.getTenKhach(),
                a.getSoDienThoai(),
                String.format("%,d", a.getTongChi()),
                String.valueOf(a.getSoLuongDon())
            });
        }
    }
    
    public static void main(String[] args) {
        new UIDSKhach();
    }
}
