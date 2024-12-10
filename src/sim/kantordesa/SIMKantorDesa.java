/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sim.kantordesa;
import java.sql.*;
import sim.kantordesa.auth.login;

/**
 *
 * @author Krisna
 */
public class SIMKantorDesa {

    static Connection conn;

    public static Connection getKoneksi() {
        try {
            String URL = "jdbc:mysql://localhost:3306/simkantordesa";
            String USER = "root";
            String PASSWORD = "root";
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            System.out.println("Terjadi Error");
        }
        return conn;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        getKoneksi();
        login LoginFrame = new login();
        LoginFrame.setVisible(true);
        LoginFrame.pack();
        LoginFrame.setLocationRelativeTo(null);
    }
    
}
