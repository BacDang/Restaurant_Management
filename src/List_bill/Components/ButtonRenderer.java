package List_bill.Components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer(String text) {
        setText(text);
        setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        setForeground(Color.WHITE);
        setBackground(new Color(52, 152, 219));
        setFocusPainted(false);
        setBorder(new RoundedBorder(8));
    }

    public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
        setText(val == null ? "" : val.toString());
        setBackground(sel ? new Color(41, 128, 185) : new Color(52, 152, 219));
        return this;
    }
}