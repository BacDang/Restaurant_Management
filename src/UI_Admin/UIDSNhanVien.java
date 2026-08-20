package UI_Admin;

import DAO.DAOEmployee;
import DAO.DAOUser;
import Model.Employee;
import Model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UIDSNhanVien extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtID;
    private JTextField txtName;
    private JTextField txtBirth;
    private JTextField txtRole;
    private JTextField txtSex;
    private JTextField txtTel;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;

    public UIDSNhanVien() {
        initComponent();
        DAOEmployee daoEmployee = new DAOEmployee();
        ArrayList<Employee> arl = daoEmployee.getAllEmployee();
        showNV(arl);
    }
    
    private void initComponent() {
        setTitle("Danh Sách Nhân Viên");
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setBounds(400, 80, 800, 600);
        setLocationRelativeTo(null);
        
        model = new DefaultTableModel(new Object[]{"Mã NV", "Tên NV", "Ngày Sinh", "Chức vụ", "Giới tính", "SĐT"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(20, 20, 750, 250);
        add(jsp);

        JLabel l1 = new JLabel("Mã NV:"); 
        l1.setBounds(20, 300, 80, 30); 
        add(l1);
        txtID = new JTextField(); 
        txtID.setBounds(120, 300, 150, 30); 
        add(txtID);

        JLabel l2 = new JLabel("Tên NV:"); 
        l2.setBounds(20, 340, 80, 30); 
        add(l2);
        txtName = new JTextField(); 
        txtName.setBounds(120, 340, 150, 30); 
        add(txtName);

        JLabel l3 = new JLabel("Ngày sinh:"); 
        l3.setBounds(20, 380, 100, 30); 
        add(l3);
        txtBirth = new JTextField(); 
        txtBirth.setBounds(120, 380, 150, 30); 
        add(txtBirth);

        JLabel l4 = new JLabel("Chức vụ:"); 
        l4.setBounds(400, 300, 80, 30); 
        add(l4);
        txtRole = new JTextField(); 
        txtRole.setBounds(500, 300, 150, 30); 
        add(txtRole);

        JLabel l5 = new JLabel("Giới tính:"); 
        l5.setBounds(400, 340, 80, 30); 
        add(l5);
        txtSex = new JTextField(); 
        txtSex.setBounds(500, 340, 150, 30); 
        add(txtSex);

        JLabel l6 = new JLabel("SĐT:"); 
        l6.setBounds(400, 380, 80, 30); 
        add(l6);
        txtTel = new JTextField(); 
        txtTel.setBounds(500, 380, 150, 30); 
        add(txtTel);

        btnAdd = new JButton("Thêm"); 
        btnAdd.setBounds(200, 450, 100, 40); 
        add(btnAdd);

        btnUpdate = new JButton("Sửa"); 
        btnUpdate.setBounds(330, 450, 100, 40); 
        add(btnUpdate);

        btnDelete = new JButton("Xóa"); 
        btnDelete.setBounds(460, 450, 100, 40); 
        add(btnDelete);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtID.setText(model.getValueAt(row, 0).toString());
                    txtName.setText(model.getValueAt(row, 1).toString());
                    txtBirth.setText(model.getValueAt(row, 2).toString());
                    txtRole.setText(model.getValueAt(row, 3).toString());
                    txtSex.setText(model.getValueAt(row, 4).toString());
                    txtTel.setText(model.getValueAt(row, 5).toString());
                }
            }
        });

        btnAdd.addActionListener(e -> {
            String id = txtID.getText().trim();
            String name = txtName.getText().trim();
            String birth = txtBirth.getText().trim();
            String role = txtRole.getText().trim();
            String sex = txtSex.getText().trim();
            String tel = txtTel.getText().trim();
            if (id.isEmpty() || name.isEmpty() || birth.isEmpty() || role.isEmpty() 
                    || sex.isEmpty() || tel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
                return;
            }
            if (name.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(this, "Tên không được nhập số!");
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                sdf.parse(birth);
            } catch (ParseException ee) {
                JOptionPane.showMessageDialog(this, "Ngày không hợp lệ! Vui lòng nhập định dạng yyyy-MM-dd");
                return;
            }
            if (!tel.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Số điện thoại chỉ được nhập số!");
                return;
            }
            DAOEmployee daoEmployee = new DAOEmployee();
            if (daoEmployee.isExistId(id)) {
                JOptionPane.showMessageDialog(this, "Đã tồn tại ID: " + id);
                return;
            }
            Employee a = new Employee(id, name, birth, role, sex, tel);
            if (daoEmployee.addEmployee(a)) {
                JOptionPane.showMessageDialog(this, "Thêm NV thành công.");
                User aa = new User(id, id, role);
                new DAOUser().addUser(aa);
                showNV(daoEmployee.getAllEmployee());
            }
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn Nhân viên để sửa!");
                return;
            }
            String ma = model.getValueAt(row, 0).toString();
            String maTF = txtID.getText().trim();
            String tenTF = txtName.getText().trim();
            String ngaySinhTF = txtBirth.getText().trim();
            String chucVuTF = txtRole.getText().trim();
            String gioiTinhTF = txtSex.getText().trim();
            String sdtTF = txtTel.getText().trim();

            if (!ma.equals(maTF)) {
                JOptionPane.showMessageDialog(this, "Không được thay đổi mã nhân viên!");
                return;
            }
            if (maTF.isEmpty() || tenTF.isEmpty() || ngaySinhTF.isEmpty() 
                    || chucVuTF.isEmpty() || gioiTinhTF.isEmpty() || sdtTF.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                sdf.parse(ngaySinhTF);
            } catch (ParseException ee) {
                JOptionPane.showMessageDialog(this, "Ngày không hợp lệ! Vui lòng nhập định dạng yyyy-MM-dd");
                return;
            }
            if (!sdtTF.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Số điện thoại chỉ được nhập số!");
                return;
            }
            Employee s = new Employee(maTF, tenTF, ngaySinhTF, chucVuTF, gioiTinhTF, sdtTF);
            DAOEmployee daoe = new DAOEmployee();
            if (daoe.updateEmployee(s)) {
                JOptionPane.showMessageDialog(this, "Sửa thành công Nhân viên mã: " + ma);
                showNV(daoe.getAllEmployee());
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn Nhân viên để xóa!");
                return;
            }
            String id = model.getValueAt(row, 0).toString();
            int cf = JOptionPane.showConfirmDialog(this, "Xác nhận xóa nhân viên " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (cf != JOptionPane.YES_OPTION) return;

            new DAOEmployee().deleteEmployee(id);
            new DAOUser().deleteUser(id);
            showNV(new DAOEmployee().getAllEmployee());
        });
        repaint();
    }

    public void showNV(ArrayList<Employee> arl) {
        model.setRowCount(0);
        for (Employee a : arl) {
            model.addRow(new Object[]{
                a.getId(), a.getName(), a.getBirth(),
                a.getRole(), a.getSex(), a.getPhone()
            });
        }
    }
    
    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> new UIDSNhanVien().setVisible(true)); 
    }
}
