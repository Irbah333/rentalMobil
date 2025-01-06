package controllers;

import models.Admin;
import classes.DatabaseConnection;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AdminController")
public class AdminController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        DatabaseConnection db = new DatabaseConnection();

        try {
            if ("editAdmin".equals(action)) {
                // Arahkan ke halaman edit admin
                int id_user = Integer.parseInt(request.getParameter("id_user"));
                handleEditAdmin(request, db, id_user);
                forwardToPage(request, response, "/pages/EditAdmin.jsp");
            } else {
                // Default: tampilkan daftar admin
                handleGetAdminList(request, db);
                forwardToPage(request, response, "/pages/DataAdmin.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Terjadi kesalahan pada server: " + e.getMessage());
            forwardToPage(request, response, "/pages/DataAdmin.jsp");
        } finally {
            db.disconnect();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String username = request.getParameter("username");
        String nama_user = request.getParameter("nama_user");
        String password = request.getParameter("password");
        int id_user = -1;

        try {
            String idUserParam = request.getParameter("id_user");
            if (idUserParam != null && !idUserParam.isEmpty()) {
                id_user = Integer.parseInt(idUserParam);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID User harus berupa angka valid.");
        }

        DatabaseConnection db = new DatabaseConnection();

        try {
            if ("addAdmin".equals(action)) {
                handleAddAdmin(request, db, username, nama_user, password);
            } else if ("updateAdmin".equals(action)) {
                if (id_user == -1) {
                    request.setAttribute("error", "ID User tidak valid untuk pembaruan.");
                } else {
                    handleUpdateAdmin(request, db, id_user, username, nama_user, password);
                }
            } else if ("deleteAdmin".equals(action)) {
                if (id_user == -1) {
                    request.setAttribute("error", "ID User tidak valid untuk penghapusan.");
                } else {
                    handleDeleteAdmin(request, db, id_user);
                }
            } else {
                request.setAttribute("error", "Aksi tidak valid.");
            }

            // Refresh daftar admin setelah operasi
            handleGetAdminList(request, db);
            forwardToPage(request, response, "/pages/DataAdmin.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Terjadi kesalahan pada server: " + e.getMessage());
            forwardToPage(request, response, "/pages/DataAdmin.jsp");
        } finally {
            db.disconnect();
        }
    }

    private void handleEditAdmin(HttpServletRequest request, DatabaseConnection db, int id_user) throws SQLException {
        String query = "SELECT id_user, username, nama_user FROM users WHERE id_user = ?";

        try (PreparedStatement stmt = db.getConnection().prepareStatement(query)) {
            stmt.setInt(1, id_user);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Admin admin = new Admin();
                    admin.setId(rs.getInt("id_user"));
                    admin.setUsername(rs.getString("username"));
                    admin.setNama(rs.getString("nama_user"));
                    request.setAttribute("admin", admin);
                } else {
                    request.setAttribute("error", "Data admin tidak ditemukan.");
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Gagal mengambil data admin: " + e.getMessage());
        }
    }

    private void handleGetAdminList(HttpServletRequest request, DatabaseConnection db) throws SQLException {
        String query = "SELECT id_user, username, nama_user FROM users";
        List<Admin> adminList = new ArrayList<>();

        try (PreparedStatement stmt = db.getConnection().prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id_user"));
                admin.setUsername(rs.getString("username"));
                admin.setNama(rs.getString("nama_user"));
                adminList.add(admin);
            }
        } catch (SQLException e) {
            System.out.println("Error saat mengambil daftar admin: " + e.getMessage());
            e.printStackTrace();
        }

        // Set data yang diambil ke atribut request
        request.setAttribute("adminList", adminList);
    }

    private void handleAddAdmin(HttpServletRequest request, DatabaseConnection db, String username, String nama_user, String password)
            throws SQLException {
        String query = "INSERT INTO users (username, nama_user, password) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = db.getConnection().prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, nama_user);
            stmt.setString(3, password);
            stmt.executeUpdate();

            request.setAttribute("success", "Admin berhasil ditambahkan.");
        } catch (SQLException e) {
            request.setAttribute("error", "Gagal menambahkan admin: " + e.getMessage());
        }
    }

    private void handleUpdateAdmin(HttpServletRequest request, DatabaseConnection db, int id_user, String username, String nama_user, String password)
        throws SQLException {
    // Ambil password lama dari database
    String oldPassword = request.getParameter("old_password");
    String getPasswordQuery = "SELECT password FROM users WHERE id_user = ?";
    String updateQuery = null;

    try (PreparedStatement getPasswordStmt = db.getConnection().prepareStatement(getPasswordQuery)) {
        getPasswordStmt.setInt(1, id_user);
        try (ResultSet rs = getPasswordStmt.executeQuery()) {
            if (rs.next()) {
                String currentPassword = rs.getString("password");

                // Validasi jika password baru diisi
                if (password != null && !password.isEmpty() && (oldPassword == null || !oldPassword.equals(currentPassword))) {
                    request.setAttribute("error", "Password lama tidak sesuai,silahkan coba lagi");
                    return;
                }

                // Tentukan query update berdasarkan input pengguna
                if ((password == null || password.isEmpty()) && username != null && nama_user != null) {
                    // Update username dan nama_user
                    updateQuery = "UPDATE users SET username = ?, nama_user = ? WHERE id_user = ?";
                } else if (password != null && !password.isEmpty()) {
                    // Update password saja (validasi password lama sudah dilakukan)
                    updateQuery = "UPDATE users SET password = ? WHERE id_user = ?";
                } else if (username != null && !username.isEmpty()) {
                    // Update username saja
                    updateQuery = "UPDATE users SET username = ? WHERE id_user = ?";
                } else if (nama_user != null && !nama_user.isEmpty()) {
                    // Update nama_user saja
                    updateQuery = "UPDATE users SET nama_user = ? WHERE id_user = ?";
                } else {
                    request.setAttribute("error", "Tidak ada data yang diperbarui.");
                    return;
                }
            } else {
                request.setAttribute("error", "Data admin tidak ditemukan.");
                return;
            }
        }
    }

    // Eksekusi query update
    try (PreparedStatement updateStmt = db.getConnection().prepareStatement(updateQuery)) {
        if (updateQuery.contains("username") && updateQuery.contains("nama_user")) {
            updateStmt.setString(1, username);
            updateStmt.setString(2, nama_user);
            updateStmt.setInt(3, id_user);
        } else if (updateQuery.contains("password")) {
            updateStmt.setString(1, password);
            updateStmt.setInt(2, id_user);
        } else if (updateQuery.contains("username")) {
            updateStmt.setString(1, username);
            updateStmt.setInt(2, id_user);
        } else if (updateQuery.contains("nama_user")) {
            updateStmt.setString(1, nama_user);
            updateStmt.setInt(2, id_user);
        }

        updateStmt.executeUpdate();
        request.setAttribute("success", "Data admin berhasil diperbarui.");
    } catch (SQLException e) {
        request.setAttribute("error", "Gagal memperbarui data admin: " + e.getMessage());
    }
}

    private void handleDeleteAdmin(HttpServletRequest request, DatabaseConnection db, int id_user)
            throws SQLException {
        String query = "DELETE FROM users WHERE id_user = ?";

        try (PreparedStatement stmt = db.getConnection().prepareStatement(query)) {
            stmt.setInt(1, id_user);
            stmt.executeUpdate();

            request.setAttribute("success", "Admin berhasil dihapus.");
        } catch (SQLException e) {
            request.setAttribute("error", "Gagal menghapus admin: " + e.getMessage());
        }
    }

    private void forwardToPage(HttpServletRequest request, HttpServletResponse response, String page)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }
}