<%@ page import="models.PeminjamanModel" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <meta charset="UTF-8">
    <title>Data Sewa</title>
    <style>
    body {
        font-family: Arial, sans-serif;
        background-color: #e6f7ff;
        padding: 20px;
    }
    h1 {
        color: #004080;
    }
    .container {
        background-color: #ffffff;
        padding: 20px;
        border-radius: 5px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }
    .form-group {
        margin-bottom: 15px;
    }
    .form-group label {
        display: block;
        font-weight: bold;
        color: #004080;
    }
    .form-group input {
        width: 95%; 
        padding: 10px;
        margin-top: 5px;
        margin-right: 90px; 
        border: 1px solid #99ccff;
        border-radius: 4px;
        box-sizing: border-box; 
    }
    button {
        padding: 10px 20px;
        border: none;
        border-radius: 5px;
        font-size: 16px;
        cursor: pointer;
    }
    .btn-submit {
        background-color: #007BFF;
        color: white;
    }
    .btn-submit:hover {
        background-color: #0056b3;
    }
    .back-button {
        display: inline-block;
        margin-bottom: 20px;
        padding: 10px 20px;
        background-color: #004080;
        color: white;
        text-decoration: none;
        border-radius: 4px;
    }
    .table-container {
        margin-top: 20px;
    }
    table {
        border-collapse: collapse;
        width: 100%;
        margin-top: 20px;
    }
    table, th, td {
        border: 1px solid #cce7ff;
    }
    th, td {
        padding: 8px;
        text-align: left;
    }
    th {
        background-color: #cce7ff;
        color: #004080;
    }
    td {
        background-color: #f2faff;
    }
    </style>
</head>
<%
    if (request.getAttribute("daftarMobilTersedia") == null) {
        response.sendRedirect(request.getContextPath() + "/PeminjamanController");
        return;
    }
%>

<body>
    <a href="<%= request.getContextPath() %>/pages/Menu.jsp" class="back-button">BACK</a>
    <h1>Data Sewa</h1>

    <div class="container">
        <!-- Form Input Peminjaman -->
        <form action="${pageContext.request.contextPath}/PeminjamanController" method="POST">
            <div class="form-group">
                <label for="nama">Nama Penyewa:</label>
                <input type="text" id="nama" name="nama" placeholder="Masukkan nama penyewa" required>
            </div>
            <div class="form-group">
                <label for="telepon">Telepon:</label>
                <input type="text" id="telepon" name="telepon" placeholder="+62" required>
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" placeholder="Masukkan email penyewa" required>
            </div>
            <div class="form-group">
                <label for="alamat">Alamat:</label>
                <input type="text" id="alamat" name="alamat" placeholder="Masukkan alamat penyewa" required>
            </div>
            <div class="form-group">
                <label for="plat">Plat Mobil:</label>
                <input type="text" id="plat" name="plat" placeholder="Masukkan plat nomor mobil" required>
            </div>
            <div class="form-group">
                <label for="lama">Lama Peminjaman (Hari):</label>
                <input type="number" id="lama" name="lama" placeholder="Masukkan lama hari peminjaman" required>
            </div>
            <div class="form-group">
                <label for="tanggal">Tanggal Pinjam:</label>
                <input type="date" id="tanggal" name="tanggal" required>
            </div>
            <button type="submit" class="btn-submit">Simpan</button>
        </form>
    </div>

    <div class="table-container">
        <h3>Mobil Tersedia</h3>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Plat</th>
                    <th>Nama Mobil</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    List<PeminjamanModel> daftarMobilTersedia = (List<PeminjamanModel>) request.getAttribute("daftarMobilTersedia");
                    if (daftarMobilTersedia != null && !daftarMobilTersedia.isEmpty()) {
                        for (PeminjamanModel mobil : daftarMobilTersedia) { 
                %>
                <tr>
                    <td><%= mobil.getIdMobil() %></td>
                    <td><%= mobil.getNoPlat() %></td>
                    <td><%= mobil.getNamaMobil() %></td>
                </tr>
                <% 
                        }
                    } else { 
                %>
                <tr>
                    <td colspan="3">Tidak ada mobil yang tersedia.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <% if (request.getAttribute("success") != null) { %>
        <p style="color: green;"><%= request.getAttribute("success") %></p>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>
</body>
</html>