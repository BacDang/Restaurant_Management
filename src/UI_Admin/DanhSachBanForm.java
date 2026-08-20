package UI_Admin;

import DAO.DAOTable;
import Model.Table;
import List_Table.Components.*;
import UI_NhanVien.PhucVuBep.UIThemMonVaoDon;
import UI_NhanVien.PhucVuBep.UIXemMonDaGoi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DanhSachBanForm extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> fTrangThai;
    private JComboBox<String> fNau;
    private JComboBox<String> fPhucVu;
    private JComboBox<String> fThanhToan;

    public DanhSachBanForm() {
        setTitle("Quản lý danh sách bàn");
        setSize(950, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 247, 250));

        add(createTitle(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        loadData();
    }

    private JLabel createTitle() {
        JLabel title = new JLabel("DANH SÁCH BÀN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(52, 73, 94));
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        return title;
    }

    private JPanel createMainPanel() {
        model = new DefaultTableModel(
                new String[]{"Số bàn", "Trạng thái", "Nấu", "Phục vụ", "Thanh toán", "Chi tiết", "Thêm món"}, 0
        ) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int r, int c) {
                return c >= 1 && c <= 6;
            }
        };

        table = new JTable(model);
        styleTable(table);

        // Các cột combo
        setUpComboBoxColumn(table, 1, "Trống", "Có khách");
        setUpComboBoxColumn(table, 2, "Chưa nấu", "Đã nấu");
        setUpComboBoxColumn(table, 3, "Đã gọi món", "Đã phục vụ");
        setUpComboBoxColumn(table, 4, "Chưa thanh toán", "Đã thanh toán");

        // Các cột nút
        addButtonToTable(5, "Chi tiết");
        addButtonToTable(6, "Thêm món");

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Lưu giá trị cũ của trạng thái khi click chuột
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                if (row >= 0 && col >= 0) {
                    Object val = model.getValueAt(row, col);
                    String oldValue = val != null ? val.toString() : "";
                    table.putClientProperty("oldValue", oldValue);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                if (row < 0 || col < 0) return;

                if (col == 5) {
                    String tenBan = model.getValueAt(row, 0).toString();
                    String trangThaiBan = model.getValueAt(row, 1).toString();
                    String phucVu = model.getValueAt(row, 3).toString();
                    if (trangThaiBan.equals("Trống")) {
                        JOptionPane.showMessageDialog(table, "Bàn chưa có khách");
                    } else if (phucVu.equals("Chưa phục vụ")) {
                        JOptionPane.showMessageDialog(table, "Bàn chưa gọi món");
                    } else {
                        DAOTable dao = new DAOTable();
                        String maDon = dao.getOrderId(tenBan);
                        UIXemMonDaGoi a = new UIXemMonDaGoi(maDon, tenBan);
                        a.setVisible(true);
                    }
                }

                if (col == 6) {
                    String tenBan = model.getValueAt(row, 0).toString();
                    String trangThaiBan = model.getValueAt(row, 1).toString();
                    if (trangThaiBan.equals("Trống")) {
                        JOptionPane.showMessageDialog(table, "Bàn chưa có khách");
                    } else {
                        DAOTable dao = new DAOTable();
                        String maDon = dao.getOrderId(tenBan);
                        UIThemMonVaoDon a = new UIThemMonVaoDon(maDon, tenBan, () -> loadData());
                        a.setVisible(true);
                    }
                }
            }
        });

        model.addTableModelListener((TableModelEvent e) -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (row < 0 || col < 0 || row >= model.getRowCount()) return;

            if (col == 1) {
                String tenBan = model.getValueAt(row, 0).toString();
                String newValue = model.getValueAt(row, 1).toString();
                Object oldObj = table.getClientProperty("oldValue");
                String oldValue = oldObj != null ? oldObj.toString() : "";
                if (newValue.equals(oldValue)) {
                    return;
                }
                DAOTable daoban = new DAOTable();
                String thanhToan = model.getValueAt(row, 4).toString();
                if (thanhToan.equals("Chưa thanh toán") && newValue.equals("Trống")) {
                    int ccf = JOptionPane.showConfirmDialog(table, "Bàn chưa thanh toán, tiếp tục?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (ccf != JOptionPane.YES_OPTION) {
                        loadData();
                        return;
                    }
                }
                if (newValue.equals("Có khách")) {
                    int ccf = JOptionPane.showConfirmDialog(table, "Tạo đơn mới?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (ccf != JOptionPane.YES_OPTION) {
                        loadData();
                        return;
                    }
                    daoban.resetTable(tenBan);
                    daoban.addNewOrderId(tenBan);
                } else if (newValue.equals("Trống")) {
                    if (daoban.resetTable(tenBan)) {
                        JOptionPane.showMessageDialog(table, "Reset bàn thành công");
                    } else {
                        JOptionPane.showMessageDialog(table, "Không thể reset bàn");
                    }
                }
                daoban.updateStatus(tenBan, newValue);
                loadData();
            }

            if (col == 2) {
                String tenBan = model.getValueAt(row, 0).toString();
                String newValue = (String) model.getValueAt(row, col);
                String pv = model.getValueAt(row, 3).toString();
                if (pv.equals("Chưa phục vụ")) {
                    JOptionPane.showMessageDialog(table, "Chưa gọi món!");
                } else {
                    DAOTable daoban = new DAOTable();
                    if (daoban.updateCook(tenBan, newValue)) {
                        JOptionPane.showMessageDialog(table, "Đổi thành công trạng thái nấu bàn "
                                + tenBan + " thành " + newValue);
                    } else {
                        JOptionPane.showMessageDialog(table, "Thao tác thất bại");
                    }
                }
                loadData();
            }

            if (col == 3) {
                String tenBan = model.getValueAt(row, 0).toString();
                String nau = model.getValueAt(row, 2).toString();
                String newValue = model.getValueAt(row, 3).toString();
                Object oldObj = table.getClientProperty("oldValue");
                String oldValue = oldObj != null ? oldObj.toString() : "";
                if (oldValue.equals("Chưa phục vụ")) {
                    JOptionPane.showMessageDialog(table, "Hãy thêm món trước");
                } else if (newValue.equals("Đã phục vụ") && nau.equals("Chưa nấu")) {
                    JOptionPane.showMessageDialog(table, "Món chưa được nấu xong!");
                } else {
                    DAOTable daoban = new DAOTable();
                    if (!daoban.updateServe(tenBan, newValue)) {
                        JOptionPane.showMessageDialog(table, "Thay đổi thất bại");
                    } else {
                        JOptionPane.showMessageDialog(table, "Đổi thành công trạng thái phục vụ bàn "
                                + tenBan + " thành " + newValue);
                    }
                }
                loadData();
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));
        panel.add(createFilterRow(), BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(new Color(245, 247, 250));
        JButton refreshBtn = ButtonStyle.create("Làm mới", new Color(46, 204, 113));
        refreshBtn.addActionListener(e -> loadData());
        footer.add(refreshBtn);
        return footer;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(36);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setSelectionBackground(new Color(52, 152, 219));
        t.setSelectionForeground(Color.WHITE);
        t.setGridColor(new Color(230, 230, 230));
        t.setShowGrid(true);

        JTableHeader header = t.getTableHeader();
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        t.setDefaultRenderer(Object.class, center);
    }

    private JPanel createFilterRow() {
        JPanel p = new JPanel(new GridLayout(1, 7, 5, 5));
        p.setBackground(new Color(230, 240, 250));
        p.setBorder(new EmptyBorder(5, 15, 5, 15));

        p.add(new JLabel(""));
        fTrangThai = createFilterCombo(1, "Tất cả", "Trống", "Có khách");
        fNau = createFilterCombo(2, "Tất cả", "Chưa nấu", "Đã nấu");
        fPhucVu = createFilterCombo(3, "Tất cả", "Chưa phục vụ", "Đã gọi món", "Đã phục vụ");
        fThanhToan = createFilterCombo(4, "Tất cả", "Chưa thanh toán", "Đã thanh toán");
        p.add(fTrangThai);
        p.add(fNau);
        p.add(fPhucVu);
        p.add(fThanhToan);
        p.add(new JLabel(""));
        p.add(new JLabel(""));
        return p;
    }

    private JComboBox<String> createFilterCombo(int col, String... items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        c.addActionListener(e -> applyFilters());
        return c;
    }

    private void applyFilters() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        addFilter(filters, fTrangThai, 1);
        addFilter(filters, fNau, 2);
        addFilter(filters, fPhucVu, 3);
        addFilter(filters, fThanhToan, 4);
        sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private void addFilter(List<RowFilter<Object, Object>> filters, JComboBox<String> combo, int col) {
        String val = combo.getSelectedItem().toString();
        if (!val.equals("Tất cả")) {
            filters.add(RowFilter.regexFilter("^" + val + "$", col));
        }
    }

    private void setUpComboBoxColumn(JTable t, int col, String... items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ((JLabel) combo.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        t.getColumnModel().getColumn(col).setCellEditor(new DefaultCellEditor(combo));
    }

    private void addButtonToTable(int col, String text) {
        TableColumn column = table.getColumnModel().getColumn(col);
        column.setCellRenderer(new ButtonRenderer(text));
        column.setCellEditor(new ButtonEditor(new JCheckBox(), text, table));
    }

    private void loadData() {
        ArrayList<Table> arl = new DAOTable().getAllTable();
        showListTable(arl);
    }

    public void showListTable(ArrayList<Table> ds) {
        model.setRowCount(0);
        for (Table b : ds) {
            model.addRow(new Object[]{
                b.getTableName(), b.getTableStatus(), b.getTableCook(), b.getTableServe(), b.getTablePay(),
                "Chi tiết", "Thêm món"
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DanhSachBanForm().setVisible(true));
    }
}
