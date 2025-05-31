package org.example.week10.manager;

import org.example.week10.Catatan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class DBManager {
    private static DBManager instance;
    private Connection conn;

    private DBManager() {
        try {
            conn = getConnection("jdbc:sqlite:catatan.db");
            createTableIfNotExists();
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
                "kategori TEXT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
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

                if (deadline != null && deadline.length() > 10) {
                    deadline = deadline.substring(0, 10);
                }

                list.add(new Catatan(id, judul, konten, deadline, kategori));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void tambahCatatan(Catatan catatan) {
        String sql = "INSERT INTO catatan (judul, konten, deadline, kategori) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, catatan.getJudul());
            pstmt.setString(2, catatan.getKonten());
            pstmt.setString(3, catatan.getDeadline());
            pstmt.setString(4, catatan.getKategori());
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
        String sql = "UPDATE catatan SET judul = ?, konten = ?, deadline = ?, kategori = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, catatan.getJudul());
            pstmt.setString(2, catatan.getKonten());
            String deadline = catatan.getDeadline();
            if (deadline != null && deadline.length() > 10) {
                deadline = deadline.substring(0, 10);
            }
            pstmt.setString(3, deadline);
            pstmt.setString(4, catatan.getKategori());
            pstmt.setInt(5, catatan.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
