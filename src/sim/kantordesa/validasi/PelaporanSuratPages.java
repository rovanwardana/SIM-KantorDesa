package sim.kantordesa.validasi;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import sim.kantordesa.config.koneksi;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author krisna 
 */
public class PelaporanSuratPages extends javax.swing.JFrame {

    private javax.swing.table.DefaultTableModel model;
    Connection c = koneksi.getConnection();

    public PelaporanSuratPages() {
        initComponents();
        showLineChart();
        createPieChart();

        model = new javax.swing.table.DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }
        };

        tbHistory.setModel(model);

        model.addColumn("No.");
        model.addColumn("Nomor Surat");
        model.addColumn("Nama Pemohon");
        model.addColumn("Tanggal Pengajuan");
        model.addColumn("Perihal");
        model.addColumn("Status Validasi Sekdes");
        model.addColumn("Status Validasi Kades");
        model.addColumn("Comment");
        model.addColumn("Aksi");

        setTableAction();
        adjustColumnWidths(tbHistory);

    }

    private void showLineChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            Statement s = c.createStatement();
            String sql = "SELECT DATE_FORMAT(created_at, '%Y-%m') AS bulan, COUNT(*) AS jumlah_surat FROM mail_content GROUP BY DATE_FORMAT(created_at, '%Y-%m') ORDER BY bulan;";
            ResultSet r = s.executeQuery(sql);

            while (r.next()) {
                String bulan = r.getString("bulan");
                int jumlahSurat = r.getInt("jumlah_surat");
                dataset.addValue(jumlahSurat, "Jumlah Surat", bulan);
            }

//            System.out.println(dataset.getValue(0, 0));
            r.close();
            s.close();
        } catch (SQLException e) {
            System.out.println("Error, " + e);
        }
        
        System.out.println("Dataset size: " + dataset.getRowCount());



        JFreeChart lineChart = ChartFactory.createLineChart(
                "Surat Masuk per Bulan",
                "Bulan",
                "Jumlah Surat",
                dataset
        );
        

            CategoryPlot plot = lineChart.getCategoryPlot();
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800,600));
        chartPanel1.removeAll();
        chartPanel1.setLayout(new BorderLayout());
        chartPanel1.add(chartPanel, BorderLayout.CENTER);
        chartPanel1.revalidate();
        chartPanel1.repaint();
    }
    
    
    private void createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Mengambil data dari database
        try {
            Statement s = c.createStatement();
            String sql = "SELECT t.type_name, COUNT(*) AS jumlah_surat FROM mail_content c JOIN mail_type t ON c.mail_type_id = t.mail_type_id GROUP BY t.type_name;";
            ResultSet r = s.executeQuery(sql);

            while (r.next()) {
                String category = r.getString("type_name");
                int jumlahSurat = r.getInt("jumlah_surat");
                dataset.setValue(category, jumlahSurat);
            }
            System.out.println(dataset.getValue(0));
            r.close();
            s.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println(dataset.getItemCount());

        // Membuat Pie Chart
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Jumlah Surat Masuk per Kategori", // Judul chart
                dataset,                          // Data untuk chart
                true,                             // Legend
                true,                             // Tooltip
                false                             // URLs
        );

        // Membungkus chart dalam ChartPanel
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel2.setPreferredSize(new java.awt.Dimension(400, 400));
        chartPanel2.removeAll();
        chartPanel2.setLayout(new BorderLayout());
        chartPanel2.add(chartPanel, BorderLayout.CENTER);
        chartPanel2.revalidate();
        chartPanel2.repaint();
    }
    
    
    
    public void setTableAction() {
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try {
            Statement s = c.createStatement();
            String sql = "select mail_id, mail_number, created_at, applicant_name, mail_comment, status_validation, status_lead, mail_comment, mail_type.type_name from mail_content inner join mail_type on mail_content.mail_type_id = mail_type.mail_type_id;";
            ResultSet r = s.executeQuery(sql);
            int i = 1;
            while (r.next()) {

                model.addRow(new Object[]{
                    i++,
                    r.getString("mail_number"),
                    r.getString("applicant_name"),
                    r.getString("created_at"),
                    r.getString("type_name"),
                    r.getBoolean("status_validation") == false ? "Reject" : "Accept",
                    r.getBoolean("status_lead") == false ? "Reject" : "Accept",
                    r.getString("mail_comment"),
                    r.getString("mail_id"),});

            }
            r.close();
            s.close();
        } catch (SQLException e) {
            System.out.println("Error, " + e);
        }
        tbHistory.getColumn("Status Validasi Sekdes").setCellRenderer(new StatusCellRenderer());
        tbHistory.getColumn("Status Validasi Kades").setCellRenderer(new StatusCellRenderer());
        tbHistory.getColumn("Aksi").setCellRenderer(new ButtonPanelRenderer());
        tbHistory.getColumn("Aksi").setCellEditor(new ButtonPanelEditor(tbHistory));
    }

    public static void adjustColumnWidths(JTable table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = getMaxPreferredWidth(table, column);
            tableColumn.setPreferredWidth(preferredWidth);
        }
    }

    private static int getMaxPreferredWidth(JTable table, int column) {
        int maxWidth = 0;
        TableColumn tableColumn = table.getColumnModel().getColumn(column);

        // Get the width of the column header
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        Component comp = headerRenderer.getTableCellRendererComponent(table, tableColumn.getHeaderValue(), false, false, 0, column);
        maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);

        // Get the width of the column content
        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
            comp = cellRenderer.getTableCellRendererComponent(table, table.getValueAt(row, column), false, false, row, column);
            maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);
        }

        return maxWidth + 10; // Add some margin
    }

    class ButtonPanelRenderer extends ButtonPanel implements TableCellRenderer {

        public ButtonPanelRenderer() {
            setBackground(Color.white);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            boolean statusValidation = "Accept".equals(table.getValueAt(row, 5));
            boolean statusLead = "Accept".equals(table.getValueAt(row, 6));
            Object mailComment = table.getValueAt(row, 7);
            boolean hasMailComment = (mailComment != null && !mailComment.toString().isEmpty() && statusValidation && statusLead);

//              boolean hasMailComment = ((table.getValueAt(row, 7) != null) && (table.getValueAt(row, 5) != false) && (table.getValueAt(row, 6) != false); // Assuming mail_comment is at index 7
            downloadButton.setVisible(hasMailComment);
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }

    }

    class ButtonPanelEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        ButtonPanel panel;
        JTable table;

//        public ButtonPanelEditor(JButton editButton, JButton deleteButton, JButton downloadButton) {
        public ButtonPanelEditor(JTable table) {
            this.table = table;
            panel = new ButtonPanel();

            panel.editButton.addActionListener(e -> handleEditButtonAction());
            panel.deleteButton.addActionListener(e -> handleDeleteButtonAction());
            panel.downloadButton.addActionListener(e -> handleDownloadButtonAction());

        }

        private void handleEditButtonAction() {
            System.out.println("Edit Button diklik");
        }

        private void handleDeleteButtonAction() {
            System.out.println("Delete Button diklik");
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(table, "Silahkan pilih baris terlebih dahulu");
                return;
            }

            String mailId = (String) table.getValueAt(row, 8);

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Apakah anda yakin ingin menghapus pengajuan surat ini?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                String query = "DELETE FROM mail_content WHERE mail_id = ?";
                try {
                    boolean hasil = koneksi.delete(query, mailId);
                    if (hasil) {
                        setTableAction();
                        JOptionPane.showMessageDialog(
                                null,
                                "Data Pengajuan Berhasil Dihapus");
                    }
                } catch (HeadlessException e) {
                    System.out.println("Error, " + e);
                }
            }
        }

        private void handleDownloadButtonAction() {
            System.out.println("Download Button diklik");
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            boolean statusValidation = "Accept".equals(table.getValueAt(row, 5));
            boolean statusLead = "Accept".equals(table.getValueAt(row, 6));
            Object mailComment = table.getValueAt(row, 7);

            boolean hasMailComment = (mailComment != null && !mailComment.toString().isEmpty() && statusValidation && statusLead);
            panel.downloadButton.setVisible(hasMailComment);
            return panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    static class ButtonPanel extends javax.swing.JPanel {

        public javax.swing.JButton editButton;
        public javax.swing.JButton deleteButton;
        public javax.swing.JButton downloadButton;

        public ButtonPanel() {
            FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
            setLayout(layout);
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");
            downloadButton = new JButton("Download");

            add(editButton);
            add(deleteButton);
            add(downloadButton);

        }
    }

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        setTableAction();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTb = new javax.swing.JPanel();
        panelScrollTb = new javax.swing.JScrollPane();
        tbHistory = new javax.swing.JTable();
        labelHistory = new javax.swing.JLabel();
        refresh = new javax.swing.JButton();
        chartPanel1 = new javax.swing.JPanel();
        chartPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1291, 634));

        panelTb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        tbHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "No. Surat", "Tanggal Surat Masuk", "Status Validasi Sekdes", "Status Validasi Kades", "Perihal", "Aksi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbHistory.setRowHeight(30);
        tbHistory.setShowGrid(true);
        panelScrollTb.setViewportView(tbHistory);
        if (tbHistory.getColumnModel().getColumnCount() > 0) {
            tbHistory.getColumnModel().getColumn(0).setMinWidth(30);
            tbHistory.getColumnModel().getColumn(0).setMaxWidth(30);
            tbHistory.getColumnModel().getColumn(1).setMinWidth(120);
            tbHistory.getColumnModel().getColumn(1).setMaxWidth(130);
            tbHistory.getColumnModel().getColumn(2).setMinWidth(140);
            tbHistory.getColumnModel().getColumn(2).setMaxWidth(150);
            tbHistory.getColumnModel().getColumn(3).setMinWidth(140);
            tbHistory.getColumnModel().getColumn(3).setMaxWidth(150);
            tbHistory.getColumnModel().getColumn(4).setMinWidth(140);
            tbHistory.getColumnModel().getColumn(4).setMaxWidth(150);
            tbHistory.getColumnModel().getColumn(5).setMinWidth(120);
            tbHistory.getColumnModel().getColumn(5).setMaxWidth(130);
        }

        labelHistory.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelHistory.setText("Pelaporan Surat Masuk");

        refresh.setBackground(new java.awt.Color(19, 128, 97));
        refresh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        refresh.setForeground(new java.awt.Color(255, 255, 255));
        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        chartPanel1.setLayout(new java.awt.BorderLayout());

        chartPanel2.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout panelTbLayout = new javax.swing.GroupLayout(panelTb);
        panelTb.setLayout(panelTbLayout);
        panelTbLayout.setHorizontalGroup(
            panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTbLayout.createSequentialGroup()
                        .addComponent(labelHistory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(panelTbLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(chartPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
                        .addComponent(chartPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTbLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelScrollTb, javax.swing.GroupLayout.PREFERRED_SIZE, 1155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        panelTbLayout.setVerticalGroup(
            panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelHistory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chartPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(chartPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24)
                .addComponent(panelScrollTb, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
//    private void initComponents() {
//        chartContainer = new javax.swing.JPanel();
//        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//        setTitle("Line Chart");
//        
//        chartContainer.setLayout(new java.awt.BorderLayout());
//        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
//        getContentPane().setLayout(layout);
//        layout.setHorizontalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addComponent(chartContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//        );
//        layout.setVerticalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addComponent(chartContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//        );
//        
//        pack();
//    }

//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(() -> {
//            new PelaporanSuratPages().setVisible(true);
//        });
//    }
//    
    private javax.swing.JPanel chartContainer;

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        ValidationPages.main(null);
        dispose();
    }// GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PelaporanSuratPages().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chartPanel1;
    private javax.swing.JPanel chartPanel2;
    private javax.swing.JLabel labelHistory;
    private javax.swing.JScrollPane panelScrollTb;
    private javax.swing.JPanel panelTb;
    private javax.swing.JButton refresh;
    private javax.swing.JTable tbHistory;
    // End of variables declaration//GEN-END:variables
}
