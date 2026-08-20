package UI_NhanVien.PhucVuBep;

import Model.OrderDetail;
import Model.OrderFood;
import Model.Food;
import DAO.DAOFood;
import DAO.DAOOrder;
import DAO.DAOOrderDetail;
import DAO.DAOTable;
import Model.Order;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class UIThemMonVaoDon extends JFrame {
    private final String maDon;
    private final String tenBan;
    private JTable tableDSMon;
    private DefaultTableModel modelDSMon;
    private JTable tableDSMonCanThem;
    private DefaultTableModel modelDSMonCanThem;
    private final ArrayList<OrderFood> listMonCanThem;
    private UI_PhucVu t;
    private Runnable onCompleteCallback;

    public UIThemMonVaoDon(String maDon, String tenBan, UI_PhucVu t) {
        this.maDon = maDon;
        this.tenBan = tenBan;
        this.t = t;
        this.listMonCanThem = new ArrayList<>();
        initComponent();
        loadListFood();
    }

    public UIThemMonVaoDon(String maDon, String tenBan, Runnable onCompleteCallback) {
        this.maDon = maDon;
        this.tenBan = tenBan;
        this.onCompleteCallback = onCompleteCallback;
        this.listMonCanThem = new ArrayList<>();
        initComponent();
        loadListFood();
    }
    
    private void initComponent() {
        setBounds(400, 20, 600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);
        
        JLabel tieuDe = new JLabel("THÊM MÓN BÀN " + tenBan);
        tieuDe.setBounds(0, 0, 600, 50);
        tieuDe.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        tieuDe.setForeground(Color.WHITE);
        tieuDe.setOpaque(true);
        tieuDe.setBackground(new Color(52, 73, 94));
        tieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        add(tieuDe);
        
        JLabel titleDSM = new JLabel("DANH SÁCH MÓN");
        titleDSM.setBounds(50, 60, 480, 35);
        titleDSM.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
        titleDSM.setHorizontalAlignment(SwingConstants.CENTER);
        titleDSM.setForeground(Color.black);
        add(titleDSM);
        
        modelDSMon = new DefaultTableModel(new Object[]{"Tên món", "Loại món", "Giá tiền", "Thêm"}, 0);
        tableDSMon = new JTable(modelDSMon);
        tableDSMon.setDefaultEditor(Object.class, null);
        JTableHeader header1 = tableDSMon.getTableHeader();
        header1.setBackground(new Color(52, 73, 94));
        header1.setForeground(Color.white);
        header1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        tableDSMon.getColumnModel().getColumn(3).setMaxWidth(80);
        for (int i = 0; i < 3; i++) {
            tableDSMon.getColumnModel().getColumn(i).setCellRenderer(new CenterTextCellRenderer());
        }
        tableDSMon.setRowHeight(40);
        tableDSMon.getColumnModel().getColumn(3).setCellRenderer(new ButtonCellRenderer());
        tableDSMon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableDSMon.rowAtPoint(e.getPoint());
                int col = tableDSMon.getSelectedColumn();
                if (col == 3 && row >= 0) {
                    String tenMon = (String) modelDSMon.getValueAt(row, 0);
                    boolean flag = true;
                    for (OrderFood x : listMonCanThem) {
                        if (x.getTenMon().equals(tenMon)) {
                            x.setSoLuongMon(x.getSoLuongMon() + 1);
                            flag = false;
                        }
                    }
                    if (flag) {
                        OrderFood a = new OrderFood(tenMon, 1);
                        listMonCanThem.add(a);
                    }
                    loadListMonCanThem();
                }
            }
        });
        
        JScrollPane jsp = new JScrollPane(tableDSMon);
        jsp.setBounds(50, 95, 480, 250);
        add(jsp);
        
        JLabel title2 = new JLabel("DANH SÁCH MÓN CẦN THÊM");
        title2.setBounds(50, 350, 480, 35);
        title2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
        title2.setHorizontalAlignment(SwingConstants.CENTER);
        title2.setForeground(Color.black);
        add(title2);
        
        modelDSMonCanThem = new DefaultTableModel(new Object[]{"Tên món", "Số lượng", "Giảm"}, 0);
        tableDSMonCanThem = new JTable(modelDSMonCanThem);
        tableDSMonCanThem.setDefaultEditor(Object.class, null);
        JTableHeader header2 = tableDSMonCanThem.getTableHeader();
        header2.setBackground(new Color(52, 73, 94));
        header2.setForeground(Color.white);
        header2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        tableDSMonCanThem.getColumnModel().getColumn(2).setMaxWidth(80);
        for (int i = 0; i < 2; i++) {
            tableDSMonCanThem.getColumnModel().getColumn(i).setCellRenderer(new CenterTextCellRenderer());
        }
        tableDSMonCanThem.setRowHeight(40);
        tableDSMonCanThem.getColumnModel().getColumn(2).setCellRenderer(new ButtonCellRenderer());
        tableDSMonCanThem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableDSMonCanThem.rowAtPoint(e.getPoint());
                int col = tableDSMonCanThem.columnAtPoint(e.getPoint());
                if (col == 2 && row >= 0) {
                    String tenMon = (String) modelDSMonCanThem.getValueAt(row, 0);
                    for (int i = 0; i < listMonCanThem.size(); i++) {
                        String ten2 = listMonCanThem.get(i).getTenMon();
                        int sl2 = listMonCanThem.get(i).getSoLuongMon();
                        if (ten2.equals(tenMon)) {
                            listMonCanThem.get(i).setSoLuongMon(sl2 - 1);
                            if (sl2 - 1 <= 0) {
                                listMonCanThem.remove(i);
                            }
                            break;
                        }
                    }
                    loadListMonCanThem();
                }
            }
        });
        JScrollPane jsp2 = new JScrollPane(tableDSMonCanThem);
        jsp2.setBounds(100, 385, 380, 200);
        add(jsp2);
        
        JButton btn = new JButton("Xác nhận");
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        btn.setForeground(Color.white);
        btn.setBounds(380, 600, 100, 35);
        btn.setBackground(new Color(52, 152, 219));
        add(btn);

        btn.addActionListener(e -> {
            if (listMonCanThem.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chưa chọn món nào!");
                return;
            }
            DAOOrder daoOrder = new DAOOrder();
            if (!daoOrder.isExist(maDon)) {
                String ngayTao = LocalDate.now().toString();
                Order o = new Order(maDon, ngayTao, tenBan, "Chưa thanh toán");
                daoOrder.addOrder(o);
            }
            for (OrderFood a : listMonCanThem) {
                String name = a.getTenMon();
                int quantity = a.getSoLuongMon();
                OrderDetail od = new OrderDetail(maDon, name, quantity);
                DAOOrderDetail daoOD = new DAOOrderDetail();
                daoOD.addOrderDetail(od);
            }
            JOptionPane.showMessageDialog(this, "Thêm món vào đơn thành công!");
            listMonCanThem.clear();
            loadListMonCanThem();
            DAOTable daoT = new DAOTable();
            daoT.updateServe(tenBan, "Đã gọi món");
            
            if (t != null) {
                t.loadData();
            }
            if (onCompleteCallback != null) {
                onCompleteCallback.run();
            }
            dispose();
        });
        repaint();
    }
    
    public void loadListFood() {
        ArrayList<Food> al = new DAOFood().getAllFood();
        showListFood(al);
    }
    
    public void showListFood(ArrayList<Food> list) {
        modelDSMon.setRowCount(0);
        for (Food a : list) {
            modelDSMon.addRow(new Object[]{
                a.getFoodName(),
                a.getFoodCategory(),
                String.format("%,d VND", a.getFoodCost()),
                "+"
            });
        }
    }
    
    public void loadListMonCanThem() {
        modelDSMonCanThem.setRowCount(0);
        for (OrderFood a : listMonCanThem) {
            modelDSMonCanThem.addRow(new Object[]{a.getTenMon(), a.getSoLuongMon(), "-"});
        }
    }
    
    public static void main(String[] args) {
        new UIThemMonVaoDon("ORD09033", "B001", (UI_PhucVu) null);
    }
}

class CenterTextCellRenderer extends DefaultTableCellRenderer {
    public CenterTextCellRenderer() {
        setFont(new Font("Segoe UI Bold", Font.BOLD, 16));
        setHorizontalAlignment(CENTER);
    }
}

class ButtonCellRenderer extends DefaultTableCellRenderer {
    public ButtonCellRenderer() {
        setFont(new Font("Segoe UI Bold", Font.BOLD, 16));
        setHorizontalAlignment(CENTER);
        setBackground(new Color(52, 152, 219));
        setForeground(Color.white);
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            setBackground(new Color(41, 128, 185));
            setForeground(Color.white);
        } else {
            setBackground(new Color(52, 152, 219));
            setForeground(Color.white);
        }
        return this;
    }
}
