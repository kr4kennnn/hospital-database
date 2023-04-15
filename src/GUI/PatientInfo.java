package GUI;

import java.sql.*;
import javax.swing.*;
import java.io.IOException;

public class PatientInfo {
    
    public static void main (String args []) throws SQLException, IOException {
        String url = "jdbc:postgresql://localhost:5432/Proje";
        String user = "postgres";
        String pass = "644232";
        Connection conn = DriverManager.getConnection(url, user,pass);
        
        JFrame frame = new TDFrame(conn);
        frame.setVisible(true);
    }
}