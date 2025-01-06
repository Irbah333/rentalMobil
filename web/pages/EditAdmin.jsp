<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="models.Admin" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Data Admin</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #e6f7ff;
            margin: 0;
            padding: 20px;
        }
        .form-container {
            width: 50%;
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
        .form-container input {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #99ccff;
            border-radius: 4px;
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
</head>
<body>
    <a href="${pageContext.request.contextPath}/AdminController" class="back-button">Kembali ke Data Admin</a>
    <div class="form-container">
        <h1>Edit Data Admin</h1>

        <!-- Form untuk Update Data Admin -->
        <form action="${pageContext.request.contextPath}/AdminController" method="post">
            <input type="hidden" name="action" value="updateAdmin">
            <input type="hidden" name="id_user" value="${admin.id}">

            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" value="${admin.username}" placeholder="Masukkan username baru">
            </div>

            <div class="form-group">
                <label for="nama_user">Nama</label>
                <input type="text" id="nama_user" name="nama_user" value="${admin.nama}" placeholder="Masukkan nama baru">
            </div>

            <div class="form-group">
                <label for="old_password">Password Lama</label>
                <input type="password" id="old_password" name="old_password" placeholder="Masukkan password lama jika ingin mengganti password">
            </div>

            <div class="form-group">
                <label for="password">Password Baru</label>
                <input type="password" id="password" name="password" placeholder="Masukkan password baru (opsional)">
            </div>

            <button type="submit" class="btn-primary">Simpan Perubahan</button>
        </form>
    </div>
</body>
</html>