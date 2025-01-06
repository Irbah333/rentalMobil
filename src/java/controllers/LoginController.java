package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import classes.DatabaseConnection;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Cek kredensial di database
            if (validateUser(username, password)) {
                response.sendRedirect("pages/Menu.jsp"); // Jika login berhasil, redirect ke halaman home
            } else {
                response.sendRedirect("index.jsp?error=invalid"); // Jika login gagal, tampilkan pesan error
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("index.jsp?error=error"); // Jika terjadi kesalahan, tampilkan error
        }
    }

    private boolean validateUser(String username, String password) throws SQLException {
        boolean isValid = false;
        
        // Gunakan kelas DatabaseConnection untuk mendapatkan koneksi
        try (Connection connection = new DatabaseConnection().getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isValid = true;
            }

            resultSet.close();
            statement.close();
        }

        return isValid;
    }
}