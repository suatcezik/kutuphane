package kutuphane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnect {

    String driver = "org.sqlite.JDBC";
    String url = "jdbc:sqlite:db//library.db";

    Connection conn = null;
    Statement st = null;
 
    public Statement baglan() {
        try {
            // class.forname yolunu verdiğimiz kütüphaneyi çalıştırır.
            Class.forName(driver); // indirdiğimiz kütüphaneyi tetikledik.
            conn = DriverManager.getConnection(url);
            st = conn.createStatement();
            System.out.println("DB bağlantısı başarılı.");
           
        } catch (Exception e) {
            System.err.println("Bağlantı hatası:" + e);
        }
        return st;
    }
}
