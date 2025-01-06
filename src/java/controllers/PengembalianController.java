package controllers;
import models.PengembalianModel;
import classes.DatabaseConnection;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PengembalianController")
public class PengembalianController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        DatabaseConnection db = new DatabaseConnection();
        
        try {
            // Ambil daftar transaksi yang belum dikembalikan
            handleGetUnreturnedTransactions(request, response, db);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Terjadi kesalahan pada server: " + e.getMessage());
        } finally {
            db.disconnect();
            forwardToPage(request, response, "pages/Pengembalian.jsp");
        }
    }
    

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String action = request.getParameter("action");
    String idTransaksi = request.getParameter("idTransaksi");
    String tanggalKembali = request.getParameter("tanggalKembali");
    int tarifDenda = 50000;

    DatabaseConnection db = new DatabaseConnection();

    try {
        handleGetUnreturnedTransactions(request, response, db);
        if ("getTransaction".equals(action)) {
            if (idTransaksi == null || idTransaksi.trim().isEmpty()) {
                request.setAttribute("error", "ID Transaksi tidak boleh kosong.");
            } else {
                
                handleGetTransaction(request, response, db, idTransaksi);
            }
        } else if ("calculateAndUpdate".equals(action)) {  
            handleCalculateAndUpdate(request, response, db, idTransaksi, tanggalKembali, tarifDenda);
            
        } else {
            request.setAttribute("error", "Aksi tidak valid.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        request.setAttribute("error", "Terjadi kesalahan pada server: " + e.getMessage());
    } finally {
        db.disconnect();
        forwardToPage(request, response, "pages/Pengembalian.jsp");
    }

}

    private void handleGetTransaction(HttpServletRequest request, HttpServletResponse response,
                                      DatabaseConnection db, String idTransaksi)
            throws SQLException, ServletException, IOException {

        // Query untuk mendapatkan detail transaksi berdasarkan ID
        String query = "SELECT t.idTransaksi, t.no_plat, t.tgl_pinjam, t.lama, p.nama " +
                       "FROM transaksi t " +
                       "JOIN penyewa p ON t.idPenyewa = p.idPenyewa " +
                       "WHERE t.idTransaksi = ?";

        try (PreparedStatement stmt = db.getConnection().prepareStatement(query)) {
            stmt.setString(1, idTransaksi);
            ResultSet rs = stmt.executeQuery();

            // Set data transaksi untuk ditampilkan di JSP
            if (rs.next()) {
                request.setAttribute("no_plat", rs.getString("no_plat"));
                request.setAttribute("nama", rs.getString("nama"));
                request.setAttribute("tgl_pinjam", rs.getString("tgl_pinjam"));
                request.setAttribute("lama", rs.getInt("lama"));
                request.setAttribute("idTransaksi", idTransaksi);
            } else {
                request.setAttribute("error", "Transaksi tidak ditemukan.");
            }
            
            forwardToPage(request, response, "pages/Pengembalian.jsp");
        }
    }

    private void handleCalculateAndUpdate(HttpServletRequest request, HttpServletResponse response,
                                        DatabaseConnection db, String idTransaksi, String tanggalKembali,
                                        int tarifDenda) throws SQLException, ServletException, IOException {

      // Query untuk mendapatkan data transaksi terkait
      String query = "SELECT tgl_pinjam, lama, harga, idMobil FROM transaksi WHERE idTransaksi = ?";

      try (PreparedStatement stmt = db.getConnection().prepareStatement(query)) {
          stmt.setString(1, idTransaksi);
          ResultSet rs = stmt.executeQuery();

          if (rs.next()) {
              LocalDate tglPinjam = LocalDate.parse(rs.getString("tgl_pinjam"));
              int lamaSewa = rs.getInt("lama");
              int harga = rs.getInt("harga");
              LocalDate tglKembali = LocalDate.parse(tanggalKembali);
              int idMobil = rs.getInt("idMobil"); // Mendapatkan ID mobil terkait

              // Hitung denda
              long keterlambatan = ChronoUnit.DAYS.between(tglPinjam.plusDays(lamaSewa), tglKembali);
              int denda = (keterlambatan > 0) ? (int) keterlambatan * tarifDenda : 0;

              int totalHarga = harga + denda;

              // Update data transaksi 
              String updateQuery = "UPDATE transaksi SET tgl_kembali = ?, denda = ?, total_harga = ? WHERE idTransaksi = ?";
              try (PreparedStatement updateStmt = db.getConnection().prepareStatement(updateQuery)) {
                  updateStmt.setString(1, tanggalKembali);
                  updateStmt.setInt(2, denda);
                  updateStmt.setInt(3, totalHarga);
                  updateStmt.setString(4, idTransaksi);
                  updateStmt.executeUpdate();
              }

              // Update status mobil 
              String updateMobilQuery = "UPDATE mobil SET status = 'Tersedia' WHERE idMobil = ?";
              try (PreparedStatement updateMobilStmt = db.getConnection().prepareStatement(updateMobilQuery)) {
                  updateMobilStmt.setInt(1, idMobil);
                  updateMobilStmt.executeUpdate();
              }

              // Kirim atribut ke JSP
              request.setAttribute("denda", denda);
              request.setAttribute("total_harga", totalHarga);
              request.setAttribute("success", "Data transaksi berhasil diperbarui, total harga dihitung, dan status mobil diperbarui menjadi 'Tersedia'.");
          } else {
              request.setAttribute("error", "Transaksi tidak ditemukan.");
          }
          handleGetUnreturnedTransactions(request, response, db);
          forwardToPage(request, response, "pages/Pengembalian.jsp");
      }
  }


    
    private void handleGetUnreturnedTransactions(HttpServletRequest request, HttpServletResponse response, DatabaseConnection db)
        throws SQLException {

    // Query untuk mendapatkan daftar transaksi yang belum dikembalikan
    String query = "SELECT t.idTransaksi, m.merk, t.no_plat, p.nama, t.harga, t.tgl_pinjam, t.lama " +
                   "FROM transaksi t " +
                   "JOIN penyewa p ON t.idPenyewa = p.idPenyewa " +
                   "JOIN mobil m ON t.idMobil = m.idMobil " +
                   "WHERE t.tgl_kembali IS NULL OR t.tgl_kembali = ''";

    List<PengembalianModel> transaksiList = new ArrayList<>();
    

    try (PreparedStatement stmt = db.getConnection().prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        if (!rs.isBeforeFirst()) {  // Cek apakah result set kosong
            System.out.println("Tidak ada transaksi yang belum dikembalikan.");
        } else {
            while (rs.next()) {
                // Tambahkan setiap transaksi ke daftar model
                PengembalianModel transaksi = new PengembalianModel();
                transaksi.setIdTransaksi(rs.getString("idTransaksi"));
                transaksi.setMerk(rs.getString("merk"));
                transaksi.setNoPlat(rs.getString("no_plat"));
                transaksi.setNama(rs.getString("nama"));
                transaksi.setHarga(rs.getInt("harga"));
                transaksi.setTglPinjam(rs.getString("tgl_pinjam"));
                transaksi.setLama(rs.getInt("lama"));

                transaksiList.add(transaksi);
                System.out.println("Menambahkan transaksi ID: " + rs.getString("idTransaksi"));
            }
        }
    } catch (SQLException e) {
        System.out.println("Error saat mengambil transaksi yang belum dikembalikan: " + e.getMessage());
        e.printStackTrace();
    }

    // Set data yang diambil ke atribut request
    request.setAttribute("unreturnedTransactions", transaksiList);
}


    private void forwardToPage(HttpServletRequest request, HttpServletResponse response, String page)
            throws ServletException, IOException {
        // Arahkan ke halaman JSP tertentu
        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }
    
}
