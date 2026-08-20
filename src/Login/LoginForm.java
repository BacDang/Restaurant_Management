package Login;

import UI_Admin.UIMainAdmin;
import DAO.DAOUser;
import Model.User;
import UI_NhanVien.PhucVuBep.UI_Bep;
import UI_NhanVien.PhucVuBep.UI_PhucVu;
import UI_NhanVien.ThuNgan.UI_ThuNgan;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class LoginForm extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public LoginForm() {
        initComponent();
    }

    private void initComponent() {
        setTitle("Đăng nhập hệ thống");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        txtUser = new JTextField(15);
        txtPass = new JPasswordField(15);
        btnLogin = new JButton("Đăng nhập");
        btnLogin.addActionListener(l -> doLogin());

        JPanel panel = new JPanel();
        panel.add(new JLabel("Username:"));
        panel.add(txtUser);
        panel.add(new JLabel("Password:"));
        panel.add(txtPass);
        panel.add(btnLogin);

        add(panel);

        txtUser.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doLogin();
                }
            }
        });
        txtPass.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doLogin();
                }
            }
        });
    }

    private void doLogin() {
        String username = txtUser.getText().trim();
        String password = new String(txtPass.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.",
                    "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            DAOUser daouser = new DAOUser();
            User u = daouser.login(username, password);
            if (u == null) {
                JOptionPane.showMessageDialog(this,
                        "Tên đăng nhập hoặc mật khẩu không chính xác.",
                        "Lỗi đăng nhập",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this,
                    "Đăng nhập thành công!\nVai trò: " + u.getRole());
            this.dispose();

            switch (u.getRole()) {
                case "Admin":
                    new UIMainAdmin().setVisible(true);
                    break;
                case "Thu ngân":
                    new UI_ThuNgan().setVisible(true);
                    break;
                case "Phục vụ":
                    new UI_PhucVu().setVisible(true);
                    break;
                case "Bếp":
                    new UI_Bep().setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Vai trò không hợp lệ: " + u.getRole());
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
