package kutuphane;

import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class fncClass {

    anaForm ana = new anaForm();

    public void fncKitapEkle() {
//        String kitapTuru = ana.cbKitapTuru.getSelectedItem().toString();
//        String kitapAdi = ana.txtKitapAdi.getText();
//        String isbn = ana.txtIsbnNo.getText();
//        String yazar = ana.cbYazarAdi.getSelectedItem().toString();
//        String dil = ana.cbKitapDili.getSelectedItem().toString();
//        String yayinevi = ana.cbYayinevi.getSelectedItem().toString();
//        String kitaplikNo = ana.txtKitaplikNo.getText();
//        String rafNo = ana.txtRafNo.getText();
//        String siraNo = ana.txtSiraNo.getText();
//        String ozet = ana.txtOzet.getText();
/*
        try {
            ResultSet rsKitapTuru = new DBConnect().baglan().executeQuery("select DISTINCT gid from genre where genre='" + kitapTuru + "'");
            while (rsKitapTuru.next()) {

                kitapTuru = rsKitapTuru.getString("gid");
            }
        } catch (Exception ex) {

        }
        try {
            ResultSet rsDil = new DBConnect().baglan().executeQuery("select DISTINCT lid from language where language='" + dil + "'");
            while (rsDil.next()) {
                dil = rsDil.getString("lid");
            }
        } catch (SQLException ex) {

        }
        try {
            ResultSet rsYazar = new DBConnect().baglan().executeQuery("select DISTINCT aid from author where aname='" + yazarAd + "' and asurname='" + yazarSoyad + "'");
            while (rsYazar.next()) {
                yazar = rsYazar.getString("aid");
            }
        } catch (SQLException ex) {

        }
        try {
            ResultSet rsYayievi = new DBConnect().baglan().executeQuery("select DISTINCT pid from publisher where publisher='" + yayinevi + "'");
            while (rsYayievi.next()) {
                yayinevi = rsYayievi.getString("pid");
            }
        } catch (SQLException ex) {

        }
 */
      //  String sorgu = "insert into book values(null, '" + kitapTuru + "','" + kitapAdi + "','" + isbn + "', '" + yazar + "' , '" + dil + "', '" + yayinevi + "' , '" + kitaplikNo + "', '" + rafNo + "', '" + siraNo + "', '" + ozet + "', '" + siraNo + "')";
//        DBConnect db = new DBConnect();
//        try {
//            int sonuc = db.baglan().executeUpdate(sorgu); // bu kod insert delete ve update komutlarında kullanılır.
//            if (sonuc > 0) {
//                JOptionPane.showMessageDialog(ana, "Ekleme işlemi başarılı.");
//            }
//            // ana.dataGetir();
//        } catch (SQLException ex) {
//            System.err.println("Yazma Hatası : " + ex);
//        }
    }

}
