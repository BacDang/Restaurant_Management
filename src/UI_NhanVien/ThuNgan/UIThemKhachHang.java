package UI_NhanVien.ThuNgan;

import DAO.DAOCustomer;
import Model.Customer;
import javax.swing.*;
import java.awt.*;

public class UIThemKhachHang extends JFrame {
    private static final long serialVersionUID = 1L;

    private final JTextField txtTen;
    private final JTextField txtSDT;
    private final int tongTien;

    public UIThemKhachHang(int tongTien) {
        this.tongTien = tongTien;
        setTitle("Thông tin khách hàng");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("NHẬP THÔNG TIN KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(52, 73, 94));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel lblTen = new JLabel("Tên khách hàng:");
        JLabel lblSDT = new JLabel("Số điện thoại:");
        txtTen = new JTextField();
        txtSDT = new JTextField();

        form.add(lblTen);
        form.add(txtTen);
        form.add(lblSDT);
        form.add(txtSDT);
        add(form, BorderLayout.CENTER);

        JButton btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setBackground(new Color(46, 204, 113));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXacNhan.addActionListener(e -> xuLyThanhToan());

        JPanel bottom = new JPanel();
        bottom.add(btnXacNhan);
        add(bottom, BorderLayout.SOUTH);
    }

    private void xuLyThanhToan() {
        String ten = txtTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        if (ten.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!sdt.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại chỉ được nhập số!");
            return;
        }

        DAOCustomer daoC = new DAOCustomer();
        if (daoC.isExist(sdt)) {
            int cf = JOptionPane.showConfirmDialog(this, "Lưu vào thông tin khách hàng cũ?",
                    "Lưu thông tin khách hàng", JOptionPane.YES_NO_OPTION);
            if (cf != JOptionPane.YES_OPTION) {
                dispose();
                return;
            }
            daoC.updateCus(sdt, tongTien);
            dispose();
            return;
        }
        Customer aa = new Customer(ten, sdt, tongTien, 1);
        daoC.addCus(aa);
        JOptionPane.showMessageDialog(this, "Thêm Khách hàng mới thành công!");
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UIThemKhachHang(34000).setVisible(true));
    }
}
