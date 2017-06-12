package kutuphane;

import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class anaForm extends javax.swing.JFrame {

    //Global Değişkenler
    int sonuc = 0;
    String sorgu;
    String tabloAdi;
    String sutunAdi;
    String sutunId;
    String secilenId;
    int id = 0;
    int[] selection = null;
    ArrayList<String> bos = new ArrayList();
    String dbGelen = null;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    String date = dtf.format(now);

    public anaForm() {
        initComponents();
        chbKitapAdi.setSelected(true);
        cagir();

    }

    public void cagir() {
        String[] gelenTur = {"kitap", "yazar", "dil", "yevi", "tur"};
        for (String item : gelenTur) {
            dataGetir(item);
        }
       
    }

    public void fncGetDate() {

    }

    public void dataGetir(String gelenTabloTuru) {

        DefaultTableModel dm = new DefaultTableModel();
        try {
            if (gelenTabloTuru.equals("tur")) {
                cbKitapTuru.removeAllItems();
                dm.addColumn("id");
                dm.addColumn("Tür");
                ResultSet rs = new DBConnect().baglan().executeQuery("select * from genre ORDER BY genre ASC");
                while (rs.next()) {
                    dm.addRow(new String[]{rs.getString("gid"), rs.getString("genre")});
                    cbKitapTuru.addItem(rs.getString("genre"));
                }
                tblTur.setModel(dm);
                dataGetir("kitap");
            }
            if (gelenTabloTuru.equals("dil")) {
                cbKitapDili.removeAllItems();
                dm.addColumn("id");
                dm.addColumn("Dil");

                ResultSet rs = new DBConnect().baglan().executeQuery("select * from language ORDER BY language ASC");
                while (rs.next()) {
                    dm.addRow(new String[]{rs.getString("lid"), rs.getString("language")});
                    cbKitapDili.addItem(rs.getString("language"));
                }
                tblDil.setModel(dm);
                dataGetir("kitap");
            }
            if (gelenTabloTuru.equals("yazar")) {
                cbYazarAdi.removeAllItems();
                dm.addColumn("id");
                dm.addColumn("Yazar Adı - Soyadı");
                ResultSet rs = new DBConnect().baglan().executeQuery("select * from author ORDER BY author ASC");
                while (rs.next()) {
                    dm.addRow(new String[]{rs.getString("aid"), rs.getString("author")});
                    cbYazarAdi.addItem(rs.getString("author"));
                }
                tblYazar.setModel(dm);
                dataGetir("kitap");
            }
            if (gelenTabloTuru.equals("yevi")) {
                cbYayinevi.removeAllItems();
                dm.addColumn("id");
                dm.addColumn("Yayınevi");
                ResultSet rs = new DBConnect().baglan().executeQuery("select * from publisher ORDER BY publisher ASC");// bu sadece select için kullanılır.
                while (rs.next()) {
                    dm.addRow(new String[]{rs.getString("pid"), rs.getString("publisher")});
                    cbYayinevi.addItem(rs.getString("publisher"));
                }
                tblYayinEvi.setModel(dm);
                dataGetir("kitap");
            }
            if (gelenTabloTuru.equals("kitap")) {
                dm.addColumn("id");
                dm.addColumn("Kitap Türü");
                dm.addColumn("Kitap Adı");
                dm.addColumn("ISBN");
                dm.addColumn("Yazar Adı - Soyadı");
                dm.addColumn("Kitap Dili");
                dm.addColumn("Yayınevi");
                dm.addColumn("Kitaplık No");
                dm.addColumn("Raf No");
                dm.addColumn("Sıra No");
                dm.addColumn("Özet");
                dm.addColumn("Kayıt Tarihi");

                ResultSet rs = new DBConnect().baglan().executeQuery("select * from book ORDER BY bid DESC");
                while (rs.next()) {
                    dm.addRow(new String[]{
                        rs.getString("bid"),
                        fncIdtoColumnName("genre", "gid", rs.getString("bgenreno")),
                        rs.getString("bname"),
                        rs.getString("isbn"),
                        fncIdtoColumnName("author", "aid", rs.getString("bauthorno")),
                        fncIdtoColumnName("language", "lid", rs.getString("blanguageno")),
                        fncIdtoColumnName("publisher", "pid", rs.getString("bpublisherno")),
                        rs.getString("bcase"),
                        rs.getString("shelf"),
                        rs.getString("rowno"),
                        rs.getString("abstract"),
                        rs.getString("date")
                    });
                }
                tblKitap.setModel(dm);
                tblSorgula.setModel(dm);
            }
        } catch (SQLException e) {
            System.err.println("Data getirme hatası : " + e);
        }
    }

    public String fncIdtoColumnName(String gelenTablo, String gelenId, String dbgelenId) {
        dbGelen = null;
        try {
            ResultSet rs = new DBConnect().baglan().executeQuery("select " + gelenTablo + " from " + gelenTablo + " where " + gelenId + " = '" + dbgelenId + "'");
            while (rs.next()) {
                dbGelen = rs.getString(gelenTablo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(anaForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dbGelen;
    }

    public void fncKitapSorgu(String gelenIslem) {
        DBConnect db = new DBConnect();
        switch (gelenIslem) {
            case "insert":
                sorgu = "insert into book values(null, '" + turId + "', '" + kitapAdi + "', '" + isbn + "', '" + yazarId + "', '" + dilId + "', '" + yeviId + "', '" + kitaplikNo + "', '" + rafNo + "', '" + siraNo + "', '" + ozet + "', '" + date + "')";
                break;
            case "update":
                fncDegerAtama();
                sorgu = "update book set bgenreno = '" + turId + "', bname = '" + kitapAdi + "', isbn = '" + isbn + "', bauthorno='" + yazarId + "', blanguageno='" + dilId + "', bpublisherno='" + yeviId + "', bcase='" + kitaplikNo + "', shelf='" + rafNo + "', rowno='" + siraNo + "', abstract='" + ozet + "', date='" + date + "' where bid = '" + id + "'";
                System.out.println("Sorgu : " + sorgu);
                break;
            case "delete":
                for (String item : cokluIdDelete) {
                    sorgu = "delete from book where bid = '" + item + "'";
                    try {
                        sonuc = db.baglan().executeUpdate(sorgu); // bu kod insert delete ve update komutlarında kullanılır.
                    } catch (SQLException ex) {
                        Logger.getLogger(anaForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
        }

        try {
            if (!gelenIslem.equals("delete")) {
                sonuc = db.baglan().executeUpdate(sorgu);
            }
            if (sonuc > 0) {
                JOptionPane.showMessageDialog(this, "İşlem başarıyla gerçekleştirildi.");
                dataGetir("kitap");
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, "HATA! Tekrar deneyiniz.");
        }
    }

    public void fncDegerAtama() {
        tur = String.valueOf(cbKitapTuru.getSelectedItem());
        kitapAdi = txtKitapAdi.getText();
        isbn = txtIsbnNo.getText();
        yazar = String.valueOf(cbYazarAdi.getSelectedItem());
        dil = String.valueOf(cbKitapDili.getSelectedItem());
        yevi = String.valueOf(cbYayinevi.getSelectedItem());
        kitaplikNo = txtKitaplikNo.getText();
        rafNo = txtRafNo.getText();
        siraNo = txtSiraNo.getText();
        ozet = txtOzet.getText();
    }

    public void fncGlobalDegiskenTemizle() {
        sorgu = null;
        tabloAdi = null;
        sutunAdi = null;
        sutunId = null;
    }

    public void fncEsleme(String gelenTablo) {
        fncGlobalDegiskenTemizle();
        switch (gelenTablo) {
            case "yazar":
                tabloAdi = sutunAdi = "author";
                sutunId = "aid";
                break;
            case "dil":
                tabloAdi = sutunAdi = "language";
                sutunId = "lid";
                break;
            case "yevi":
                tabloAdi = sutunAdi = "publisher";
                sutunId = "pid";
                break;
            case "tur":
                tabloAdi = sutunAdi = "genre";
                sutunId = "gid";
                break;
        }
    }

    public void fncSorgu(String gelenSorgu, String gelenNesne, String gelenTablo, ArrayList<String> itemSil) {
        DBConnect db = new DBConnect();
        switch (gelenSorgu) {
            case "insert":
                sorgu = "insert into " + tabloAdi + " values(null, '" + gelenNesne + "')";
                break;
            case "update":
                sorgu = "update " + tabloAdi + " set " + sutunAdi + " = '" + gelenNesne + "' where " + sutunId + " = '" + id + "'";
                System.out.println("Sorgu : " + sorgu);
                break;
            case "delete":
                for (String item : itemSil) {
                    sorgu = "delete from " + tabloAdi + " where " + sutunId + " = '" + item + "'";
                    try {
                        sonuc = db.baglan().executeUpdate(sorgu); // bu kod insert delete ve update komutlarında kullanılır.
                    } catch (SQLException ex) {
                        Logger.getLogger(anaForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
        }
        try {
            if (!gelenSorgu.equals("delete")) {
                sonuc = db.baglan().executeUpdate(sorgu); // bu kod insert delete ve update komutlarında kullanılır.
            }
            if (sonuc > 0) {
                txtYayinevi.setText("");
                txtYazarAdi.setText("");
                txtLanguage.setText("");
                txtTurAdi.setText("");
                JOptionPane.showMessageDialog(this, "İşlem başarıyla gerçekleştirildi.");
            }
            dataGetir(gelenTablo);
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, "HATA! Tekrar deneyiniz.");
        }
        id = 0;
    }

    public void fncEkle(String gelenTablo, String gelenNesne) {
        fncEsleme(gelenTablo);
        fncSorgu("insert", gelenNesne, gelenTablo, bos);
    }

    //öykünün yazdığı kısım
    //ödev olarak fncDuzenle() methodunu yorum satırı haline getirip tekrar yaz.
    public void fncDuzenle2(String gelenTablo, String gelenNesne) {

        if (id > 0) {
            if (gelenTablo.equals("yevi")) {
                sorgu = "update publisher set publisher = '" + gelenNesne + "' where pid= '" + id + "' ";
            }
            if (gelenTablo.equals("dil")) {
                sorgu = "update language set language = '" + gelenNesne + "'where lid = '" + id + "'";
            }
            if (gelenTablo.equals("author")) {

                sorgu = "update author set author = '" + gelenNesne + "' where aid = '" + id + "'";
            }
            if (gelenTablo.equals("tur")) {
                sorgu = "update genre set genre = '" + gelenNesne + "' where gid = '" + id + "' ";
            }
            DBConnect db = new DBConnect();
            try {
                int sonuc = db.baglan().executeUpdate(sorgu);
                if (sonuc > 0) {
                    JOptionPane.showMessageDialog(this, "başarılı");
                    dataGetir(gelenTablo);
                }
            } catch (SQLException ex) {
                System.out.println("Düzenleme Hatası : " + ex);
                System.out.println("Sorgu : " + sorgu);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen seçim yapınız?");
        }
    }

    public void fncDuzenle(String gelenTablo, String gelenNesne) {
        fncEsleme(gelenTablo);
        if (id > 0) {
            fncSorgu("update", gelenNesne, gelenTablo, bos);
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen seçim yapınız.");
        }
    }

    public void fncSil(String gelenTablo, ArrayList<String> gelenNesne) {
        fncEsleme(gelenTablo);
        if (id > 0) {
            for (String item : gelenNesne) {
                fncSorgu("delete", "", gelenTablo, gelenNesne);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Lütfen seçim yapınız.");
        }
    }

    public void fncKitapAra() {
        DefaultTableModel dm = new DefaultTableModel();
        dm.addColumn("id");
        dm.addColumn("Kitap Türü");
        dm.addColumn("Kitap Adı");
        dm.addColumn("ISBN");
        dm.addColumn("Yazar Adı - Soyadı");
        dm.addColumn("Kitap Dili");
        dm.addColumn("Yayınevi");
        dm.addColumn("Kitaplık No");
        dm.addColumn("Raf No");
        dm.addColumn("Sıra No");
        dm.addColumn("Özet");
        dm.addColumn("Kayıt Tarihi");
        ResultSet rs = null;
        try {
            if (chbKitapAdi.isSelected() && chbYazarAdi.isSelected()) {
                rs = new DBConnect().baglan().executeQuery("select * from book where bname like '%" + txtKitapAra.getText() + "%' or bauthorno in (select aid from author where author like '%" + txtKitapAra.getText() + "%') ORDER BY bid DESC");
            } else if (chbYazarAdi.isSelected()) {
                rs = new DBConnect().baglan().executeQuery("select * from book where bauthorno in (select aid from author where author like '%" + txtKitapAra.getText() + "%') ORDER BY bid DESC");
            } else if ((chbKitapAdi.isSelected() || !chbKitapAdi.isSelected()) && !chbYazarAdi.isSelected()) {
                rs = new DBConnect().baglan().executeQuery("select * from book where bname like '%" + txtKitapAra.getText() + "%' ORDER BY bid DESC" );
            }

            while (rs.next()) {
                dm.addRow(new String[]{
                    rs.getString("bid"),
                    fncIdtoColumnName("genre", "gid", rs.getString("bgenreno")),
                    rs.getString("bname"),
                    rs.getString("isbn"),
                    fncIdtoColumnName("author", "aid", rs.getString("bauthorno")),
                    fncIdtoColumnName("language", "lid", rs.getString("blanguageno")),
                    fncIdtoColumnName("publisher", "pid", rs.getString("bpublisherno")),
                    rs.getString("bcase"),
                    rs.getString("shelf"),
                    rs.getString("rowno"),
                    rs.getString("abstract"),
                    rs.getString("date")
                });
            }
            tblSorgula.removeAll();
            tblSorgula.setModel(dm);
        } catch (SQLException ex) {
            Logger.getLogger(anaForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jtbbTab = new javax.swing.JTabbedPane();
        jpnlSorgula = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        txtKitapAra = new javax.swing.JTextField();
        chbKitapAdi = new javax.swing.JCheckBox();
        chbYazarAdi = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSorgula = new javax.swing.JTable();
        jpnlKitap = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblKitap = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cbKitapTuru = new javax.swing.JComboBox<String>();
        txtKitapAdi = new javax.swing.JTextField();
        txtIsbnNo = new javax.swing.JTextField();
        cbYazarAdi = new javax.swing.JComboBox<String>();
        cbKitapDili = new javax.swing.JComboBox<String>();
        cbYayinevi = new javax.swing.JComboBox<String>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtKitaplikNo = new javax.swing.JTextField();
        txtRafNo = new javax.swing.JTextField();
        txtSiraNo = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtOzet = new javax.swing.JTextArea();
        btnGoTur = new javax.swing.JButton();
        btnGoDil = new javax.swing.JButton();
        btnGoYazar = new javax.swing.JButton();
        btnGoYevi = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnKitapEkle = new javax.swing.JButton();
        btnKitapDuzenle = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        btnKitapSil = new javax.swing.JButton();
        jpnlYazar = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtYazarAdi = new javax.swing.JTextField();
        btnYazarEkle = new javax.swing.JButton();
        btnYazarDuzenle = new javax.swing.JButton();
        btnYazarSil = new javax.swing.JButton();
        btnYazarTemizle = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblYazar = new javax.swing.JTable();
        jpnlTur = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txtTurAdi = new javax.swing.JTextField();
        btnTurEkle = new javax.swing.JButton();
        btnTurDuzenle = new javax.swing.JButton();
        btnTurSil = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblTur = new javax.swing.JTable();
        jpnlDil = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        txtLanguage = new javax.swing.JTextField();
        btnDilEkle = new javax.swing.JButton();
        btnDilDuzenle = new javax.swing.JButton();
        btnDilSil = new javax.swing.JButton();
        btnLanguageTemizle = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblDil = new javax.swing.JTable();
        jpnlYayinevi = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtYayinevi = new javax.swing.JTextField();
        btnYayineviEkle = new javax.swing.JButton();
        btnYayineviDuzenle = new javax.swing.JButton();
        btnYayineviTemizle = new javax.swing.JButton();
        btnYayineviSil = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblYayinEvi = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnLogOut = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Arama"));

        txtKitapAra.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtKitapAraInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        txtKitapAra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKitapAraActionPerformed(evt);
            }
        });
        txtKitapAra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKitapAraKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKitapAraKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKitapAraKeyReleased(evt);
            }
        });

        chbKitapAdi.setText("Kitap Adı");
        chbKitapAdi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chbKitapAdiMouseClicked(evt);
            }
        });

        chbYazarAdi.setText("Yazar Adı");
        chbYazarAdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbYazarAdiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtKitapAra)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(chbKitapAdi)
                        .addGap(18, 18, 18)
                        .addComponent(chbYazarAdi)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtKitapAra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chbKitapAdi)
                    .addComponent(chbYazarAdi))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        tblSorgula.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8"
            }
        ));
        jScrollPane1.setViewportView(tblSorgula);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jpnlSorgulaLayout = new javax.swing.GroupLayout(jpnlSorgula);
        jpnlSorgula.setLayout(jpnlSorgulaLayout);
        jpnlSorgulaLayout.setHorizontalGroup(
            jpnlSorgulaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlSorgulaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlSorgulaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpnlSorgulaLayout.setVerticalGroup(
            jpnlSorgulaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlSorgulaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jtbbTab.addTab("Kitap Sorgula", jpnlSorgula);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Kitaplar"));

        tblKitap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblKitap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKitapMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblKitap);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Kitap Ekle"));

        jLabel2.setText("Kitap Türü");

        jLabel3.setText("Kitap Adi");

        jLabel4.setText("Isbn No");

        jLabel5.setText("Yazar Adi");

        jLabel6.setText("Kitap Dili");

        jLabel7.setText("Yayınevi");

        cbKitapTuru.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbKitapTuruItemStateChanged(evt);
            }
        });
        cbKitapTuru.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                cbKitapTuruInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        cbKitapTuru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbKitapTuruActionPerformed(evt);
            }
        });

        txtKitapAdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKitapAdiActionPerformed(evt);
            }
        });
        txtKitapAdi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKitapAdiKeyPressed(evt);
            }
        });

        cbYazarAdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbYazarAdiActionPerformed(evt);
            }
        });

        cbKitapDili.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbKitapDiliActionPerformed(evt);
            }
        });

        cbYayinevi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbYayineviActionPerformed(evt);
            }
        });

        jLabel8.setText("Kitaplık No");

        jLabel9.setText("Raf No");

        jLabel10.setText("Sıra No");

        jLabel11.setText("Özet");

        txtOzet.setColumns(20);
        txtOzet.setRows(5);
        jScrollPane2.setViewportView(txtOzet);

        btnGoTur.setText("+");
        btnGoTur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoTurActionPerformed(evt);
            }
        });

        btnGoDil.setText("+");
        btnGoDil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoDilActionPerformed(evt);
            }
        });

        btnGoYazar.setText("+");
        btnGoYazar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoYazarActionPerformed(evt);
            }
        });

        btnGoYevi.setText("+");
        btnGoYevi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoYeviActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIsbnNo)
                    .addComponent(txtKitapAdi)
                    .addComponent(cbKitapTuru, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbYazarAdi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbKitapDili, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbYayinevi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGoTur, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGoYazar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGoDil, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGoYevi, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSiraNo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                            .addComponent(txtRafNo, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtKitaplikNo, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGoTur, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(cbKitapTuru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKitaplikNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRafNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtKitapAdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSiraNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtIsbnNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbYazarAdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(btnGoYazar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbKitapDili, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(btnGoDil, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbYayinevi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(btnGoYevi, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addComponent(jScrollPane2))
                .addGap(40, 40, 40))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnKitapEkle.setText("Kaydet");
        btnKitapEkle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKitapEkleActionPerformed(evt);
            }
        });

        btnKitapDuzenle.setText("Düzenle");
        btnKitapDuzenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKitapDuzenleActionPerformed(evt);
            }
        });

        jButton7.setText("Temizle");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        btnKitapSil.setText("Sil");
        btnKitapSil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKitapSilActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnKitapEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(btnKitapDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnKitapSil, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnKitapEkle)
                    .addComponent(btnKitapDuzenle)
                    .addComponent(jButton7)
                    .addComponent(btnKitapSil))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpnlKitapLayout = new javax.swing.GroupLayout(jpnlKitap);
        jpnlKitap.setLayout(jpnlKitapLayout);
        jpnlKitapLayout.setHorizontalGroup(
            jpnlKitapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlKitapLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnlKitapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpnlKitapLayout.setVerticalGroup(
            jpnlKitapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlKitapLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );

        jtbbTab.addTab("Kitap Ekle", jpnlKitap);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Yazarlar"));

        jLabel12.setText("Yazar Adı - Soyadı");
        jLabel12.setLocation(new java.awt.Point(87, 0));

        btnYazarEkle.setText("Yeni Ekle");
        btnYazarEkle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYazarEkleActionPerformed(evt);
            }
        });

        btnYazarDuzenle.setText("Düzenle");
        btnYazarDuzenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYazarDuzenleActionPerformed(evt);
            }
        });

        btnYazarSil.setText("Sil");
        btnYazarSil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYazarSilActionPerformed(evt);
            }
        });

        btnYazarTemizle.setText("Temizle");
        btnYazarTemizle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYazarTemizleActionPerformed(evt);
            }
        });

        tblYazar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblYazar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblYazarMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblYazar);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtYazarAdi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnYazarTemizle, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnYazarSil, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnYazarEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnYazarDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtYazarAdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnYazarTemizle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnYazarEkle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnYazarDuzenle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnYazarSil)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jpnlYazarLayout = new javax.swing.GroupLayout(jpnlYazar);
        jpnlYazar.setLayout(jpnlYazarLayout);
        jpnlYazarLayout.setHorizontalGroup(
            jpnlYazarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlYazarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnlYazarLayout.setVerticalGroup(
            jpnlYazarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlYazarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jtbbTab.addTab("Yazarlar", jpnlYazar);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Türler"));
        jPanel11.setPreferredSize(new java.awt.Dimension(408, 75));

        jLabel16.setText("Tür Adı");

        txtTurAdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTurAdiActionPerformed(evt);
            }
        });

        btnTurEkle.setText("Yeni Ekle");
        btnTurEkle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTurEkleActionPerformed(evt);
            }
        });

        btnTurDuzenle.setText("Düzenle");
        btnTurDuzenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTurDuzenleActionPerformed(evt);
            }
        });

        btnTurSil.setText("Sil");
        btnTurSil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTurSilActionPerformed(evt);
            }
        });

        jButton15.setText("Temizle");

        tblTur.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTur.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTurMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblTur);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(0, 6, Short.MAX_VALUE)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTurAdi, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTurEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTurDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTurSil, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTurAdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(btnTurEkle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTurDuzenle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTurSil)
                        .addGap(0, 298, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout jpnlTurLayout = new javax.swing.GroupLayout(jpnlTur);
        jpnlTur.setLayout(jpnlTurLayout);
        jpnlTurLayout.setHorizontalGroup(
            jpnlTurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlTurLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnlTurLayout.setVerticalGroup(
            jpnlTurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlTurLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                .addContainerGap())
        );

        jtbbTab.addTab("Türler", jpnlTur);

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Diller"));

        jLabel19.setText("Dil");

        btnDilEkle.setText("Yeni Ekle");
        btnDilEkle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDilEkleActionPerformed(evt);
            }
        });

        btnDilDuzenle.setText("Düzenle");
        btnDilDuzenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDilDuzenleActionPerformed(evt);
            }
        });

        btnDilSil.setText("Sil");
        btnDilSil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDilSilActionPerformed(evt);
            }
        });

        btnLanguageTemizle.setText("Temizle");
        btnLanguageTemizle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLanguageTemizleActionPerformed(evt);
            }
        });

        tblDil.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDilMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblDil);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLanguage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLanguageTemizle, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDilEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDilDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDilSil, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(btnLanguageTemizle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(btnDilEkle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDilDuzenle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDilSil)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jpnlDilLayout = new javax.swing.GroupLayout(jpnlDil);
        jpnlDil.setLayout(jpnlDilLayout);
        jpnlDilLayout.setHorizontalGroup(
            jpnlDilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlDilLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnlDilLayout.setVerticalGroup(
            jpnlDilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlDilLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jtbbTab.addTab("Diller", jpnlDil);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Yayınevleri"));
        jPanel7.setPreferredSize(new java.awt.Dimension(385, 100));

        jLabel22.setText("Yayınevi");

        btnYayineviEkle.setText("Yeni Ekle");
        btnYayineviEkle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYayineviEkleActionPerformed(evt);
            }
        });

        btnYayineviDuzenle.setText("Düzenle");
        btnYayineviDuzenle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYayineviDuzenleActionPerformed(evt);
            }
        });

        btnYayineviTemizle.setText("Temizle");
        btnYayineviTemizle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYayineviTemizleActionPerformed(evt);
            }
        });

        btnYayineviSil.setText("Sil");
        btnYayineviSil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYayineviSilActionPerformed(evt);
            }
        });

        tblYayinEvi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblYayinEvi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblYayinEviMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblYayinEvi);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtYayinevi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnYayineviTemizle, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnYayineviEkle, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnYayineviDuzenle, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnYayineviSil, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtYayinevi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(btnYayineviTemizle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(btnYayineviEkle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnYayineviDuzenle)
                        .addGap(12, 12, 12)
                        .addComponent(btnYayineviSil)
                        .addGap(0, 292, Short.MAX_VALUE))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jpnlYayineviLayout = new javax.swing.GroupLayout(jpnlYayinevi);
        jpnlYayinevi.setLayout(jpnlYayineviLayout);
        jpnlYayineviLayout.setHorizontalGroup(
            jpnlYayineviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlYayineviLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpnlYayineviLayout.setVerticalGroup(
            jpnlYayineviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlYayineviLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                .addContainerGap())
        );

        jtbbTab.addTab("Yayınevleri", jpnlYayinevi);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jLabel1.setText("Kütüphane Otomasyonu");

        btnLogOut.setText("Çıkış");
        btnLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogOut)
                .addGap(17, 17, 17))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtbbTab, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogOut))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtbbTab, javax.swing.GroupLayout.PREFERRED_SIZE, 532, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnGoYeviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoYeviActionPerformed
        jtbbTab.setSelectedComponent(jpnlYayinevi);
    }//GEN-LAST:event_btnGoYeviActionPerformed

    private void btnGoYazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoYazarActionPerformed
        jtbbTab.setSelectedComponent(jpnlYazar);
    }//GEN-LAST:event_btnGoYazarActionPerformed

    private void btnGoDilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoDilActionPerformed
        jtbbTab.setSelectedComponent(jpnlDil);
    }//GEN-LAST:event_btnGoDilActionPerformed

    private void btnGoTurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoTurActionPerformed
        jtbbTab.setSelectedComponent(jpnlTur);
    }//GEN-LAST:event_btnGoTurActionPerformed
    String yazarId = null;
    private void cbYazarAdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbYazarAdiActionPerformed
        yazarId = String.valueOf(fncIdGetirme("aid", "author", String.valueOf(cbYazarAdi.getSelectedItem())));
    }//GEN-LAST:event_cbYazarAdiActionPerformed
    String turId = null;
    private void cbKitapTuruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKitapTuruActionPerformed
        turId = String.valueOf(fncIdGetirme("gid", "genre", String.valueOf(cbKitapTuru.getSelectedItem())));
    }//GEN-LAST:event_cbKitapTuruActionPerformed

    private void tblKitapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKitapMouseClicked
        int row = tblKitap.getSelectedRow();
        id = Integer.valueOf(String.valueOf(tblKitap.getValueAt(row, 0))); // object değişkeni önce stringe cast edildi. sonra int e cast edildi.
        cbKitapTuru.setSelectedItem(tblKitap.getValueAt(row, 1));
        txtKitapAdi.setText("" + tblKitap.getValueAt(row, 2));
        txtIsbnNo.setText("" + tblKitap.getValueAt(row, 3));
        cbYazarAdi.setSelectedItem(tblKitap.getValueAt(row, 4));
        cbKitapDili.setSelectedItem(tblKitap.getValueAt(row, 5));
        cbYayinevi.setSelectedItem(tblKitap.getValueAt(row, 6));
        txtKitaplikNo.setText("" + tblKitap.getValueAt(row, 7));
        txtRafNo.setText("" + tblKitap.getValueAt(row, 8));
        txtSiraNo.setText("" + tblKitap.getValueAt(row, 9));
        txtOzet.setText("" + tblKitap.getValueAt(row, 10));
        selection = tblKitap.getSelectedRows();

    }//GEN-LAST:event_tblKitapMouseClicked

    String tur = null;
    String yazar = null;
    String dil = null;
    String yevi = null;
    String kitapAdi = null;
    String isbn = null;
    String kitaplikNo = null;
    String rafNo = null;
    String siraNo = null;
    String ozet = null;

    private void btnKitapEkleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKitapEkleActionPerformed
        fncDegerAtama();
        fncKitapSorgu("insert");
    }//GEN-LAST:event_btnKitapEkleActionPerformed

    public int fncIdGetirme(String gelenSutun, String gelenTablo, String gelenNesne) {
        int dbId = 0;
        try {
            ResultSet rs = new DBConnect().baglan().executeQuery("select " + gelenSutun + " from " + gelenTablo + " where " + gelenTablo + " = '" + gelenNesne + "' ");
            while (rs.next()) {
                dbId = Integer.valueOf(rs.getString(gelenSutun));
            }
        } catch (SQLException ex) {
            Logger.getLogger(anaForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(gelenSutun + " tur id : " + dbId);
        return dbId;

    }

    private void btnYayineviTemizleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYayineviTemizleActionPerformed
        txtYayinevi.setText(null);
        txtYayinevi.requestFocus();
        id = 0;
    }//GEN-LAST:event_btnYayineviTemizleActionPerformed

    private void btnYayineviEkleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYayineviEkleActionPerformed
        fncEkle("yevi", txtYayinevi.getText());
    }//GEN-LAST:event_btnYayineviEkleActionPerformed

    private void btnLanguageTemizleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLanguageTemizleActionPerformed
        txtLanguage.setText(null);
        txtLanguage.requestFocus();
    }//GEN-LAST:event_btnLanguageTemizleActionPerformed

    private void btnDilEkleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDilEkleActionPerformed
        fncEkle("dil", txtLanguage.getText());
    }//GEN-LAST:event_btnDilEkleActionPerformed

    private void btnYazarTemizleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYazarTemizleActionPerformed
        txtYazarAdi.setText(null);
        txtYazarAdi.requestFocus();
    }//GEN-LAST:event_btnYazarTemizleActionPerformed

    private void btnYazarEkleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYazarEkleActionPerformed
        fncEkle("yazar", txtYazarAdi.getText());
    }//GEN-LAST:event_btnYazarEkleActionPerformed

    private void tblYayinEviMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblYayinEviMouseClicked
        int row = tblYayinEvi.getSelectedRow();
        id = Integer.valueOf(String.valueOf(tblYayinEvi.getValueAt(row, 0))); // object değişkeni önce stringe cast edildi. sonra int e cast edildi.
        txtYayinevi.setText("" + tblYayinEvi.getValueAt(row, 1));//yapıkredi
        selection = tblYayinEvi.getSelectedRows();
    }//GEN-LAST:event_tblYayinEviMouseClicked

    private void tblDilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDilMouseClicked
        int row = tblDil.getSelectedRow();
        id = Integer.valueOf(String.valueOf(tblDil.getValueAt(row, 0))); // object değişkeni önce stringe cast edildi. sonra int e cast edildi.
        txtLanguage.setText("" + tblDil.getValueAt(row, 1));
        selection = tblDil.getSelectedRows();
    }//GEN-LAST:event_tblDilMouseClicked

    private void tblYazarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblYazarMouseClicked
        int row = tblYazar.getSelectedRow();
        id = Integer.valueOf(String.valueOf(tblYazar.getValueAt(row, 0))); // object değişkeni önce stringe cast edildi. sonra int e cast edildi.
        txtYazarAdi.setText("" + tblYazar.getValueAt(row, 1));
        selection = tblYazar.getSelectedRows();
    }//GEN-LAST:event_tblYazarMouseClicked

    private void btnYayineviDuzenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYayineviDuzenleActionPerformed
        fncDuzenle("yevi", txtYayinevi.getText());
    }//GEN-LAST:event_btnYayineviDuzenleActionPerformed

    private void btnDilDuzenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDilDuzenleActionPerformed
        fncDuzenle("dil", txtLanguage.getText());
    }//GEN-LAST:event_btnDilDuzenleActionPerformed

    private void btnYazarDuzenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYazarDuzenleActionPerformed
        fncDuzenle("yazar", txtYazarAdi.getText());
    }//GEN-LAST:event_btnYazarDuzenleActionPerformed

    private void btnYayineviSilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYayineviSilActionPerformed
        ArrayList<String> cokluId = new ArrayList<>();
        if (id > 0) {
            for (int s : selection) {
                secilenId = String.valueOf(tblYayinEvi.getValueAt(s, 0));
                cokluId.add(secilenId);
            }
            fncSil("yevi", cokluId);
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen seçim yapınız.");
        }
    }//GEN-LAST:event_btnYayineviSilActionPerformed

    private void btnDilSilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDilSilActionPerformed
        ArrayList<String> cokluId = new ArrayList<>();
        if (id > 0) {
            for (int s : selection) {
                secilenId = String.valueOf(tblDil.getValueAt(s, 0));
                cokluId.add(secilenId);
            }
            fncSil("dil", cokluId);
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen seçim yapınız.");
        }

    }//GEN-LAST:event_btnDilSilActionPerformed

    private void btnYazarSilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYazarSilActionPerformed
        ArrayList<String> cokluId = new ArrayList<>();
        if (id > 0) {
            for (int s : selection) {
                secilenId = String.valueOf(tblYazar.getValueAt(s, 0));
                cokluId.add(secilenId);
            }
            fncSil("yazar", cokluId);
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen seçim yapınız.");
        }
    }//GEN-LAST:event_btnYazarSilActionPerformed

    private void btnKitapDuzenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKitapDuzenleActionPerformed
        fncKitapSorgu("update");
        fncDegerAtama();
    }//GEN-LAST:event_btnKitapDuzenleActionPerformed

    private void cbKitapTuruItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbKitapTuruItemStateChanged

    }//GEN-LAST:event_cbKitapTuruItemStateChanged
    String dilId = null;
    private void cbKitapDiliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKitapDiliActionPerformed
        dilId = String.valueOf(fncIdGetirme("lid", "language", String.valueOf(cbKitapDili.getSelectedItem())));
    }//GEN-LAST:event_cbKitapDiliActionPerformed
    String yeviId = null;
    private void cbYayineviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbYayineviActionPerformed
        yeviId = String.valueOf(fncIdGetirme("pid", "publisher", String.valueOf(cbYayinevi.getSelectedItem())));
    }//GEN-LAST:event_cbYayineviActionPerformed
    ArrayList<String> cokluIdDelete = new ArrayList<>();
    private void btnKitapSilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKitapSilActionPerformed

        if (id > 0) {
            for (int s : selection) {
                secilenId = String.valueOf(tblKitap.getValueAt(s, 0));
                cokluIdDelete.add(secilenId);
            }
            fncKitapSorgu("delete");
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen seçim yapınız.");
        }
    }//GEN-LAST:event_btnKitapSilActionPerformed

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        int cikisCevap = JOptionPane.showConfirmDialog(this, "Çıkmak istediğinizden emin misiniz?", "Çıkış", 0);
        if (cikisCevap == 0) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnLogOutActionPerformed

    private void txtKitapAraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKitapAraKeyTyped

    }//GEN-LAST:event_txtKitapAraKeyTyped

    private void txtKitapAraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKitapAraKeyPressed
        
    }//GEN-LAST:event_txtKitapAraKeyPressed

    private void txtKitapAraInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtKitapAraInputMethodTextChanged

    }//GEN-LAST:event_txtKitapAraInputMethodTextChanged

    private void txtKitapAraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKitapAraActionPerformed

    }//GEN-LAST:event_txtKitapAraActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        txtKitapAdi.setText("");
        txtIsbnNo.setText("");
        txtKitaplikNo.setText("");
        txtSiraNo.setText("");
        txtRafNo.setText("");
        txtOzet.setText("");
        cbKitapDili.setSelectedIndex(0);
        cbKitapTuru.setSelectedIndex(0);
        cbYayinevi.setSelectedIndex(0);
        cbYazarAdi.setSelectedIndex(0);
        cbKitapTuru.requestFocus();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void tblTurMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTurMouseClicked
        int row = tblTur.getSelectedRow();
        id = Integer.valueOf(String.valueOf(tblTur.getValueAt(row, 0))); // object değişkeni önce stringe cast edildi. sonra int e cast edildi.
        txtTurAdi.setText("" + tblTur.getValueAt(row, 1));
        selection = tblTur.getSelectedRows();
    }//GEN-LAST:event_tblTurMouseClicked

    private void btnTurSilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTurSilActionPerformed
        ArrayList<String> cokluId = new ArrayList<>();
        if (id > 0) {
            for (int s : selection) {
                secilenId = String.valueOf(tblTur.getValueAt(s, 0));
                cokluId.add(secilenId);
            }
            fncSil("tur", cokluId);
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen seçim yapınız.");
        }
    }//GEN-LAST:event_btnTurSilActionPerformed

    private void btnTurDuzenleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTurDuzenleActionPerformed
        fncDuzenle("tur", txtTurAdi.getText());
    }//GEN-LAST:event_btnTurDuzenleActionPerformed

    private void btnTurEkleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTurEkleActionPerformed
        fncEkle("tur", txtTurAdi.getText());
    }//GEN-LAST:event_btnTurEkleActionPerformed

    private void txtTurAdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTurAdiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTurAdiActionPerformed

    private void chbKitapAdiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chbKitapAdiMouseClicked
        fncKitapAra();
    }//GEN-LAST:event_chbKitapAdiMouseClicked

    private void chbYazarAdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbYazarAdiActionPerformed
        fncKitapAra();
    }//GEN-LAST:event_chbYazarAdiActionPerformed

    private void txtKitapAdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKitapAdiActionPerformed

    }//GEN-LAST:event_txtKitapAdiActionPerformed

    private void txtKitapAdiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKitapAdiKeyPressed

    }//GEN-LAST:event_txtKitapAdiKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

    }//GEN-LAST:event_formWindowClosing

    private void cbKitapTuruInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_cbKitapTuruInputMethodTextChanged

    }//GEN-LAST:event_cbKitapTuruInputMethodTextChanged

    private void txtKitapAraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKitapAraKeyReleased
       fncKitapAra();
    }//GEN-LAST:event_txtKitapAraKeyReleased

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(anaForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new anaForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDilDuzenle;
    private javax.swing.JButton btnDilEkle;
    private javax.swing.JButton btnDilSil;
    private javax.swing.JButton btnGoDil;
    private javax.swing.JButton btnGoTur;
    private javax.swing.JButton btnGoYazar;
    private javax.swing.JButton btnGoYevi;
    private javax.swing.JButton btnKitapDuzenle;
    private javax.swing.JButton btnKitapDuzenle1;
    private javax.swing.JButton btnKitapDuzenle2;
    private javax.swing.JButton btnKitapDuzenle3;
    private javax.swing.JButton btnKitapDuzenle4;
    private javax.swing.JButton btnKitapEkle;
    private javax.swing.JButton btnKitapEkle1;
    private javax.swing.JButton btnKitapEkle2;
    private javax.swing.JButton btnKitapEkle3;
    private javax.swing.JButton btnKitapEkle4;
    private javax.swing.JButton btnKitapSil;
    private javax.swing.JButton btnKitapSil1;
    private javax.swing.JButton btnKitapSil2;
    private javax.swing.JButton btnKitapSil3;
    private javax.swing.JButton btnKitapSil4;
    private javax.swing.JButton btnLanguageTemizle;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnTurDuzenle;
    private javax.swing.JButton btnTurEkle;
    private javax.swing.JButton btnTurSil;
    private javax.swing.JButton btnYayineviDuzenle;
    public javax.swing.JButton btnYayineviEkle;
    private javax.swing.JButton btnYayineviSil;
    private javax.swing.JButton btnYayineviTemizle;
    private javax.swing.JButton btnYazarDuzenle;
    private javax.swing.JButton btnYazarDuzenle1;
    private javax.swing.JButton btnYazarEkle;
    private javax.swing.JButton btnYazarEkle1;
    private javax.swing.JButton btnYazarSil;
    private javax.swing.JButton btnYazarSil1;
    private javax.swing.JButton btnYazarTemizle;
    private javax.swing.JButton btnYazarTemizle1;
    private javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JComboBox<String> cbKitapDili;
    public javax.swing.JComboBox<String> cbKitapTuru;
    public javax.swing.JComboBox<String> cbYayinevi;
    public javax.swing.JComboBox<String> cbYazarAdi;
    private javax.swing.JCheckBox chbKitapAdi;
    private javax.swing.JCheckBox chbYazarAdi;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JPanel jpnlDil;
    private javax.swing.JPanel jpnlKitap;
    private javax.swing.JPanel jpnlSorgula;
    private javax.swing.JPanel jpnlTur;
    private javax.swing.JPanel jpnlYayinevi;
    private javax.swing.JPanel jpnlYazar;
    private javax.swing.JTabbedPane jtbbTab;
    private javax.swing.JTable tblDil;
    public javax.swing.JTable tblKitap;
    public javax.swing.JTable tblSorgula;
    private javax.swing.JTable tblTur;
    private javax.swing.JTable tblYayinEvi;
    private javax.swing.JTable tblYazar;
    private javax.swing.JTable tblYazar1;
    public javax.swing.JTextField txtIsbnNo;
    public javax.swing.JTextField txtKitapAdi;
    private javax.swing.JTextField txtKitapAra;
    public javax.swing.JTextField txtKitaplikNo;
    private javax.swing.JTextField txtLanguage;
    public javax.swing.JTextArea txtOzet;
    public javax.swing.JTextField txtRafNo;
    public javax.swing.JTextField txtSiraNo;
    private javax.swing.JTextField txtTurAdi;
    public javax.swing.JTextField txtYayinevi;
    private javax.swing.JTextField txtYazarAdi;
    // End of variables declaration//GEN-END:variables

}
