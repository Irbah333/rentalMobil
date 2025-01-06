package controllers;

import classes.DatabaseConnection;
import models.Mobil;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/MobilController")
public class MobilController extends HttpServlet {

   @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");
    String message = "";

    try {
        if ("add".equals(action)) {
            addMobil(request);
            message = "Data mobil berhasil ditambahkan!";
        } else if ("update".equals(action)) {
            updateMobil(request);
            message = "Data mobil berhasil diperbarui!";
        } else if ("delete".equals(action)) {
            deleteMobil(request);
            message = "Data mobil berhasil dihapus!";
        } else {
            message = "Aksi tidak dikenali!";
        }
    } catch (Exception e) {
        message = "Error: " + e.getMessage();
        e.printStackTrace();
    }

    // Redirect ke halaman utama dengan pesan
    request.setAttribute("message", message);
    getAllMobil(request, response);
}
private void addMobil(HttpServletRequest request) throws Exception {
    String merk = request.getParameter("merk");
    String noPlat = request.getParameter("no_plat");
    String tahun = request.getParameter("tahun");
    String harga = request.getParameter("harga");
    String status = request.getParameter("status");
    
    DatabaseConnection db = new DatabaseConnection();
    
        if (db.isConnected) {
            String query = "INSERT INTO mobil (merk, no_plat, tahun, harga, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = db.con.prepareStatement(query);
            pstmt.setString(1, merk);
            pstmt.setString(2, noPlat);
            pstmt.setInt(3, Integer.parseInt(tahun));
            pstmt.setDouble(4, Double.parseDouble(harga));
            pstmt.setString(5, status);
            pstmt.executeUpdate();
        } else {
            throw new Exception("Koneksi database gagal.");
        }
}
private void updateMobil(HttpServletRequest request) throws Exception {
    String id = request.getParameter("id");
    String merk = request.getParameter("merk");
    String noPlat = request.getParameter("no_plat");
    String tahun = request.getParameter("tahun");
    String harga = request.getParameter("harga");
    String status = request.getParameter("status");

    
    DatabaseConnection db = new DatabaseConnection();
        if (db.isConnected) {
            String query = "UPDATE mobil SET merk = ?, no_plat = ?, tahun = ?, harga = ?, status = ? WHERE idMobil = ?";
            PreparedStatement pstmt = db.con.prepareStatement(query);
            pstmt.setString(1, merk);
            pstmt.setString(2, noPlat);
            pstmt.setInt(3, Integer.parseInt(tahun));
            pstmt.setDouble(4, Double.parseDouble(harga));
            pstmt.setString(5, status);
            pstmt.setInt(6, Integer.parseInt(id));
            pstmt.executeUpdate();
        } else {
            throw new Exception("Koneksi database gagal.");
        }
}
private void deleteMobil(HttpServletRequest request) throws Exception {
    String id = request.getParameter("id");
    
    DatabaseConnection db = new DatabaseConnection();
        if (db.isConnected) {
            String query = "DELETE FROM mobil WHERE idMobil = ?";
            PreparedStatement pstmt = db.con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(id));
            pstmt.executeUpdate();
        } else {
            throw new Exception("Koneksi database gagal.");
        }
}

    // Method untuk mengambil semua data mobil
    private void getAllMobil(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Mobil> mobilList = new ArrayList<>();
        
        // Koneksi ke database
        DatabaseConnection db = new DatabaseConnection();
        try {
            if (db.isConnected) {
                String sql = "SELECT * FROM mobil";
                ResultSet rs = db.runSelectQuery(sql);
                
                // Ambil data dari ResultSet dan masukkan ke dalam list
                while (rs.next()) {
                    Mobil mobil = new Mobil();
                    mobil.setIdMobil(rs.getInt("idMobil"));
                    mobil.setMerk(rs.getString("merk"));
                    mobil.setNoPlat(rs.getString("no_plat"));
                    mobil.setTahun(rs.getInt("tahun"));
                    mobil.setHarga(rs.getDouble("harga"));
                    mobil.setStatus(rs.getString("status"));
                    mobilList.add(mobil);
                }
                
                // Kirimkan data mobil ke JSP
                request.setAttribute("mobilList", mobilList);
                
                // Pindahkan request dan response ke halaman JSP
                request.getRequestDispatcher("pages/DataMobil.jsp").forward(request, response);
            } else {
                throw new Exception("Koneksi database gagal: " + db.message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("pages/DataMobil.jsp").forward(request, response);
        } finally {
            db.disconnect();
        }
    }
    
    protected void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String merk = "", noPlat = "", status = "";
        int tahun = 0;
        double hargaSewa = 0;
        DatabaseConnection db = new DatabaseConnection();
        
        if (id != null && db.isConnected) {
            try (PreparedStatement pstmt = db.con.prepareStatement("SELECT * FROM mobil WHERE idMobil = ?")) {
                pstmt.setInt(1, Integer.parseInt(id));
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        merk = rs.getString("merk");
                        noPlat = rs.getString("no_plat");
                        tahun = rs.getInt("tahun");
                        hargaSewa = rs.getDouble("harga");
                        status = rs.getString("status");
                    }
                }
            } catch (Exception e) {
                request.setAttribute("error", "Error: " + e.getMessage());
            } finally {
                db.disconnect();
            }
        }

        request.setAttribute("merk", merk);
        request.setAttribute("noPlat", noPlat);
        request.setAttribute("tahun", tahun);
        request.setAttribute("hargaSewa", hargaSewa);
        request.setAttribute("status", status);

        RequestDispatcher dispatcher = request.getRequestDispatcher("mobil.jsp");
        dispatcher.forward(request, response);
    }

   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Aksi untuk menampilkan data mobil
        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            edit(request, response);
        }else {
            getAllMobil(request, response);
        }
    }
}