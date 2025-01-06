package classes;
import java.sql.*;

public class DatabaseConnection {
    public Connection con;
    public Statement stmt;
    public boolean isConnected;
    public String message;

    public DatabaseConnection() {
       
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Gunakan driver MySQL terbaru
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/datarent", "root", ""); // Database datarent
            stmt = con.createStatement();
            isConnected = true;
            message = "DB connected successfully";
        } catch (ClassNotFoundException | SQLException e) {
            isConnected = false;
            message = "Connection failed: " + e.getMessage();
        }
    }

    // Menjalankan query (INSERT, UPDATE, DELETE)
    public void runQuery(String query) {
        try {
            int result = stmt.executeUpdate(query);
            message = "Info: " + result + " rows affected";
        } catch (SQLException e) {
            message = "Error executing query: " + e.getMessage();
        }
    }

    // Menjalankan query SELECT
    public ResultSet runSelectQuery(String query) {
        try {
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            message = "Error executing select query: " + e.getMessage();
            return null;
        }
    }

    // Menutup koneksi
    public void disconnect() {
        try {
            if (stmt != null) stmt.close();
            if (con != null) con.close();
            message = "DB disconnected successfully";
        } catch (SQLException e) {
            message = "Error disconnecting: " + e.getMessage();
        }
    }

    // Getter untuk Connection jika diperlukan
    public Connection getConnection() {
        return con;
    }
}