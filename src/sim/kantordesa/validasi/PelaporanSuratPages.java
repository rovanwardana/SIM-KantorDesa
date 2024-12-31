package sim.kantordesa.validasi;

import com.formdev.flatlaf.FlatLightLaf;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
//import javax.swing.text.Document;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
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
        model.addColumn("Tipe Surat");
        model.addColumn("Total Pengajuan");

        setTableAction();
        adjustColumnWidths(tbHistory);

    }

    private JFreeChart showLineChart() {
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
        
        return lineChart;
    }
    
    
    private JFreeChart createPieChart() {
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
        
        return pieChart;
    }
    
    
    
    public void setTableAction() {
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try {
            Statement s = c.createStatement();
            String sql = "SELECT mail_type.type_name, COUNT(mail_content.mail_id) AS total_pengajuan " +
                     "FROM mail_content " +
                     "INNER JOIN mail_type ON mail_content.mail_type_id = mail_type.mail_type_id " +
                     "GROUP BY mail_type.type_name " +
                     "ORDER BY mail_type.type_name ASC;";
            ResultSet r = s.executeQuery(sql);
            int i = 1;
            while (r.next()) {
                model.addRow(new Object[]{
                    i++,
                    r.getString("type_name"),
                    r.getInt("total_pengajuan")
                });

            }
            r.close();
            s.close();
        } catch (SQLException e) {
            System.out.println("Error, " + e);
        }
        tbHistory.getColumnModel().getColumn(0).setHeaderValue("No");
        tbHistory.getColumnModel().getColumn(1).setHeaderValue("Tipe Surat");
        tbHistory.getColumnModel().getColumn(2).setHeaderValue("Total Pengajuan");
        
        
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

        return maxWidth + 10; 
    }
    
    public void addJTableToPDF(JTable table, Document document) {
    try {
        // Membuat tabel PDF dengan jumlah kolom sesuai JTable
        PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
        pdfTable.setWidthPercentage(100);

        // Menambahkan header dari JTable
        for (int i = 0; i < table.getColumnCount(); i++) {
            PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i)));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfTable.addCell(cell);
        }

        // Menambahkan isi tabel dari JTable
        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getColumnCount(); col++) {
                Object value = table.getValueAt(row, col);
                pdfTable.addCell(value != null ? value.toString() : "");
            }
        }

        // Menambahkan tabel ke dokumen PDF
        document.add(pdfTable);
    } catch (DocumentException e) {
        System.out.println("Error saat menambahkan JTable ke PDF: " + e.getMessage());
    }
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
    
    private void exportChartAsImage(JFreeChart chart, File file) {
    try {
        ChartUtilities.saveChartAsPNG(file, chart, 800, 600);
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan grafik: " + e.getMessage());
    }
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

        sideBarHistory = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        administrasiBar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        panelTb = new javax.swing.JPanel();
        panelScrollTb = new javax.swing.JScrollPane();
        tbHistory = new javax.swing.JTable();
        labelHistory = new javax.swing.JLabel();
        refresh = new javax.swing.JButton();
        chartPanel1 = new javax.swing.JPanel();
        chartPanel2 = new javax.swing.JPanel();
        UnduhLaporan = new javax.swing.JButton();
        usernameBar = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        sideBarHistory.setBackground(new java.awt.Color(255, 255, 255));
        sideBarHistory.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(106, 93, 172));
        jLabel4.setText("Sekretaris");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Username");

        jButton2.setBackground(new java.awt.Color(83, 192, 161));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Home");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Menu Utama");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/validasi/gambar/iconamoon_profile-circle-fill.png"))); // NOI18N

        jButton3.setText("Disposisi");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Pengajuan Surat");
        jButton4.setBorder(null);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Pelaporan Surat");
        jButton5.setBorder(null);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/validasi/gambar/streamline_chat-bubble-square-write-solid.png"))); // NOI18N

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/validasi/gambar/pepicons-pop_paper-plane-circle-filled.png"))); // NOI18N

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/validasi/gambar/mdi_graph-box.png"))); // NOI18N

        jButton6.setText("Validasi");
        jButton6.setBorder(null);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/validasi/gambar/lets-icons_check-fill.png"))); // NOI18N

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/validasi/gambar/el_off.png"))); // NOI18N

        jButton7.setText("Keluar");
        jButton7.setBorder(null);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sideBarHistoryLayout = new javax.swing.GroupLayout(sideBarHistory);
        sideBarHistory.setLayout(sideBarHistoryLayout);
        sideBarHistoryLayout.setHorizontalGroup(
            sideBarHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarHistoryLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(12, 12, 12)
                .addGroup(sideBarHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18))
            .addGroup(sideBarHistoryLayout.createSequentialGroup()
                .addGroup(sideBarHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(sideBarHistoryLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(sideBarHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(sideBarHistoryLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(5, 5, 5)
                                .addComponent(jButton4))
                            .addComponent(jLabel6)
                            .addGroup(sideBarHistoryLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))
                            .addGroup(sideBarHistoryLayout.createSequentialGroup()
                                .addGroup(sideBarHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(sideBarHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton6)
                                    .addComponent(jButton5)
                                    .addComponent(jButton7))))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        sideBarHistoryLayout.setVerticalGroup(
            sideBarHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarHistoryLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(sideBarHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(sideBarHistoryLayout.createSequentialGroup()
                        .addGroup(sideBarHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(sideBarHistoryLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4))
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(sideBarHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(sideBarHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13))
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(sideBarHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(sideBarHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(jButton7))
                .addContainerGap(989, Short.MAX_VALUE))
        );

        administrasiBar.setBackground(new java.awt.Color(19, 128, 97));
        administrasiBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Administrasi");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/validasi/gambar/akar-icons_three-line-horizontal3.png"))); // NOI18N

        javax.swing.GroupLayout administrasiBarLayout = new javax.swing.GroupLayout(administrasiBar);
        administrasiBar.setLayout(administrasiBarLayout);
        administrasiBarLayout.setHorizontalGroup(
            administrasiBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(administrasiBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addContainerGap())
        );
        administrasiBarLayout.setVerticalGroup(
            administrasiBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(administrasiBarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(administrasiBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7))
                .addContainerGap(19, Short.MAX_VALUE))
        );

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

        UnduhLaporan.setBackground(new java.awt.Color(19, 128, 97));
        UnduhLaporan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        UnduhLaporan.setForeground(new java.awt.Color(255, 255, 255));
        UnduhLaporan.setText("Unduh");
        UnduhLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnduhLaporanActionPerformed(evt);
            }
        });

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
                        .addComponent(UnduhLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(panelTbLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(chartPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                        .addComponent(chartPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTbLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelScrollTb, javax.swing.GroupLayout.PREFERRED_SIZE, 878, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(184, 184, 184))
        );
        panelTbLayout.setVerticalGroup(
            panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelHistory)
                    .addComponent(UnduhLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addGroup(panelTbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chartPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(chartPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addComponent(panelScrollTb, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(502, Short.MAX_VALUE))
        );

        usernameBar.setBackground(new java.awt.Color(19, 128, 97));
        usernameBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Username");

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/validasi/gambar/iconamoon_profile-circle-fillnew.png"))); // NOI18N

        javax.swing.GroupLayout usernameBarLayout = new javax.swing.GroupLayout(usernameBar);
        usernameBar.setLayout(usernameBarLayout);
        usernameBarLayout.setHorizontalGroup(
            usernameBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, usernameBarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(14, 14, 14))
        );
        usernameBarLayout.setVerticalGroup(
            usernameBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usernameBarLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(usernameBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel14))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sideBarHistory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(administrasiBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernameBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernameBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(administrasiBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sideBarHistory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
   
    private javax.swing.JPanel chartContainer;

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void UnduhLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UnduhLaporanActionPerformed
        try{
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan Laporan Sebagai");
            fileChooser.setSelectedFile(new File("LaporanSurat.pdf"));
            int userSelection = fileChooser.showSaveDialog(this);
            
            if(userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                
                com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4.rotate()); 
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                
                Font tittleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                Paragraph title = new Paragraph("Laporan Surat Masuk", tittleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph("\n"));
                
                // Ekspor Line Chart sebagai gambar
                File lineChartFile = new File("line_chart_temp.png");
                exportChartAsImage(showLineChart(), lineChartFile);
                
                File pieChartFile = new File("pie_chart_temp.png");
                exportChartAsImage(createPieChart(), pieChartFile);
                
                // Tambahkan grafik Line Chart ke PDF dengan ukuran disesuaikan
                Image lineChartImage = Image.getInstance(lineChartFile.getAbsolutePath());
                lineChartImage.scaleToFit(675, 475); 
                lineChartImage.setAlignment(Element.ALIGN_CENTER);
                document.add(lineChartImage);
                
                document.newPage(); // Jarak antar grafik
                
                // Tambahkan grafik Pie Chart ke PDF dengan ukuran disesuaikan
                Image pieChartImage = Image.getInstance(pieChartFile.getAbsolutePath());
                pieChartImage.scaleToFit(700, 500); 
                pieChartImage.setAlignment(Element.ALIGN_CENTER);
                document.add(pieChartImage);
                
                document.newPage(); // Jarak antar elemen
                
                // Menyisipkan tabel dari JTable ke PDF
                addJTableToPDF(tbHistory, document);
                
                document.close();
                
                // Hapus file sementara
                lineChartFile.delete();
                pieChartFile.delete();

                JOptionPane.showMessageDialog(this, "Laporan berhasil disimpan di " + filePath);

            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat membuat laporan: " + e.getMessage());
        }// TODO add your handling code here:
    }//GEN-LAST:event_UnduhLaporanActionPerformed

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
    private javax.swing.JButton UnduhLaporan;
    private javax.swing.JPanel administrasiBar;
    private javax.swing.JPanel chartPanel1;
    private javax.swing.JPanel chartPanel2;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel labelHistory;
    private javax.swing.JScrollPane panelScrollTb;
    private javax.swing.JPanel panelTb;
    private javax.swing.JButton refresh;
    private javax.swing.JPanel sideBarHistory;
    private javax.swing.JTable tbHistory;
    private javax.swing.JPanel usernameBar;
    // End of variables declaration//GEN-END:variables
}
