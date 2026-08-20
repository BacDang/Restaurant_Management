package List_Table.Components;

import javax.swing.*;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {
    private static final long serialVersionUID = 1L;
    private final JButton button;
    private final String type;
    private boolean clicked;

    public ButtonEditor(JCheckBox box, String type, JTable table) {
        super(box);
        this.type = type;
        button = ButtonStyle.create(type, new Color(52, 152, 219));
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable t, Object val, boolean sel, int row, int col) {
        clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        clicked = false;
        return type;
    }
}