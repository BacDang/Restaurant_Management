package UIAdmin.Sup;

import DAO.DAOFood;
import Model.Food;
import UI_Admin.UIDSMon;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

public class UISuaMon extends JFrame {
    
    private final UIDSMon parent;
    
    public UISuaMon(String ma, String tenmon, String phanloai, int giamon, UIDSMon parent) {
        this.parent = parent;
        
        setTitle("Sửa món");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0, 0, 600, 360);
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(parent);
        
        getContentPane().setBackground(new Color(240, 248, 255));
        
        JLabel title = new JLabel("Sửa món", SwingConstants.CENTER);
        title.setBounds(150, 20, 300, 40);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
        add(title);
        
        JLabel id = new JLabel("ID");
        id.setBounds(24, 83, 40, 24);
        add(id);
        
        JTextField idText = new JTextField(String.valueOf(ma));
        idText.setBounds(24, 107, 75, 40);
        idText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(idText);
        idText.setEditable(false);
        
        JLabel tenMon = new JLabel("Tên món");
        tenMon.setBounds(119, 83, 84, 24);
        add(tenMon);
        
        JTextField tenText = new JTextField(tenmon);
        tenText.setBounds(119, 107, 457, 40);
        add(tenText);
        
        JLabel phanLoai = new JLabel("Phân loại");
        phanLoai.setBounds(24, 154, 90, 24);
        add(phanLoai);
        
        JTextField loaiText = new JTextField(phanloai);
        loaiText.setBounds(24, 178, 266, 40);
        add(loaiText);
        
        JLabel giaTien = new JLabel("Giá tiền (VND)");
        giaTien.setBounds(310, 154, 120, 24);
        add(giaTien);
        
        JTextField giaText = new JTextField(String.valueOf(giamon));
        giaText.setBounds(310, 178, 266, 40);
        add(giaText);
        
        JButton huyButton = new JButton("Hủy");
        huyButton.setBounds(310, 240, 75, 40);
        add(huyButton);
        huyButton.addActionListener(e -> dispose());
        
        JButton saveButton = new JButton("Lưu thông tin");
        saveButton.setBounds(405, 240, 171, 40);
        saveButton.setBackground(new Color(52, 152, 219));
        saveButton.setForeground(Color.WHITE);
        add(saveButton);
        
        saveButton.addActionListener(e -> {
            String ten = tenText.getText().trim();
            String loai = loaiText.getText().trim();
            String giaStr = giaText.getText().trim();
            if (ten.isEmpty() || loai.isEmpty() || giaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không được để trống. Vui lòng bổ sung đầy đủ thông tin.");
                return;
            }
            int gia;
            try {
                gia = Integer.parseInt(giaStr);
                if (gia <= 0) {
                    JOptionPane.showMessageDialog(this, "Giá tiền phải là số nguyên dương.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá tiền phải là số nguyên hợp lệ.");
                return;
            }
            
            Food a = new Food(idText.getText().trim(), ten, loai, gia); 
            DAOFood daomon = new DAOFood();
            boolean check = daomon.changeMon(a);
            if (!check) {
                JOptionPane.showMessageDialog(this, "Sửa món không thành công.");
            } else {
                JOptionPane.showMessageDialog(this, "Sửa món thành công!");
                if (parent != null) {
                    parent.refreshTable();
                }
                dispose();
            }
        });
        repaint();
    }
}
