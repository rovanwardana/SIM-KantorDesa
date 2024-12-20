/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sim.kantordesa.validasi;

import java.awt.Component;
import javax.swing.JTable;

/**
 *
 * @author Razik
 */
public class ButtonRenderer extends javax.swing.JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Periksa" : value.toString());
        return this;
    }
}
