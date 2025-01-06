package controllers;

import models.PeminjamanModel;
import classes.DatabaseConnection;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PeminjamanController")
public class PeminjamanController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DatabaseConnection db = new DatabaseConnection();
        List<PeminjamanModel> daftarMobilTersedia = new ArrayList<>();

        try {
            // Ambil mobil dengan status "tersedia"
            String query = "SELECT idMobil, no_plat, merk FROM mobil WHERE status = 'tersedia' ORDER BY idMobil ASC";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PeminjamanModel mobil = new PeminjamanModel();
                mobil.setIdMobil(rs.getInt("idMobil"));
                mobil.setNoPlat(rs.getString("no_plat"));
                mobil.setNamaMobil(rs.getString("merk"));
                daftarMobilTersedia.add(mobil);
            }
            request.setAttribute("daftarMobilTersedia", daftarMobilTersedia);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Gagal mengambil data mobil tersedia: " + e.getMessage());
        } finally {
            db.disconnect();
        }

        // Forward ke JSP
        request.getRequestDispatcher("pages/Peminjaman.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nama = request.getParameter("nama");
        String alamat = request.getParameter("alamat");
        String telepon = request.getParameter("telepon");
        String email = request.getParameter("email");
        String plat = request.getParameter("plat");
        int lama = Integer.parseInt(request.getParameter("lama"));
        String tanggalPinjam = request.getParameter("tanggal");

        DatabaseConnection db = new DatabaseConnection();

        try {
            db.getConnection().setAutoCommit(false); // Mulai transaksi

            // Cek apakah penyewa sudah ada di tabel penyewa
            String idPenyewaQuery = "SELECT idPenyewa FROM penyewa WHERE nama = ?";
            PreparedStatement stmtPenyewa = db.getConnection().prepareStatement(idPenyewaQuery);
            stmtPenyewa.setString(1, nama);
            ResultSet rsPenyewa = stmtPenyewa.executeQuery();

            int idPenyewa;
            if (rsPenyewa.next()) {
                idPenyewa = rsPenyewa.getInt("idPenyewa");
            } else {
                // Insert penyewa baru
                String insertPenyewaQuery = "INSERT INTO penyewa (nama, alamat, telepon, email) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtInsertPenyewa = db.getConnection().prepareStatement(insertPenyewaQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                stmtInsertPenyewa.setString(1, nama);
                stmtInsertPenyewa.setString(2, alamat);
                stmtInsertPenyewa.setString(3, telepon);
                stmtInsertPenyewa.setString(4, email);
                stmtInsertPenyewa.executeUpdate();

                ResultSet rsGenerated = stmtInsertPenyewa.getGeneratedKeys();
                if (rsGenerated.next()) {
                    idPenyewa = rsGenerated.getInt(1);
                } else {
                    throw new Exception("Gagal menyimpan data penyewa.");
                }
            }

            // Ambil ID Mobil dan Harga Sewa
            String idMobilQuery = "SELECT idMobil, harga FROM mobil WHERE no_plat = ? AND status = 'Tersedia'";
            PreparedStatement stmtMobil = db.getConnection().prepareStatement(idMobilQuery);
            stmtMobil.setString(1, plat);
            ResultSet rsMobil = stmtMobil.executeQuery();

            if (!rsMobil.next()) {
                throw new Exception("Mobil dengan plat '" + plat + "' Tidak tersedia.");
            }
            int idMobil = rsMobil.getInt("idMobil");
            double hargaSewa = rsMobil.getDouble("harga"); // Ambil harga sewa

            // Simpan data transaksi
            String insertTransaksiQuery = "INSERT INTO transaksi (idPenyewa, idMobil, no_plat, harga, lama, tgl_pinjam, denda, total_harga) " +
                                          "VALUES (?, ?, ?, ?, ?, ?, 0, 0)";
            PreparedStatement stmtInsertTransaksi = db.getConnection().prepareStatement(insertTransaksiQuery);
            stmtInsertTransaksi.setInt(1, idPenyewa);
            stmtInsertTransaksi.setInt(2, idMobil);
            stmtInsertTransaksi.setString(3, plat);
            stmtInsertTransaksi.setDouble(4, hargaSewa); // Isi harga sewa di sini
            stmtInsertTransaksi.setInt(5, lama);
            stmtInsertTransaksi.setString(6, tanggalPinjam);
            stmtInsertTransaksi.executeUpdate();

            // Update status mobil menjadi "tidak tersedia"
            String updateMobilQuery = "UPDATE mobil SET status = 'Tidak tersedia' WHERE idMobil = ?";
            PreparedStatement stmtUpdateMobil = db.getConnection().prepareStatement(updateMobilQuery);
            stmtUpdateMobil.setInt(1, idMobil);
            stmtUpdateMobil.executeUpdate();

            db.getConnection().commit(); // Commit transaksi
            request.setAttribute("success", "Peminjaman berhasil disimpan.");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                db.getConnection().rollback(); // Rollback jika terjadi kesalahan
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            request.setAttribute("error", "Gagal menyimpan peminjaman: " + e.getMessage());
        } finally {
            db.disconnect();
            doGet(request, response); // Refresh data
        }
    }
}
