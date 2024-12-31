package sim.kantordesa.dashboard;

import java.awt.CardLayout;
import java.sql.Connection;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import sim.kantordesa.config.User;
import sim.kantordesa.config.koneksi;
import sim.kantordesa.master.Create_acc;
import sim.kantordesa.auth.login;
import sim.kantordesa.master.Akses_role;

public class Dashboard extends javax.swing.JFrame {

    private final Connection conn;
    private final User currentUser;

    public Dashboard(User currentUser, Set<String> userAccess) {
        this.currentUser = currentUser;
        initComponents();
        conn = koneksi.getConnection();
        NamaUser.setText("");
        Role.setText("");
        NamaUser.setText(currentUser.getFullName());
        Role.setText(currentUser.getRole());
        setSidebarVisibility(userAccess);
        Card.add(new Beranda().getContentPanel(), "Beranda");
        switchPanel(Card, "Beranda");
        Card.add(new Akses_role().getContentPanel(), "Akses Role");
        Card.add(new Create_acc().getContentPanel(), "Daftar Akun");
    }

    private static void switchPanel(JPanel content, String cardName) {
        CardLayout layout = (CardLayout) content.getLayout();
        layout.show(content, cardName);
    }
    
    private void setSidebarVisibility(Set<String> userAccess) {
    FormSuratMasuk.setVisible(userAccess.contains("Form Surat Masuk"));
    FormSuratKeluar.setVisible(userAccess.contains("Form Surat Keluar"));
    HistorySuratMasuk.setVisible(userAccess.contains("History Surat Masuk"));
    HistorySuratKeluar.setVisible(userAccess.contains("History Surat Keluar"));
    Disposisi.setVisible(userAccess.contains("Disposisi"));
    Validasi.setVisible(userAccess.contains("Validasi"));
    DaftarAkun.setVisible(userAccess.contains("Daftar Akun"));
    AksesRole.setVisible(userAccess.contains("Akses Role"));
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Sidebar = new javax.swing.JPanel();
        Beranda = new javax.swing.JLabel();
        FormSuratMasuk = new javax.swing.JLabel();
        FormSuratKeluar = new javax.swing.JLabel();
        HistorySuratMasuk = new javax.swing.JLabel();
        HistorySuratKeluar = new javax.swing.JLabel();
        Disposisi = new javax.swing.JLabel();
        Validasi = new javax.swing.JLabel();
        DaftarAkun = new javax.swing.JLabel();
        AksesRole = new javax.swing.JLabel();
        Keluar = new javax.swing.JLabel();
        NamaDesa = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Header = new javax.swing.JPanel();
        NamaUser = new javax.swing.JLabel();
        Role = new javax.swing.JLabel();
        Card = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIM-Kantor Desa");

        Sidebar.setBackground(new java.awt.Color(255, 255, 255));
        Sidebar.setPreferredSize(new java.awt.Dimension(200, 640));

        Beranda.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Beranda.setForeground(new java.awt.Color(19, 128, 97));
        Beranda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/home.png"))); // NOI18N
        Beranda.setText("Beranda");
        Beranda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BerandaMouseClicked(evt);
            }
        });

        FormSuratMasuk.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FormSuratMasuk.setForeground(new java.awt.Color(19, 128, 97));
        FormSuratMasuk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/form.png"))); // NOI18N
        FormSuratMasuk.setText("Form Surat Masuk");

        FormSuratKeluar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FormSuratKeluar.setForeground(new java.awt.Color(19, 128, 97));
        FormSuratKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/form.png"))); // NOI18N
        FormSuratKeluar.setText("Form Surat Keluar");

        HistorySuratMasuk.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        HistorySuratMasuk.setForeground(new java.awt.Color(19, 128, 97));
        HistorySuratMasuk.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        HistorySuratMasuk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/history.png"))); // NOI18N
        HistorySuratMasuk.setText("History Surat Masuk");

        HistorySuratKeluar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        HistorySuratKeluar.setForeground(new java.awt.Color(19, 128, 97));
        HistorySuratKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/history.png"))); // NOI18N
        HistorySuratKeluar.setText("History Surat Keluar");

        Disposisi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Disposisi.setForeground(new java.awt.Color(19, 128, 97));
        Disposisi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/disposisi.png"))); // NOI18N
        Disposisi.setText("Disposisi");

        Validasi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Validasi.setForeground(new java.awt.Color(19, 128, 97));
        Validasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/validasi.png"))); // NOI18N
        Validasi.setText("Validasi");

        DaftarAkun.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        DaftarAkun.setForeground(new java.awt.Color(19, 128, 97));
        DaftarAkun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/register.png"))); // NOI18N
        DaftarAkun.setText("Daftar Akun");
        DaftarAkun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DaftarAkunMouseClicked(evt);
            }
        });

        AksesRole.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        AksesRole.setForeground(new java.awt.Color(19, 128, 97));
        AksesRole.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/access.png"))); // NOI18N
        AksesRole.setText("Akses Role");
        AksesRole.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AksesRoleMouseClicked(evt);
            }
        });

        Keluar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Keluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/log_out.png"))); // NOI18N
        Keluar.setText("Keluar");
        Keluar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                KeluarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout SidebarLayout = new javax.swing.GroupLayout(Sidebar);
        Sidebar.setLayout(SidebarLayout);
        SidebarLayout.setHorizontalGroup(
            SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidebarLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Beranda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(FormSuratMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(HistorySuratMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(HistorySuratKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(FormSuratKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Disposisi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Validasi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DaftarAkun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AksesRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Keluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SidebarLayout.setVerticalGroup(
            SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidebarLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(Beranda)
                .addGap(18, 32, Short.MAX_VALUE)
                .addComponent(FormSuratMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 32, Short.MAX_VALUE)
                .addComponent(FormSuratKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 32, Short.MAX_VALUE)
                .addComponent(HistorySuratMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 32, Short.MAX_VALUE)
                .addComponent(HistorySuratKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 32, Short.MAX_VALUE)
                .addComponent(Disposisi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 32, Short.MAX_VALUE)
                .addComponent(Validasi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 33, Short.MAX_VALUE)
                .addComponent(DaftarAkun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 33, Short.MAX_VALUE)
                .addComponent(AksesRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                .addComponent(Keluar)
                .addGap(20, 20, 20))
        );

        Disposisi.getAccessibleContext().setAccessibleDescription("");

        NamaDesa.setBackground(new java.awt.Color(19, 128, 97));
        NamaDesa.setPreferredSize(new java.awt.Dimension(200, 60));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Sistem Informasi Manajemen");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("DESA TAMAN");

        javax.swing.GroupLayout NamaDesaLayout = new javax.swing.GroupLayout(NamaDesa);
        NamaDesa.setLayout(NamaDesaLayout);
        NamaDesaLayout.setHorizontalGroup(
            NamaDesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NamaDesaLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(NamaDesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        NamaDesaLayout.setVerticalGroup(
            NamaDesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NamaDesaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        Header.setBackground(new java.awt.Color(19, 128, 97));
        Header.setPreferredSize(new java.awt.Dimension(1300, 60));

        NamaUser.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        NamaUser.setForeground(new java.awt.Color(255, 255, 255));
        NamaUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sim/kantordesa/dashboard/icon/user.png"))); // NOI18N
        NamaUser.setText("Nama Lengkap User");

        Role.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Role.setForeground(new java.awt.Color(255, 255, 255));
        Role.setText("Role");

        NamaUser.setText("");
        NamaUser.setText(currentUser.getFullName());

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HeaderLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(NamaUser, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Role, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NamaUser)
                    .addComponent(Role))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        Card.setBackground(new java.awt.Color(255, 255, 255));
        Card.setPreferredSize(new java.awt.Dimension(1300, 640));
        Card.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                    .addComponent(NamaDesa, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Card, javax.swing.GroupLayout.DEFAULT_SIZE, 1291, Short.MAX_VALUE)
                    .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, 1291, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NamaDesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                    .addComponent(Card, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AksesRoleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AksesRoleMouseClicked
        switchPanel(Card, "Akses Role"); // Menampilkan panel Akses Role
    }//GEN-LAST:event_AksesRoleMouseClicked

    private void BerandaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BerandaMouseClicked
        switchPanel(Card, "Beranda"); // Menampilkan panel Beranda
    }//GEN-LAST:event_BerandaMouseClicked

    private void DaftarAkunMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DaftarAkunMouseClicked
        switchPanel(Card, "Daftar Akun");
    }//GEN-LAST:event_DaftarAkunMouseClicked

    private void KeluarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_KeluarMouseClicked
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin keluar?",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            login loginFrame = new login(); 
            loginFrame.setVisible(true);
            this.dispose(); 
        }
    }//GEN-LAST:event_KeluarMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AksesRole;
    private javax.swing.JLabel Beranda;
    private javax.swing.JPanel Card;
    private javax.swing.JLabel DaftarAkun;
    private javax.swing.JLabel Disposisi;
    private javax.swing.JLabel FormSuratKeluar;
    private javax.swing.JLabel FormSuratMasuk;
    private javax.swing.JPanel Header;
    private javax.swing.JLabel HistorySuratKeluar;
    private javax.swing.JLabel HistorySuratMasuk;
    private javax.swing.JLabel Keluar;
    private javax.swing.JPanel NamaDesa;
    private javax.swing.JLabel NamaUser;
    private javax.swing.JLabel Role;
    private javax.swing.JPanel Sidebar;
    private javax.swing.JLabel Validasi;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
