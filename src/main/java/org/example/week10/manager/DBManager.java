package org.example.week10.manager;

import org.example.week10.Catatan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static DBManager instance;
    private Connection conn;

    private DBManager() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:catatan.db");
            createTableIfNotExists();
            alterTableIfNeeded(); // Tambahan untuk pastikan kolom selesai ada
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

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

    private void alterTableIfNeeded() {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("ALTER TABLE catatan ADD COLUMN selesai INTEGER DEFAULT 0");
        } catch (SQLException ignored) {
            // kolom mungkin sudah ada
        }
    }

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
                catatan.setSelesai(selesai);
                list.add(catatan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

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

    public void hapusCatatan(int id) {
        String sql = "DELETE FROM catatan WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
