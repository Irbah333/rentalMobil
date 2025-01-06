<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="classes.DatabaseConnection" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Mobil</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #e6f7ff;
            margin: 0;
            padding: 20px;
        }
        .form-container {
            width: 50%;
            max-width: 600px;
            padding: 20px;
            background-color: #ffffff;
            border: 1px solid #cce7ff;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            margin: auto;
        }
        .form-container label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .form-container input, .form-container select {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #99ccff;
            border-radius: 4px;
            box-sizing: border-box;
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
    <script>
        function validateForm() {
            const inputs = document.querySelectorAll("input[required], select[required]");
            for (const input of inputs) {
                if (!input.value.trim()) {
                    alert("Semua field harus diisi!");
                    return false;
                }
            }
            return true;
        }
    </script>
</head>
<body>

    <a href="${pageContext.request.contextPath}/MobilController" class="back-button">Kembali ke Data Mobil</a>
    <div class="form-container">
        <h1>Edit Data Mobil</h1>
        <form action="${pageContext.request.contextPath}/MobilController" method="POST" onsubmit="return validateForm();">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="${param.id}">

            <label for="merk">Merk Mobil</label>
            <input type="text" id="merk" name="merk" value="${merk}" required>

            <label for="no_plat">No. PLAT</label>
            <input type="text" id="no_plat" name="no_plat" value="${noPlat}" required>

            <label for="tahun">Tahun</label>
            <input type="number" id="tahun" name="tahun" value="${tahun}" required min="1900" max="<%= java.time.Year.now().getValue() %>">

            <label for="harga">Harga Sewa</label>
            <input type="number" id="harga" name="harga" value="${hargaSewa}" required min="0">

            <label for="status">Status Mobil</label>
            <select id="status" name="status" required>
                <option value="Tersedia" ${status == 'Tersedia' ? 'selected' : ''}>Tersedia</option>
                <option value="Tidak Tersedia" ${status == 'Tidak Tersedia' ? 'selected' : ''}>Tidak Tersedia</option>
            </select>

            <button type="submit">Simpan Perubahan</button>
        </form>

        <form action="${pageContext.request.contextPath}/MobilController" method="POST" style="margin-top: 20px;">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="id" value="${param.id}">
            <button type="submit" onclick="return confirm('Apakah Anda yakin ingin menghapus data ini?');" style="background-color: #DC3545;">Hapus Data</button>
        </form>
    </div>
</body>
</html>