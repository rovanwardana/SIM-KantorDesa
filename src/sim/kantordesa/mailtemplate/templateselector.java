/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sim.kantordesa.mailtemplate;

import java.sql.*;
import sim.kantordesa.auth.login;
import sim.kantordesa.config.koneksi;

/**
 *
 * @author manii
 */
public class templateselector extends javax.swing.JFrame {
    
    public templateselector() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        body = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        nama_pengaju = new javax.swing.JLabel();
        text_namapengaju = new javax.swing.JTextField();
        template_surat = new javax.swing.JLabel();
        box_template_surat = new javax.swing.JComboBox<>();
        btn_next = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        header.setBackground(new java.awt.Color(19, 128, 97));

        title.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        title.setForeground(new java.awt.Color(255, 255, 255));
        title.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        title.setText("TEMPLATE SURAT");

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(title)
                .addContainerGap(434, Short.MAX_VALUE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(title)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        nama_pengaju.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nama_pengaju.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        nama_pengaju.setText("Nama Pengaju");

        text_namapengaju.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        template_surat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        template_surat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        template_surat.setText("Template Surat");

        box_template_surat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        box_template_surat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Surat Izin Keramaian", "Surat Keterangan Kehilangan", "Surat Keterangan Kelahiran", "Surat Keterangan Kematian", "Surat Keterangan Kurang Mampu", "Surat Keterangan Usaha", "Surat Permohonan Cerai" }));

        btn_next.setBackground(new java.awt.Color(19, 128, 97));
        btn_next.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_next.setForeground(new java.awt.Color(255, 255, 255));
        btn_next.setText("Lanjutkan");
        btn_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nextActionPerformed(evt);
            }
        });

        btn_back.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_back.setForeground(new java.awt.Color(19, 128, 97));
        btn_back.setText("Kembali");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(bodyLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bodyLayout.createSequentialGroup()
                        .addComponent(btn_next, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(box_template_surat, 0, 640, Short.MAX_VALUE)
                        .addComponent(template_surat)
                        .addComponent(nama_pengaju)
                        .addComponent(text_namapengaju)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(nama_pengaju)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(text_namapengaju, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(template_surat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(box_template_surat, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addGap(40, 40, 40)
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_next, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nextActionPerformed
        String applicantName = text_namapengaju.getText();
        String selectedTemplate = box_template_surat.getSelectedItem().toString();
        int mailTypeId = getMailTypeId(selectedTemplate);
        if (mailTypeId != 0 && !applicantName.isEmpty()) {
        saveApplicantData(applicantName, mailTypeId);
        mailform nextForm = new mailform(selectedTemplate);
        nextForm.setVisible(true);
        this.dispose();
        } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Nama Pengaju atau Template Surat tidak valid!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btn_nextActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        login dashboard = new login(); // Pastikan `Dashboard` adalah nama class untuk dashboard
        dashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_backActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(templateselector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(templateselector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(templateselector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(templateselector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new templateselector().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private javax.swing.JComboBox<String> box_template_surat;
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_next;
    private javax.swing.JPanel header;
    private javax.swing.JLabel nama_pengaju;
    private javax.swing.JLabel template_surat;
    private javax.swing.JTextField text_namapengaju;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables

    private void saveApplicantData(String applicantName, int mailTypeId) {
        try {
            Connection conn = koneksi.getConnection();
            String query = "INSERT INTO mail_content (applicant_name, mail_type_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, applicantName);
            ps.setInt(2, mailTypeId);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (SQLException ex) {
        javax.swing.JOptionPane.showMessageDialog(this, "Sistem Error!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int getMailTypeId(String templateName) {
    int mailTypeId = 0;
    try {
        Connection conn = koneksi.getConnection();
        String query = "SELECT mail_type_id FROM mail_type WHERE type_name = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, templateName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            mailTypeId = rs.getInt("mail_type_id");
        }
        rs.close();
        ps.close();
        conn.close();
    } catch (SQLException ex) {
        javax.swing.JOptionPane.showMessageDialog(this, "Sistem Error!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    return mailTypeId;
    }
}