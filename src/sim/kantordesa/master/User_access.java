package sim.kantordesa.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import sim.kantordesa.config.koneksi;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JCheckBox;

public class User_access extends javax.swing.JFrame {

    private final Connection conn;

    public User_access() {
        initComponents();
        conn = koneksi.getConnection();
        getAllData();
        getData();
        role();
        tampilan();
        hideCheckboxes();
        nonAktifButton();
    }

    private void getAllData() {
        DefaultTableModel model = (DefaultTableModel) tabel_data.getModel();
        model.setRowCount(0);

        try {
            String sql = "SELECT role.id_role, role.role_name, access.access_name "
                    + "FROM role "
                    + "INNER JOIN role_access ON role.id_role = role_access.role_id "
                    + "INNER JOIN access ON role_access.access_id = access.access_id";
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_role"),
                    rs.getString("role_name"),
                    rs.getString("access_name")
                };
                model.addRow(row);
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            Logger.getLogger(User_access.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void getData() {
        DefaultTableModel model = (DefaultTableModel) tabel_data.getModel();
        model.setRowCount(0);

        try {
            String selectedRole = cmb_role.getSelectedItem() != null ? cmb_role.getSelectedItem().toString() : "";
            if (roleMap.containsKey(selectedRole)) {
                int roleId = roleMap.get(selectedRole);

                String sql = "SELECT role.id_role, role.role_name, access.access_name "
                        + "FROM role "
                        + "INNER JOIN role_access ON role.id_role = role_access.role_id "
                        + "INNER JOIN access ON role_access.access_id = access.access_id "
                        + "WHERE role.id_role = ?";
                PreparedStatement st = conn.prepareStatement(sql);
                st.setInt(1, roleId);
                ResultSet rs = st.executeQuery();

                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("id_role"),
                        rs.getString("role_name"),
                        rs.getString("access_name")
                    };
                    model.addRow(row);
                }

                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(User_access.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Map<String, Integer> roleMap = new HashMap<>();

    public void role() {
        try {
            String sql = "SELECT * FROM role";
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            getData();
            getAllData();

            // Tambahkan item "Pilih Role" pertama di combobox
            cmb_role.addItem("Pilih Role");

            while (rs.next()) {
                String roleName = rs.getString("role_name");
                int roleId = rs.getInt("id_role");
                roleMap.put(roleName, roleId);
                cmb_role.addItem(roleName);
            }

            cmb_role.addItem("Baru"); // Tambahkan item "Baru"

            // ActionListener untuk combobox cmb_role
            cmb_role.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedRole = cmb_role.getSelectedItem() != null ? cmb_role.getSelectedItem().toString() : "";

                    if (selectedRole.equals("Baru")) {
                        // Menampilkan komponen untuk input role baru
                        getAllData();
                        lbl_roleBaru.setVisible(true);
                        txt_roleBaru.setVisible(true);
                        txt_id.setText("");
                        btn_tambahRole.setVisible(true);

                        // Sembunyikan semua checkbox
                        hideCheckboxes();

                        // Nonaktifkan tombol aksi lainnya
                        nonAktifButton();

                    } else if (roleMap.containsKey(selectedRole)) {
                        // Reset label input role baru
                        getData();
                        lbl_roleBaru.setVisible(false);
                        txt_roleBaru.setVisible(false);
                        btn_tambahRole.setVisible(false);

                        // Dapatkan ID role dari roleMap
                        int roleId = roleMap.get(selectedRole);
                        txt_id.setText(String.valueOf(roleId)); // Tampilkan ID role

                        // Muat akses untuk role yang dipilih
                        loadRoleAccess(roleId);

                        // Tampilkan semua checkbox
                        showCheckboxes();

                        // Aktifkan tombol aksi
                        aktifButton();
                    } else {
                        // Jika "Pilih Role" dipilih
                        getAllData();
                        txt_id.setText(""); // Kosongkan ID
                        hideCheckboxes();
                        nonAktifButton();
                    }
                }
            });

        } catch (Exception e) {
            Logger.getLogger(User_access.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void addAccessForRole(int roleId) {
        try {
            JCheckBox[] checkboxes = {
                cb_ManajemenData, cb_MelihatDaftarDisposisi, cb_MelihatDaftarSuratKeluar,
                cb_MelihatDaftarSuratMasuk, cb_MembalasSuratMasuk, cb_MenandaiDisposisiSelesai,
                cb_MencetakSuratKeluar, cb_MendisposisiSuratMasuk, cb_MengeditDisposisi,
                cb_MenghapusDisposisi, cb_MenghapusSuratKeluar, cb_MengirimFormSuratKeluar,
                cb_MengirimFormSuratMasuk, cb_MengisiFormSuratKeluar, cb_MengisiFormSuratMasuk,
                cb_ValidasiSurat
            };

            for (JCheckBox cb : checkboxes) {
                if (cb.isSelected()) {
                    String accessName = cb.getText();

                    // Periksa apakah akses sudah ada di database
                    int accessId = getExistingAccessId(accessName);

                    if (accessId > 0) {
                        // Periksa apakah role sudah memiliki akses tersebut
                        String checkSql = "SELECT COUNT(*) FROM role_access WHERE role_id = ? AND access_id = ?";
                        PreparedStatement psCheck = conn.prepareStatement(checkSql);
                        psCheck.setInt(1, roleId);
                        psCheck.setInt(2, accessId);
                        ResultSet rsCheck = psCheck.executeQuery();

                        if (rsCheck.next() && rsCheck.getInt(1) == 0) {
                            // Jika belum ada, tambahkan akses ke role_access
                            String insertSql = "INSERT INTO role_access (role_id, access_id) VALUES (?, ?)";
                            PreparedStatement psInsert = conn.prepareStatement(insertSql);
                            psInsert.setInt(1, roleId);
                            psInsert.setInt(2, accessId);
                            psInsert.executeUpdate();
                        }

                        rsCheck.close();
                        psCheck.close();
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Akses berhasil ditambahkan untuk role dengan ID: " + roleId);
            getData(); // Refresh data tabel

        } catch (SQLException e) {
            Logger.getLogger(User_access.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menambahkan akses: " + e.getMessage());
        }
    }

    private boolean isAccessAlreadyAssigned(int roleId, int accessId) throws SQLException {
        String sqlCheckAccess = "SELECT COUNT(*) AS count FROM role_access WHERE role_id = ? AND access_id = ?";
        PreparedStatement psCheck = conn.prepareStatement(sqlCheckAccess);
        psCheck.setInt(1, roleId);
        psCheck.setInt(2, accessId);
        ResultSet rs = psCheck.executeQuery();

        if (rs.next()) {
            return rs.getInt("count") > 0; // Jika COUNT > 0, artinya sudah ada
        }

        return false; // Tidak ditemukan
    }

    private int getExistingAccessId(String accessName) throws SQLException {
        String sqlGetAccessId = "SELECT access_id FROM access WHERE access_name = ?";
        PreparedStatement ps = conn.prepareStatement(sqlGetAccessId);
        ps.setString(1, accessName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("access_id");
        } else {
            JOptionPane.showMessageDialog(this, "Akses '" + accessName + "' tidak ditemukan di database.");
            return -1; // Jika tidak ditemukan, kembalikan nilai invalid
        }
    }

    private void resetCheckboxes() {
        JCheckBox[] checkboxes = {
            cb_ManajemenData, cb_MelihatDaftarDisposisi, cb_MelihatDaftarSuratKeluar,
            cb_MelihatDaftarSuratMasuk, cb_MembalasSuratMasuk, cb_MenandaiDisposisiSelesai,
            cb_MencetakSuratKeluar, cb_MendisposisiSuratMasuk, cb_MengeditDisposisi,
            cb_MenghapusDisposisi, cb_MenghapusSuratKeluar, cb_MengirimFormSuratKeluar,
            cb_MengirimFormSuratMasuk, cb_MengisiFormSuratKeluar, cb_MengisiFormSuratMasuk,
            cb_ValidasiSurat
        };

        for (JCheckBox cb : checkboxes) {
            cb.setSelected(false); // Hilangkan centang
        }
    }

    private void loadRoleAccess(int roleId) {
        try {
            // Reset semua checkbox
            resetCheckboxes();

            // Ambil akses untuk role yang dipilih
            String sql = "SELECT access.access_name "
                    + "FROM role_access "
                    + "INNER JOIN access ON role_access.access_id = access.access_id "
                    + "WHERE role_access.role_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, roleId);
            ResultSet rs = st.executeQuery();

            // Loop melalui hasil query dan centang checkbox yang sesuai
            while (rs.next()) {
                String accessName = rs.getString("access_name");

                // Cocokkan access_name dengan checkbox
                JCheckBox[] checkboxes = {
                    cb_ManajemenData, cb_MelihatDaftarDisposisi, cb_MelihatDaftarSuratKeluar,
                    cb_MelihatDaftarSuratMasuk, cb_MembalasSuratMasuk, cb_MenandaiDisposisiSelesai,
                    cb_MencetakSuratKeluar, cb_MendisposisiSuratMasuk, cb_MengeditDisposisi,
                    cb_MenghapusDisposisi, cb_MenghapusSuratKeluar, cb_MengirimFormSuratKeluar,
                    cb_MengirimFormSuratMasuk, cb_MengisiFormSuratKeluar, cb_MengisiFormSuratMasuk,
                    cb_ValidasiSurat
                };

                for (JCheckBox cb : checkboxes) {
                    if (cb.getText().equals(accessName)) {
                        cb.setSelected(true);
                    }
                }
            }

            rs.close();
            st.close();

        } catch (SQLException e) {
            Logger.getLogger(User_access.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Set<String> getExistingAccessNames(int roleId) {
        Set<String> existingAccess = new HashSet<>();
        try {
            String sql = "SELECT access.access_name "
                    + "FROM role_access "
                    + "INNER JOIN access ON role_access.access_id = access.access_id "
                    + "WHERE role_access.role_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, roleId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                existingAccess.add(rs.getString("access_name"));
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            Logger.getLogger(User_access.class.getName()).log(Level.SEVERE, null, e);
        }
        return existingAccess;
    }

    private void hideCheckboxes() {
        lbl_akses.setVisible(false);
        cb_ManajemenData.setVisible(false);
        cb_MelihatDaftarDisposisi.setVisible(false);
        cb_MelihatDaftarSuratKeluar.setVisible(false);
        cb_MelihatDaftarSuratMasuk.setVisible(false);
        cb_MembalasSuratMasuk.setVisible(false);
        cb_MenandaiDisposisiSelesai.setVisible(false);
        cb_MencetakSuratKeluar.setVisible(false);
        cb_MendisposisiSuratMasuk.setVisible(false);
        cb_MengeditDisposisi.setVisible(false);
        cb_MenghapusDisposisi.setVisible(false);
        cb_MenghapusSuratKeluar.setVisible(false);
        cb_MengirimFormSuratKeluar.setVisible(false);
        cb_MengirimFormSuratMasuk.setVisible(false);
        cb_MengisiFormSuratKeluar.setVisible(false);
        cb_MengisiFormSuratMasuk.setVisible(false);
        cb_ValidasiSurat.setVisible(false);
    }

// Menampilkan checkbox akses yang terkait dengan role yang dipilih
    private void showCheckboxes() {
        lbl_akses.setVisible(true);
        cb_ManajemenData.setVisible(true);
        cb_MelihatDaftarDisposisi.setVisible(true);
        cb_MelihatDaftarSuratKeluar.setVisible(true);
        cb_MelihatDaftarSuratMasuk.setVisible(true);
        cb_MembalasSuratMasuk.setVisible(true);
        cb_MenandaiDisposisiSelesai.setVisible(true);
        cb_MencetakSuratKeluar.setVisible(true);
        cb_MendisposisiSuratMasuk.setVisible(true);
        cb_MengeditDisposisi.setVisible(true);
        cb_MenghapusDisposisi.setVisible(true);
        cb_MenghapusSuratKeluar.setVisible(true);
        cb_MengirimFormSuratKeluar.setVisible(true);
        cb_MengirimFormSuratMasuk.setVisible(true);
        cb_MengisiFormSuratKeluar.setVisible(true);
        cb_MengisiFormSuratMasuk.setVisible(true);
        cb_ValidasiSurat.setVisible(true);
    }

    private void nonAktifButton() {
        btn_tambah.setVisible(false);
        btn_hapus.setVisible(false);
    }

    private void aktifButton() {
        btn_tambah.setVisible(true);
        btn_hapus.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbl_akses = new javax.swing.JLabel();
        cb_MengisiFormSuratKeluar = new javax.swing.JCheckBox();
        cb_MengirimFormSuratKeluar = new javax.swing.JCheckBox();
        cb_ValidasiSurat = new javax.swing.JCheckBox();
        cb_MelihatDaftarSuratKeluar = new javax.swing.JCheckBox();
        cb_MencetakSuratKeluar = new javax.swing.JCheckBox();
        cb_MenghapusSuratKeluar = new javax.swing.JCheckBox();
        cb_MengisiFormSuratMasuk = new javax.swing.JCheckBox();
        cb_MengirimFormSuratMasuk = new javax.swing.JCheckBox();
        cb_MelihatDaftarSuratMasuk = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_data = new javax.swing.JTable();
        cb_MembalasSuratMasuk = new javax.swing.JCheckBox();
        cb_MendisposisiSuratMasuk = new javax.swing.JCheckBox();
        cb_ManajemenData = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        btn_tambah = new javax.swing.JButton();
        txt_cari = new javax.swing.JTextField();
        btn_cari = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        cb_MelihatDaftarDisposisi = new javax.swing.JCheckBox();
        cb_MengeditDisposisi = new javax.swing.JCheckBox();
        cb_MenghapusDisposisi = new javax.swing.JCheckBox();
        cb_MenandaiDisposisiSelesai = new javax.swing.JCheckBox();
        cmb_role = new javax.swing.JComboBox<>();
        lbl_roleBaru = new javax.swing.JLabel();
        txt_roleBaru = new javax.swing.JTextField();
        btn_tambahRole = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setPreferredSize(new java.awt.Dimension(250, 720));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 202, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(0, 204, 204));
        jPanel2.setPreferredSize(new java.awt.Dimension(1455, 60));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Akses Akun");

        jLabel2.setText("Role");

        lbl_akses.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbl_akses.setText("Akses");

        cb_MengisiFormSuratKeluar.setText("Mengisi Form Surat Keluar");
        cb_MengisiFormSuratKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_MengisiFormSuratKeluarActionPerformed(evt);
            }
        });

        cb_MengirimFormSuratKeluar.setText("Mengirim Form Surat Keluar");

        cb_ValidasiSurat.setText("Validasi Surat");

        cb_MelihatDaftarSuratKeluar.setText("Melihat Daftar Surat Keluar");

        cb_MencetakSuratKeluar.setText("Mencetak Surat Keluar");

        cb_MenghapusSuratKeluar.setText("Menghapus Surat Keluar");

        cb_MengisiFormSuratMasuk.setText("Mengisi Form Surat Masuk");

        cb_MengirimFormSuratMasuk.setText("Mengirim Form Surat Masuk");

        cb_MelihatDaftarSuratMasuk.setText("Melihat Daftar Surat Masuk");

        tabel_data.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tabel_data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID Role", "Role", "Akses"
            }
        ));
        jScrollPane1.setViewportView(tabel_data);

        cb_MembalasSuratMasuk.setText("Membalas Surat Masuk");

        cb_MendisposisiSuratMasuk.setText("Mendisposisi Surat Masuk");

        cb_ManajemenData.setText("Manajemen Data");

        jLabel4.setText("ID");

        txt_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idActionPerformed(evt);
            }
        });

        btn_tambah.setText("Simpan");
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        btn_cari.setText("Cari");

        btn_hapus.setText("Hapus");
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        cb_MelihatDaftarDisposisi.setText("Melihat Daftar Disposisi");
        cb_MelihatDaftarDisposisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_MelihatDaftarDisposisiActionPerformed(evt);
            }
        });

        cb_MengeditDisposisi.setText("Mengedit Disposisi");

        cb_MenghapusDisposisi.setText("Menghapus Disposisi");

        cb_MenandaiDisposisiSelesai.setText("Menandai Disposisi Selesai");

        cmb_role.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_roleActionPerformed(evt);
            }
        });

        lbl_roleBaru.setText("Role Baru");

        btn_tambahRole.setText("Tambah Role");
        btn_tambahRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahRoleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_hapus))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmb_role, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(46, 46, 46)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cb_MengirimFormSuratMasuk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cb_MengisiFormSuratMasuk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cb_MenghapusSuratKeluar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cb_MencetakSuratKeluar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cb_MelihatDaftarSuratKeluar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cb_ValidasiSurat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cb_MengirimFormSuratKeluar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cb_MengisiFormSuratKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cb_MembalasSuratMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cb_MendisposisiSuratMasuk, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cb_MelihatDaftarDisposisi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cb_MengeditDisposisi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cb_MenghapusDisposisi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(cb_MelihatDaftarSuratMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cb_MenandaiDisposisiSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cb_ManajemenData, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lbl_akses, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lbl_roleBaru, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_roleBaru, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(btn_tambahRole, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmb_role, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_roleBaru)
                            .addComponent(txt_roleBaru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_tambahRole))
                        .addGap(22, 22, 22)
                        .addComponent(lbl_akses)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_MengisiFormSuratKeluar)
                            .addComponent(cb_MelihatDaftarSuratMasuk))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cb_MengirimFormSuratKeluar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cb_ValidasiSurat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cb_MelihatDaftarSuratKeluar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cb_MencetakSuratKeluar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cb_MenghapusSuratKeluar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cb_MengisiFormSuratMasuk)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cb_MengirimFormSuratMasuk))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cb_MembalasSuratMasuk)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cb_MendisposisiSuratMasuk)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cb_MengeditDisposisi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cb_MelihatDaftarDisposisi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cb_MenghapusDisposisi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cb_MenandaiDisposisiSelesai)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cb_ManajemenData)))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_tambah)
                            .addComponent(btn_hapus)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(128, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1135, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_idActionPerformed

    private void cb_MengisiFormSuratKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_MengisiFormSuratKeluarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_MengisiFormSuratKeluarActionPerformed

    private void cb_MelihatDaftarDisposisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_MelihatDaftarDisposisiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_MelihatDaftarDisposisiActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
        String selectedRole = cmb_role.getSelectedItem().toString();

        try {
            if (selectedRole.equals("Baru")) {
                // Menampilkan komponen untuk input role baru
                lbl_roleBaru.setVisible(true);
                txt_roleBaru.setVisible(true);
                txt_id.setText(""); // Kosongkan ID karena role baru belum ada
                btn_tambahRole.setVisible(true);

                // Sembunyikan semua checkbox
                hideCheckboxes();

                // Nonaktifkan tombol aksi lainnya
                nonAktifButton();

            } else {
                // Jika role sudah ada, tambahkan atau hapus akses berdasarkan checkbox yang dicentang
                Integer roleId = roleMap.get(selectedRole);
                if (roleId != null) {
                    // Dapatkan akses yang sudah ada di database
                    Set<String> existingAccess = getExistingAccessNames(roleId);

                    // Loop melalui semua checkbox untuk membandingkan dengan akses yang ada
                    JCheckBox[] checkboxes = {
                        cb_ManajemenData, cb_MelihatDaftarDisposisi, cb_MelihatDaftarSuratKeluar,
                        cb_MelihatDaftarSuratMasuk, cb_MembalasSuratMasuk, cb_MenandaiDisposisiSelesai,
                        cb_MencetakSuratKeluar, cb_MendisposisiSuratMasuk, cb_MengeditDisposisi,
                        cb_MenghapusDisposisi, cb_MenghapusSuratKeluar, cb_MengirimFormSuratKeluar,
                        cb_MengirimFormSuratMasuk, cb_MengisiFormSuratKeluar, cb_MengisiFormSuratMasuk,
                        cb_ValidasiSurat
                    };

                    for (JCheckBox cb : checkboxes) {
                        String accessName = cb.getText();
                        int accessId = getExistingAccessId(accessName);

                        if (cb.isSelected()) {
                            // Tambahkan akses baru jika belum ada
                            if (!existingAccess.contains(accessName)) {
                                String insertSql = "INSERT INTO role_access (role_id, access_id) VALUES (?, ?)";
                                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                                insertStmt.setInt(1, roleId);
                                insertStmt.setInt(2, accessId);
                                insertStmt.executeUpdate();
                                insertStmt.close();
                            }
                        } else {
                            // Hapus akses lama jika tidak dicentang
                            if (existingAccess.contains(accessName)) {
                                String deleteSql = "DELETE FROM role_access WHERE role_id = ? AND access_id = ?";
                                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                                deleteStmt.setInt(1, roleId);
                                deleteStmt.setInt(2, accessId);
                                deleteStmt.executeUpdate();
                                deleteStmt.close();
                            }
                        }
                    }

                    JOptionPane.showMessageDialog(this, "Akses berhasil diperbarui untuk role: " + selectedRole);
                    loadRoleAccess(roleId); // Muat ulang akses untuk role
                    getData(); // Perbarui tabel data
                } else {
                    JOptionPane.showMessageDialog(this, "Role tidak ditemukan di database.");
                }
            }

        } catch (Exception e) {
            Logger.getLogger(User_access.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
        }


    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_tambahRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahRoleActionPerformed
        String roleBaru = txt_roleBaru.getText().trim();

        if (!roleBaru.isEmpty()) {
            try {
                // Tambahkan role baru ke database
                String sqlInsert = "INSERT INTO role (role_name) VALUES (?)";
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, roleBaru);
                ps.executeUpdate();

                // Ambil ID role baru yang baru ditambahkan
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int roleId = generatedKeys.getInt(1); // ID role baru

                    // Menambahkan role ke roleMap dan combobox
                    roleMap.put(roleBaru, roleId);
                    cmb_role.addItem(roleBaru);
                    cmb_role.setSelectedItem(roleBaru);
                    txt_id.setText(String.valueOf(roleId));

                    // Sembunyikan input role baru dan tampilkan checkbox
                    lbl_roleBaru.setVisible(false);
                    txt_roleBaru.setVisible(false);
                    btn_tambahRole.setVisible(false);

                    // Tampilkan checkbox untuk akses yang relevan
                    showCheckboxes();

                    // Tampilkan tombol aksi untuk melanjutkan pengaturan akses
                    aktifButton();

                    JOptionPane.showMessageDialog(this, "Role baru berhasil ditambahkan dengan ID: " + roleId);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambahkan role baru.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menambahkan role baru: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nama role baru tidak boleh kosong.");
        }
    }//GEN-LAST:event_btn_tambahRoleActionPerformed

    private void cmb_roleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_roleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmb_roleActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        String selectedRole = cmb_role.getSelectedItem() != null ? cmb_role.getSelectedItem().toString() : "";

        if (selectedRole.equals("") || selectedRole.equals("Pilih Role") || selectedRole.equals("Baru")) {
            JOptionPane.showMessageDialog(this, "Pilih role yang valid untuk dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus role \"" + selectedRole + "\" beserta semua aksesnya?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Dapatkan ID role dari roleMap
                Integer roleId = roleMap.get(selectedRole);
                if (roleId != null) {
                    // Hapus semua akses terkait role dari tabel role_access
                    String deleteAccessSql = "DELETE FROM role_access WHERE role_id = ?";
                    PreparedStatement deleteAccessStmt = conn.prepareStatement(deleteAccessSql);
                    deleteAccessStmt.setInt(1, roleId);
                    deleteAccessStmt.executeUpdate();
                    deleteAccessStmt.close();

                    // Hapus role dari tabel role
                    String deleteRoleSql = "DELETE FROM role WHERE id_role = ?";
                    PreparedStatement deleteRoleStmt = conn.prepareStatement(deleteRoleSql);
                    deleteRoleStmt.setInt(1, roleId);
                    deleteRoleStmt.executeUpdate();
                    deleteRoleStmt.close();

                    JOptionPane.showMessageDialog(this, "Role \"" + selectedRole + "\" dan semua akses terkait berhasil dihapus.");

                    // Perbarui GUI
                    roleMap.remove(selectedRole);
                    cmb_role.removeItem(selectedRole);
                    resetCheckboxes();
                    getData();
                } else {
                    JOptionPane.showMessageDialog(this, "Role tidak ditemukan di database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                Logger.getLogger(User_access.class.getName()).log(Level.SEVERE, null, e);
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus role: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_hapusActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(User_access.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(User_access.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(User_access.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(User_access.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new User_access().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cari;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_tambahRole;
    private javax.swing.JCheckBox cb_ManajemenData;
    private javax.swing.JCheckBox cb_MelihatDaftarDisposisi;
    private javax.swing.JCheckBox cb_MelihatDaftarSuratKeluar;
    private javax.swing.JCheckBox cb_MelihatDaftarSuratMasuk;
    private javax.swing.JCheckBox cb_MembalasSuratMasuk;
    private javax.swing.JCheckBox cb_MenandaiDisposisiSelesai;
    private javax.swing.JCheckBox cb_MencetakSuratKeluar;
    private javax.swing.JCheckBox cb_MendisposisiSuratMasuk;
    private javax.swing.JCheckBox cb_MengeditDisposisi;
    private javax.swing.JCheckBox cb_MenghapusDisposisi;
    private javax.swing.JCheckBox cb_MenghapusSuratKeluar;
    private javax.swing.JCheckBox cb_MengirimFormSuratKeluar;
    private javax.swing.JCheckBox cb_MengirimFormSuratMasuk;
    private javax.swing.JCheckBox cb_MengisiFormSuratKeluar;
    private javax.swing.JCheckBox cb_MengisiFormSuratMasuk;
    private javax.swing.JCheckBox cb_ValidasiSurat;
    private javax.swing.JComboBox<String> cmb_role;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_akses;
    private javax.swing.JLabel lbl_roleBaru;
    private javax.swing.JTable tabel_data;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_roleBaru;
    // End of variables declaration//GEN-END:variables

    private void tampilan() {
        lbl_roleBaru.setVisible(false);
        txt_roleBaru.setVisible(false);
        btn_tambahRole.setVisible(false);
    }

}
