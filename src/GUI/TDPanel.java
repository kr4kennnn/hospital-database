package GUI;

import java.sql.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

class TDPanel extends JPanel {

    private final JLabel inputLbl = new JLabel("OdaNo/TcNo/Durum: ");
    private final JTextField txt1 = new JTextField(11);
    private final JButton btn1 = new JButton("OdaNo");
    private final JButton btn2 = new JButton("TcNo");
    private final JButton btn3 = new JButton("Durum");
    private final JButton btn4 = new JButton("View");
    private final JButton btn5 = new JButton("Hasta Ekle");
    private final JButton btn6 = new JButton("Hasta Güncelle");
    private final JButton btn7 = new JButton("Hasta Sil");
    private final JButton btn8 = new JButton("Query1");
    private final JButton btn9 = new JButton("Query2");
    private final JButton btn10 = new JButton("Query3");
    private final JButton btn11 = new JButton("Trigger1");
    private final JButton btn12 = new JButton("Sequence");    
    private final JButton btn0 = new JButton("Kapat");
    private final JLabel outputLbl = new JLabel(" ");
    private final DefaultTableModel hastalar;
    private Connection conne;

    public TDPanel(Connection conn) {

        Object[] columnNames = {"TCNO", "AD", "SOYAD", "CİNSİYET", "DTARİHİ"};
        hastalar = new DefaultTableModel(columnNames, 0);
        JTable tbl = new JTable(hastalar);
        JScrollPane sp = new JScrollPane(tbl);
        add(inputLbl);
        add(txt1);
        add(btn1);
        add(btn2);
        add(btn3);
        add(btn4);
        add(btn5);
        add(btn6);
        add(btn7);
        add(btn8);
        add(btn9);
        add(btn10);
        add(btn11);
        add(btn12);
        add(btn0);
        add(outputLbl);
        add(sp);

        conne = conn;

        btn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                goster1();
            }
        });

        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                goster2();
            }
        });

        btn3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                goster3();
            }
        });

        btn4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                goster4();
            }
        });

        btn5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ekle1();
            }
        });

        btn6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                guncelle();
            }
        });

        btn7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                sil();
            }
        });
        
        btn8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                query1();
            }
        });
        
        btn9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                query2();
            }
        });
        
        btn10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                goster5();
            }
        });

        btn11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                trigger();
            }
        });
        
        btn12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ekle2();
            }
        });
        
        btn0.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    conne.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                setVisible(false);
                System.exit(0);
            }
        });
    }
    
    private void goster1(){
        int odano = Integer.parseInt(txt1.getText());
        try {
            PreparedStatement pstmt = conne.prepareStatement("select * from odadurumu(?)");
            pstmt.setInt(1, odano);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                SQLWarning warning = pstmt.getWarnings();
                JOptionPane.showInternalMessageDialog(null, warning.getMessage());    
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void goster2() {
        int tcno = Integer.parseInt(txt1.getText());
        try {
            PreparedStatement pstmt = conne.prepareStatement("select * from fonk(?)");
            pstmt.setInt(1, tcno);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                String varyant = rs.getString(1);
                String odaNo = rs.getString(2);
                JOptionPane.showInternalMessageDialog(null, "Varyant: " + varyant + " Oda no: " + odaNo);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void goster3() {
        String durum = txt1.getText();
        String label = "<html>";
        try {
            PreparedStatement pstmt = conne.prepareStatement("select * from hastalikdurumu(?)");
            pstmt.setString(1, durum);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                SQLWarning warning = pstmt.getWarnings();
                while (warning != null) {
                    label = label + "<br/>" + warning.getMessage();
                    warning = warning.getNextWarning();
                }
                label = label + "</html>";
                outputLbl.setText(label);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void goster4() {
        try {
            String query2 = "Select * FROM yasaraligi ORDER BY dogumtarihi";
            PreparedStatement p2 = conne.prepareStatement(query2);
            ResultSet r2 = p2.executeQuery();
            while (r2.next()) {
                String fname = r2.getString(1);
                String lname = r2.getString(2);
                String dogumtarihi = r2.getString(3);
                Object[] satir = {fname, lname, dogumtarihi};
                hastalar.addRow(satir);
            }
            p2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void goster5() {
        int tcno = Integer.parseInt(txt1.getText());
        String query = "SELECT isim, soyisim FROM hasta WHERE tcno = " + tcno;
        PreparedStatement p;

        try {
            p = conne.prepareStatement(query);
            ResultSet r;
            r = p.executeQuery();
            hastalar.setRowCount(0);

            if (!r.next()) {
                outputLbl.setText("Böyle bir hasta bulunmamaktadır! ");
            } else {
                String y_fname = r.getString(1);
                String y_lname = r.getString(2);
                p.close();

                String query2 = "SELECT * FROM hasta WHERE tcno = " + tcno;
                PreparedStatement p2 = conne.prepareStatement(query2);
                ResultSet r2 = p2.executeQuery();

                while (r2.next()) {
                    String tcnum = r2.getString(1);
                    String fname = r2.getString(2);
                    String lname = r2.getString(3);
                    String cinsiyet = r2.getString(4);
                    String dogumtarihi = r2.getString(5);

                    Object[] satir = {tcnum, fname, lname, cinsiyet, dogumtarihi};
                    hastalar.addRow(satir);
                }
                p2.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ekle1() {
        JTextField textField1 = new JTextField();
        JTextField textField2 = new JTextField();
        JTextField textField3 = new JTextField();
        JTextField textField4 = new JTextField();
        JTextField textField5 = new JTextField();

        Object[] inputFields = {"tcno", textField1,
                                "isim", textField2,
                                "soyisim", textField3,
                                "cinsiyet(E/K)", textField4,
                                "doğum tarihi (YYYY/AA/GG)", textField5};
        int option = JOptionPane.showConfirmDialog(this, inputFields, "Hasta Bilgileri", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String tcno = textField1.getText();
            String isim = textField2.getText();
            String soyisim = textField3.getText();
            String cinsiyet = textField4.getText();
            String dtarihi = textField5.getText();

            String query = "INSERT INTO hasta (tcno, isim, soyisim, cinsiyet, dogumtarihi) "
                    + "VALUES(" + tcno  + ",'" + isim + "', '" + soyisim + "', '" + cinsiyet + "', '" + dtarihi + "')";

            System.out.println(query);

            Statement s = null;
            try {
                s = conne.createStatement();
                s.executeUpdate(query);
                conne.setAutoCommit(false);
                conne.commit();
                s.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void ekle2(){
        JTextField textField1 = new JTextField();
        JTextField textField2 = new JTextField();
        JTextField textField3 = new JTextField();

        Object[] inputFields = {"tcno", textField1,
                                "giriş tarihi", textField2,
                                "varyant", textField3};
        int option = JOptionPane.showConfirmDialog(this, inputFields, "Hasta Kayıt Bilgileri", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String tcno = textField1.getText();
            String giris_tarihi = textField2.getText();
            String varyant = textField3.getText();

            String query = "INSERT INTO kayit (tc_no, oda_no, giris_tarihi, varyant) "
                    + "VALUES(" + tcno  + ", nextval('seq1'), '" + giris_tarihi + "', '" + varyant + "')";

            System.out.println(query);

            Statement s = null;
            try {
                s = conne.createStatement();
                s.executeUpdate(query);
                conne.setAutoCommit(false);
                conne.commit();
                s.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void guncelle() {
        String tcnoOld = JOptionPane.showInputDialog("Hasta tcsi giriniz");
        JTextField textField1 = new JTextField();
        JTextField textField2 = new JTextField();
        JTextField textField3 = new JTextField();
        JTextField textField4 = new JTextField();
        JTextField textField5 = new JTextField();

        Object[] inputFields = {"tcno", textField1,
            "isim", textField2,
            "soyisim", textField3,
            "cinsiyet (E/K)", textField4,
            "dtarihi (YYYY/AA/GG formatında)", textField5};
        int option = JOptionPane.showConfirmDialog(this, inputFields, "Hasta Bilgileri", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String tcnoNew = textField1.getText();
            String isim = textField2.getText();
            String soyisim = textField3.getText();
            String cinsiyet = textField4.getText();
            String dtarihi = textField5.getText();

            String query = "UPDATE hasta SET tcno =" + tcnoNew
                    + ",isim =" + "'" + isim + "'"
                    + ",soyisim =" + "'" + soyisim + "'"
                    + ",cinsiyet =" + "'" + cinsiyet + "'"
                    + ",dogumtarihi =" + "'" + dtarihi + "'"
                    + "WHERE tcno =" + tcnoOld;
            System.out.println(query);

            Statement s = null;
            try {
                s = conne.createStatement();
                s.executeUpdate(query);
                conne.setAutoCommit(false);
                conne.commit();
                s.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void sil() {
        String tcno = JOptionPane.showInputDialog("Hasta tcsi giriniz");
        String query = "DELETE FROM hasta WHERE tcno =" + tcno;
        System.out.println(query);
        Statement s = null;
        try {
            s = conne.createStatement();
            s.executeUpdate(query);
            conne.setAutoCommit(false);
            conne.commit();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void query1(){
        try {
            String query1 = "SELECT tc_No, oda_No " +
                            "FROM kayit " +
                            "WHERE varyant = 'ALFA' " +
                            "EXCEPT " +
                            "SELECT tc_No, oda_No " +
                            "FROM kayit " +
                            "WHERE oda_No BETWEEN 1 AND 8";
            PreparedStatement p2 = conne.prepareStatement(query1);
            ResultSet r2 = p2.executeQuery();
            while (r2.next()) {
                String tcno = r2.getString(1);
                String oda_No = r2.getString(2);
                JOptionPane.showInternalMessageDialog(null, "TC: " + tcno + " Oda NO: " + oda_No);
            }
            p2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void query2(){
        String label = "<html>";
        try {
            String query2 = "SELECT varyant , count(*) " +
                            "FROM kayit " +
                            "GROUP BY varyant " + 
                            "HAVING count(*)>=2";
            PreparedStatement p2 = conne.prepareStatement(query2);
            ResultSet r2 = p2.executeQuery();
            while (r2.next()) {
                String varyant = r2.getString(1);
                String count = r2.getString(2);
                label = label + "<br/>" + "Varyant: " + varyant + "  Sayı: " + count;
            }
            label = label + "</html>";
            outputLbl.setText(label);
            p2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void trigger(){
        JTextField textField1 = new JTextField();
        JTextField textField2 = new JTextField();
        JTextField textField3 = new JTextField();
        JTextField textField4 = new JTextField();
        Object[] inputFields = {"tcno", textField1,
                                "oda no", textField2,
                                "giris_tarihi", textField3,
                                "varyant", textField4};
        int option = JOptionPane.showConfirmDialog(this, inputFields, "Kayıt Bilgileri", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        
        if (option == JOptionPane.OK_OPTION) {
            String tcno = textField1.getText();            
            String odano = textField2.getText();
            String girisTarihi = textField3.getText();
            String covidVaryanti = textField4.getText();
            
            String query = "INSERT INTO kayit (tc_no, oda_no, giris_tarihi, varyant) "
                    + "VALUES(" + tcno + "," + odano + ", '" + girisTarihi + "' ,'" + covidVaryanti + "')";

            System.out.println(query);

            Statement s = null;
            try {
                s = conne.createStatement();
                s.executeUpdate(query);
                conne.setAutoCommit(false);
                conne.commit();
                s.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
