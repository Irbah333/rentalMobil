package controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.PengembalianModel;
import classes.DatabaseConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RiwayatController")
public class RiwayatController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<PengembalianModel> transaksiList = new ArrayList<>();
        DatabaseConnection db = new DatabaseConnection();

        if (db.isConnected) {
            try {
                // Query untuk menggabungkan tabel
                String sql = "SELECT " +
                             "transaksi.idTransaksi, " +
                             "mobil.merk, " +
                             "mobil.no_plat, " +
                             "penyewa.nama AS namaPenyewa, " +
                             "transaksi.harga, " +
                             "transaksi.tgl_pinjam, " +
                             "transaksi.lama, " +
                             "transaksi.tgl_kembali, " +
                             "transaksi.denda, " +
                             "transaksi.total_harga " +
                             "FROM transaksi " +
                             "INNER JOIN mobil ON transaksi.idMobil = mobil.idMobil " +
                             "INNER JOIN penyewa ON transaksi.idPenyewa = penyewa.idPenyewa " +
                             "WHERE transaksi.lama IS NOT NULL " +
                             "AND transaksi.tgl_kembali IS NOT NULL " +
                             "AND transaksi.denda IS NOT NULL " +
                             "AND transaksi.total_harga IS NOT NULL " ;

                ResultSet resultSet = db.runSelectQuery(sql);

                // Iterasi buat dapetin hasil query daftar riwayat
                while (resultSet.next()) {
                    PengembalianModel transaksi = new PengembalianModel();
                    transaksi.setIdTransaksi(resultSet.getString("idTransaksi"));
                    transaksi.setMerk(resultSet.getString("merk"));
                    transaksi.setNoPlat(resultSet.getString("no_plat"));
                    transaksi.setNama(resultSet.getString("namaPenyewa")); 
                    transaksi.setHarga(resultSet.getInt("harga"));
                    transaksi.setTglPinjam(resultSet.getString("tgl_pinjam"));
                    transaksi.setLama(resultSet.getInt("lama"));
                    transaksi.setTglKembali(resultSet.getString("tgl_kembali"));
                    transaksi.setDenda(resultSet.getInt("denda"));
                    transaksi.setTotal_harga(resultSet.getInt("total_harga"));
                    transaksiList.add(transaksi);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.disconnect();
            }
        } else {
            request.setAttribute("error", "Database connection failed: " + db.message);
        }

        request.setAttribute("transaksiList", transaksiList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/Riwayat.jsp");
        dispatcher.forward(request, response);
    }
}
