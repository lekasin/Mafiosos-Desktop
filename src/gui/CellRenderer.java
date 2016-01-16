package gui;

import personer.Spiller;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by lars-erikkasin on 16.01.2016.
 */
public class CellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        Border paddingBorder = BorderFactory.createEmptyBorder(0,5,0,5);

        Font font = new Font(label.getFont().getName(), Font.PLAIN, 14);
        label.setFont(font);

        if (!isSelected)
            label.setBackground(index % 2 == 0 ? new Color(190, 226, 241, 160) : Color.white);
        else
            label.setBackground(new Color(130, 160, 180));

        label.setBorder(paddingBorder);
        return label;
    }

}
