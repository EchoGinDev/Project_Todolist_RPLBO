package org.example.week10.manager;

import org.example.week10.Catatan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class untuk manajemen koneksi dan operasi database SQLite.
 */
public class DBManager {
    private static DBManager instance;  // Objek singleton
    private Connection conn;            // Koneksi ke database

    /**
     * Konstruktor private: hanya bisa diakses dari dalam class.
     * Membuka koneksi SQLite dan pastikan tabel sudah ada.
     */
    private DBManager() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:catatan.db"); // Koneksi ke SQLite lokal
            createTableIfNotExists();  // Buat tabel jika belum ada
            alterTableIfNeeded();      // Tambahan: pastikan kolom selesai ada
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mengembalikan instance tunggal dari DBManager (Singleton Pattern).
     */
    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    /**
     * Membuat tabel catatan jika belum ada.
     */
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS catatan (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "judul TEXT NOT NULL," +
                "konten TEXT," +
                "deadline TEXT," +
                "kategori TEXT," +
                "selesai INTEGER DEFAULT 0)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cek apakah kolom `selesai` ada, jika belum, tambahkan.
     * Diabaikan kalau sudah ada (akan throw error yang kita abaikan).
     */
    private void alterTableIfNeeded() {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("ALTER TABLE catatan ADD COLUMN selesai INTEGER DEFAULT 0");
        } catch (SQLException ignored) {
            // Kemungkinan besar kolom sudah ada, jadi diabaikan
        }
    }

    /**
     * Mengambil semua catatan dari database.
     */
    public List<Catatan> getAllCatatan() {
        List<Catatan> list = new ArrayList<>();
        String sql = "SELECT * FROM catatan";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String judul = rs.getString("judul");
                String konten = rs.getString("konten");
                String deadline = rs.getString("deadline");
                String kategori = rs.getString("kategori");
                boolean selesai = rs.getBoolean("selesai");

                Catatan catatan = new Catatan(id, judul, konten, deadline, kategori);
                catatan.setSelesai(selesai); // set status selesai
                list.add(catatan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Menambahkan catatan baru ke database.
     */
    public void tambahCatatan(Catatan catatan) {
        String sql = "INSERT INTO catatan (judul, konten, deadline, kategori, selesai) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, catatan.getJudul());
            pstmt.setString(2, catatan.getKonten());
            pstmt.setString(3, catatan.getDeadline());
            pstmt.setString(4, catatan.getKategori());
            pstmt.setBoolean(5, catatan.isSelesai());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Menghapus catatan berdasarkan ID.
     */
    public void hapusCatatan(int id) {
        String sql = "DELETE FROM catatan WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Memperbarui semua data catatan berdasarkan ID.
     */
    public void updateCatatan(Catatan catatan) {
        String sql = "UPDATE catatan SET judul = ?, konten = ?, deadline = ?, kategori = ?, selesai = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, catatan.getJudul());
            pstmt.setString(2, catatan.getKonten());
            pstmt.setString(3, catatan.getDeadline());
            pstmt.setString(4, catatan.getKategori());
            pstmt.setBoolean(5, catatan.isSelesai());
            pstmt.setInt(6, catatan.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Memperbarui hanya status "selesai" sebuah catatan berdasarkan ID.
     */
    public void updateCatatanStatus(int id, boolean selesai) {
        String sql = "UPDATE catatan SET selesai = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, selesai);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
