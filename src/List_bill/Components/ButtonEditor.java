package List_bill.Components;

import UI_NhanVien.ThuNgan.UIOrderDetail;
import DAO.DAOTable;
import DAO.DAOOrder;
import UI_NhanVien.ThuNgan.UIThemKhachHang;
import UI_NhanVien.ThuNgan.UIDanhSachOrder;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {

    private final JButton button;
    private boolean clicked;
    private final String action;
    private final JTable table;
    private final DefaultTableModel model;
    private final UIDanhSachOrder frame;

    public ButtonEditor(JCheckBox checkBox, String action, JTable table, DefaultTableModel model, UIDanhSachOrder frame) {
        super(checkBox);
        this.action = action;
        this.table = table;
        this.model = model;
        this.frame = frame;
        button = ButtonStyle.create(action, new Color(52, 152, 219));
        button.setBorder(new RoundedBorder(20));
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object val, boolean isSelected, int row, int col) {
        clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked) {
            int row = table.getSelectedRow();
            if (row != -1) {
                String id = model.getValueAt(row, 0).toString();
                String tenBan = model.getValueAt(row, 1).toString();
                if (action.equals("Xem")) {
                    new UIOrderDetail(id, tenBan).setVisible(true);
                } else if (action.equals("Thanh toán")) {
                    int conf = JOptionPane.showConfirmDialog(table, "Thanh toán bàn " + tenBan + "?", "Xác nhận thanh toán", JOptionPane.YES_NO_OPTION);
                    if (conf == JOptionPane.YES_OPTION) { 
                        new DAOOrder().updatePaid(id);
                        new DAOTable().updatePaid(tenBan);

                        String tongTienStr = model.getValueAt(row, 2).toString().replace(",", "");
                        try {
                            int tongTien = (int) Long.parseLong(tongTienStr);
                            new UIThemKhachHang(tongTien).setVisible(true);
                        } catch (NumberFormatException ex) {
                            new UIThemKhachHang(0).setVisible(true);
                        }

                        JOptionPane.showMessageDialog(table,
                                "Thanh toán thành công cho " + tenBan + "!",
                                "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                        if (frame != null) {
                            frame.loadData();
                        }
                    }
                }
            }
        }
        clicked = false;
        return action;
    }
}
