<%@page import="java.util.List"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="models.Mobil" %>
<!DOCTYPE html>
<html>
<head>
    <title>Data Mobil</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #e6f7ff;
            margin: 0;
            padding: 20px;
        }
        h1 {
            color: #004080;
        }
        .container {
            display: flex;
            flex-wrap: wrap;
        }
        .form-container {
            width: 45%;
            padding: 20px;
            background-color: #ffffff;
            border: 1px solid #cce7ff;
            border-radius: 5px;
            margin-right: 5%;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .form-container label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #004080;
        }
        .form-container input, .form-container select {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #99ccff;
            border-radius: 4px;
            background-color: #f2faff;
        }
        .form-container button {
            padding: 10px 20px;
            background-color: #004080;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .form-container button:hover {
            background-color: #003366;
        }
        .table-container {
            width: 50%;
            overflow-x: auto;
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
        .back-button {
            display: inline-block;
            margin-bottom: 20px;
            padding: 10px 20px;
            background-color: #004080;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .back-button:hover {
            background-color: #003366;
        }
    </style>
</head>
<body>
    
    <a href="<%= request.getContextPath() %>/pages/Menu.jsp" class="back-button">BACK</a>
    <h1>Data Mobil</h1>

    <div class="form-container">
        <form action="${pageContext.request.contextPath}/MobilController" method="POST">
            <input type="hidden" name="action" value="add">
            <label for="merk">Merk Mobil</label>
            <input type="text" id="merk" name="merk" placeholder="Masukkan merk mobil">

            <label for="no_plat">No. PLAT</label>
            <input type="text" id="no_plat" name="no_plat" placeholder="Masukkan nomor plat">

            <label for="tahun">Tahun</label>
            <input type="text" id="tahun" name="tahun" placeholder="Masukkan tahun pembuatan">

            <label for="harga">Harga Sewa</label>
            <input type="text" id="harga" name="harga" placeholder="Masukkan harga sewa">

            <label for="status">Status Mobil</label>
            <select id="status" name="status">
                <option value="Tersedia">Tersedia</option>
                <option value="Tidak Tersedia">Tidak Tersedia</option>
            </select>

            <button type="submit">Simpan</button>
        </form>
    </div>

    <table>
        <thead>
            <tr>
                <th>ID Mobil</th>
                <th>Merk</th>
                <th>No. PLAT</th>
                <th>Tahun</th>
                <th>Harga Sewa</th>
                <th>Status</th>
                <th> Aksi </th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<Mobil> mobilList = (List<Mobil>) request.getAttribute("mobilList");
                if (mobilList != null && !mobilList.isEmpty()) {
                    for (Mobil mobil : mobilList) {
            %>
            <tr>
                <td><%= mobil.getIdMobil() %></td>
                <td><%= mobil.getMerk() %></td>
                <td><%= mobil.getNoPlat() %></td>
                <td><%= mobil.getTahun() %></td>
                <td><%= mobil.getHarga() %></td>
                <td><%= mobil.getStatus() %></td>
                <td> <div class="action-buttons">
                        <a href="pages/Edit.jsp?id=<%= mobil.getIdMobil() %>">Edit</a>
                        <form action="${pageContext.request.contextPath}/MobilController" method="POST" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="<%= mobil.getIdMobil() %>">
                            <button type="submit" onclick="return confirm('Apakah Anda yakin ingin menghapus data ini?');">Delete</button>
                        </form>
                    </div>
                </td>
            </tr>
            <% 
                    }
                } else { 
            %>
            <tr>
                <td colspan="6">Tidak ada data mobil.</td>
            </tr>
            <% } %>
        </tbody>
    </table>
</body>
</html>