package UI_NhanVien.PhucVuBep;

import DAO.DAOOrderDetail;
import Model.OrderFood;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class UIXemMonDaGoi extends JFrame {

    private final String maDon;
    private final String tenBan;
    private JTable tableDSMon;
    private DefaultTableModel modelDSMon;
    private ArrayList<OrderFood> listMonDaGoi;

    public UIXemMonDaGoi(String maDon, String tenBan) {
        this.maDon = maDon;
        this.tenBan = tenBan;
        initComponent();
        listMonDaGoi = new ArrayList<>();
        loadListMonDaGoi();
    }

    private void initComponent() {
        setBounds(400, 50, 600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel tieuDe = new JLabel("XEM MÓN ĐÃ GỌI BÀN " + tenBan);
        tieuDe.setBounds(0, 0, 600, 50);
        tieuDe.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        tieuDe.setForeground(Color.WHITE);
        tieuDe.setOpaque(true);
        tieuDe.setBackground(new Color(52, 73, 94));
        tieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        add(tieuDe);

        JLabel md = new JLabel("MÃ ĐƠN: " + this.maDon);
        md.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));
        md.setForeground(Color.black);
        md.setBounds(50, 75, 300, 20);
        add(md);

        modelDSMon = new DefaultTableModel(new Object[]{"Tên món", "Số lượng", "Giảm"}, 0);
        tableDSMon = new JTable(modelDSMon);
        tableDSMon.setDefaultEditor(Object.class, null);
        JTableHeader header1 = tableDSMon.getTableHeader();
        header1.setBackground(new Color(52, 73, 94));
        header1.setForeground(Color.white);
        header1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        tableDSMon.getColumnModel().getColumn(2).setMaxWidth(80);
        for (int i = 0; i < 2; i++) {
            tableDSMon.getColumnModel().getColumn(i).setCellRenderer(new CenterTextCellRenderer());
        }
        tableDSMon.setRowHeight(40);
        tableDSMon.getColumnModel().getColumn(2).setCellRenderer(new ButtonCellRenderer());
        tableDSMon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableDSMon.rowAtPoint(e.getPoint());
                int col = tableDSMon.columnAtPoint(e.getPoint());
                if (col == 2 && row >= 0) {
                    String tenMon = modelDSMon.getValueAt(row, 0).toString();
                    int quantity = Integer.parseInt(modelDSMon.getValueAt(row, 1).toString());
                    DAOOrderDetail daoOD = new DAOOrderDetail();
                    if (quantity - 1 <= 0) {
                        daoOD.deleteOrderDetail(maDon, tenMon);
                    } else {
                        daoOD.decreaseQuantity(maDon, tenMon);
                    }
                    loadListMonDaGoi();
                }
            }
        });

        JScrollPane jsp = new JScrollPane(tableDSMon);
        jsp.setBounds(50, 120, 480, 250);
        add(jsp);

        JButton btn = new JButton("Đóng");
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        btn.setForeground(Color.white);
        btn.setBounds(430, 400, 100, 35);
        btn.setBackground(new Color(52, 152, 219));
        btn.addActionListener(e -> dispose());
        add(btn);

        repaint();
    }

    public void loadListMonDaGoi() {
        listMonDaGoi = new DAOOrderDetail().getOrderDetail(maDon);
        showDS(listMonDaGoi);
    }

    public void showDS(ArrayList<OrderFood> ds) {
        modelDSMon.setRowCount(0);
        for (OrderFood a : ds) {
            modelDSMon.addRow(new Object[]{a.getTenMon(), a.getSoLuongMon(), "-"});
        }
    }

    public static void main(String[] args) {
        new UIXemMonDaGoi("ORD09033", "B001");
    }
}
