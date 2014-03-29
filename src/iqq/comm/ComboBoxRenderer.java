package iqq.comm;

import iqq.util.QQEnvironment;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ComboBoxRenderer extends JLabel
        implements ListCellRenderer {

    private Font uhOhFont;

    public ComboBoxRenderer() {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

    /*
     * This method finds the image and text corresponding to the selected value
     * and returns the label, set up to display the text and image.
     */
    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        //Set the icon and text.  If icon was null, say so.
        String path = QQEnvironment.getConfigPath() + value.toString() + File.separator + "face" + File.separator + "face.jpg";
        //Log.println(path);
        File f = new File(path);
        Icon icon = null;
        if (f.exists()) {
            icon = new ImageIcon(path);
        }
        String pet = value.toString();
        if (icon != null) {
            setIcon(icon);
            setText(pet);
            setFont(list.getFont());
        } else {
             setText(pet);
             setFont(list.getFont());
        }

        return this;
    }
}