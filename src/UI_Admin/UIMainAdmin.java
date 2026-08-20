package UI_Admin;

import DAO.DAOOrder;
import DAO.DAOOrderDetail;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import Login.LoginForm;
import java.util.ArrayList;

public class UIMainAdmin extends JFrame {
    private static final long serialVersionUID = 1L;

    // Panel Doanh Thu
    private JTextField txtNgayXem;
    private JLabel lblSoDon;
    private JLabel lblTongDoanhThu;
    private JButton btnXemDoanhThu;

    // Panel Chức Năng
    private JButton btnDanhSachBan;
    private JButton btnDanhSachMon;
    private JButton btnDanhSachKhach;
    private JButton btnDanhSachNhanVien;

    public UIMainAdmin() {
        initComponent();
    }

    private void initComponent() {
        setTitle("Trang Quản Trị");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(220, 220, 220));
        setResizable(false);

        Font titleFont = new Font("Inter", Font.BOLD, 18);
        Font labelFont = new Font("Inter", Font.PLAIN, 14);
        Font buttonFont = new Font("Inter", Font.BOLD, 16);
        Color panelBgColor = new Color(240, 240, 240);
        Color buttonBgColor = new Color(255, 255, 255);

        // Tạo Panel DOANH THU (ở trên)
        JPanel pnlDoanhThu = new JPanel();
        pnlDoanhThu.setLayout(null);
        pnlDoanhThu.setBackground(panelBgColor);
        pnlDoanhThu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlDoanhThu.setPreferredSize(new Dimension(450, 200));

        JLabel lblTitleDoanhThu = new JLabel("DOANH THU");
        lblTitleDoanhThu.setFont(titleFont);
        lblTitleDoanhThu.setPreferredSize(new Dimension(400, 30));
        lblTitleDoanhThu.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitleDoanhThu.setBounds(170, 20, 150, 30);

        txtNgayXem = new JTextField("yyyy-MM-dd");
        txtNgayXem.setPreferredSize(new Dimension(200, 30));
        txtNgayXem.setBounds(20, 60, 150, 30);

        lblSoDon = new JLabel("Số đơn: ...");
        lblSoDon.setPreferredSize(new Dimension(100, 30));
        lblSoDon.setBounds(20, 150, 120, 30);

        lblTongDoanhThu = new JLabel("Tổng doanh thu: ...");
        lblTongDoanhThu.setPreferredSize(new Dimension(150, 30));
        lblTongDoanhThu.setBounds(20, 110, 250, 30);

        btnXemDoanhThu = new JButton("Xem doanh thu");
        btnXemDoanhThu.setFont(labelFont);
        btnXemDoanhThu.setBounds(190, 60, 150, 30);

        btnXemDoanhThu.addActionListener(evt -> {
            String ngayXem = txtNgayXem.getText().trim();
            if (!ngayXem.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày đúng định dạng yyyy-MM-dd (Ví dụ: 2026-08-20)");
                return;
            }
            ArrayList<String> arlOrderId = new DAOOrder().getOrderIdByDate(ngayXem);
            int count = arlOrderId.size();
            lblSoDon.setText("Số đơn: " + count);

            long money = 0;
            DAOOrderDetail daoOD = new DAOOrderDetail();
            for (String orderId : arlOrderId) {
                long orderCost = daoOD.getOrderCost(orderId);
                money += orderCost;
            }
            lblTongDoanhThu.setText("Tổng doanh thu: " + String.format("%,d VND", money));
        });

        pnlDoanhThu.add(lblTitleDoanhThu);
        pnlDoanhThu.add(txtNgayXem);
        pnlDoanhThu.add(btnXemDoanhThu);
        pnlDoanhThu.add(lblSoDon);
        pnlDoanhThu.add(lblTongDoanhThu);

        // Tạo Panel CHỨC NĂNG KHÁC (ở giữa) 
        JPanel pnlChucNang = new JPanel(new BorderLayout(10, 10));
        pnlChucNang.setBackground(panelBgColor);
        pnlChucNang.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel lblTitleChucNang = new JLabel("CHỨC NĂNG QUẢN LÝ");
        lblTitleChucNang.setFont(titleFont);
        lblTitleChucNang.setHorizontalAlignment(SwingConstants.CENTER);
        pnlChucNang.add(lblTitleChucNang, BorderLayout.NORTH);

        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 15, 15));
        pnlButtons.setBackground(panelBgColor);

        btnDanhSachBan = new JButton("Danh sách bàn");
        btnDanhSachMon = new JButton("Danh sách món");
        btnDanhSachKhach = new JButton("Danh sách khách hàng");
        btnDanhSachNhanVien = new JButton("Danh sách nhân viên");

        JButton[] buttons = {btnDanhSachBan, btnDanhSachMon, btnDanhSachKhach, btnDanhSachNhanVien};
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBackground(buttonBgColor);
            btn.setPreferredSize(new Dimension(150, 80));
            btn.setFocusPainted(false);
        }

        pnlButtons.add(btnDanhSachBan);
        pnlButtons.add(btnDanhSachMon);
        pnlButtons.add(btnDanhSachKhach);
        pnlButtons.add(btnDanhSachNhanVien);
        pnlChucNang.add(pnlButtons, BorderLayout.CENTER);
        add(pnlDoanhThu, BorderLayout.NORTH);
        add(pnlChucNang, BorderLayout.CENTER);

        btnDanhSachMon.addActionListener(e -> new UIDSMon());
        btnDanhSachBan.addActionListener(evt -> new UIDSBan());
        btnDanhSachKhach.addActionListener(evt -> new UIDSKhach());
        btnDanhSachNhanVien.addActionListener(evt -> new UIDSNhanVien());

        JButton dangxuat = new JButton("Đăng xuất");
        dangxuat.setBounds(10, 10, 50, 30);
        dangxuat.addActionListener(l -> {
            new LoginForm().setVisible(true);
            dispose();
        });
        pnlChucNang.add(dangxuat, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UIMainAdmin().setVisible(true));
    }
}
