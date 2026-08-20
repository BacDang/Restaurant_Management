package List_bill.Components;

import javax.swing.border.Border;
import java.awt.*;

public class RoundedBorder implements Border {
    private final int r;
    RoundedBorder(int r) { this.r = r; }
    public Insets getBorderInsets(Component c) { return new Insets(r + 1, r + 1, r + 2, r); }
    public boolean isBorderOpaque() { return false; }
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        g.setColor(Color.WHITE);
        g.drawRoundRect(x, y, w - 1, h - 1, r, r);
    }
}