package org.example.week10;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static final String DB_URL = "jdbc:sqlite:catatan.db";
    private static DBManager instance;
    private Connection connection;

    private DBManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            if (connection != null) {
                System.out.println("Koneksi ke database berhasil!");
            } else {
                System.out.println("Gagal melakukan koneksi ke database.");
            }
            createTableIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertCatatan(Catatan baru) {
    }

    private void createTableIfNotExists() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS catatan ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "judul TEXT NOT NULL, "
                + "konten TEXT NOT NULL, "
                + "deadline TEXT)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableQuery);
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

    // Menambahkan catatan baru ke database
    public void tambahCatatan(Catatan catatan) {
        String query = "INSERT INTO catatan (judul, konten, deadline) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, catatan.getJudul());
            stmt.setString(2, catatan.getIsi());
            stmt.setString(3, catatan.getDeadline());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Mengambil semua catatan dari database
    public List<Catatan> getAllCatatan() {
        List<Catatan> catatanList = new ArrayList<>();
        String query = "SELECT * FROM catatan";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Catatan catatan = new Catatan(
                        rs.getInt("id"),
                        rs.getString("judul"),
                        rs.getString("konten"),
                        rs.getString("deadline")
                );
                catatanList.add(catatan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return catatanList;
    }

    // Memperbarui catatan di database
    public void updateCatatan(Catatan catatan) {
        String query = "UPDATE catatan SET judul = ?, konten = ?, deadline = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, catatan.getJudul());
            stmt.setString(2, catatan.getIsi());
            stmt.setString(3, catatan.getDeadline());
            stmt.setInt(4, catatan.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Menghapus catatan dari database
    public void hapusCatatan(int id) {
        String query = "DELETE FROM catatan WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
