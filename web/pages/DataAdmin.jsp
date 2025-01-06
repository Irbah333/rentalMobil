<%@ page import="models.Admin" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <meta charset="UTF-8">
    <title>Data Admin</title>
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
        .btn-update {
            background-color: green;
            color: white;
        }
        .btn-update:hover {
            background-color: darkgreen;
        }
        .btn-delete {
            background-color: #DC3545;
            color: white;
        }
        .btn-delete:hover {
            background-color: darkred;
        }
        .btn-add {
            background-color: #00509e;
            color: white;
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
        .table-container {
            width: 100%;
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
        .message {
            margin-top: 10px;
            font-weight: bold;
        }
        .message.success {
            color: green;
            font-size: 20px;
        }
        .message.error {
            color: red;
            font-size: 20px;
        }
    </style>
</head>
<body>
    <a href="<%= request.getContextPath() %>/pages/Menu.jsp" class="back-button">BACK</a>
    <h1>Data Admin</h1>
    <div class="container">
        <!-- Form untuk Tambah Admin -->
        <form action="${pageContext.request.contextPath}/AdminController" method="post">
            <input type="hidden" name="action" value="addAdmin">

            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" placeholder="Masukkan username" required>
            </div>
            <div class="form-group">
                <label for="nama_user">Nama</label>
                <input type="text" id="nama_user" name="nama_user" placeholder="Masukkan nama" required>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Masukkan Password" required>
            </div>
            <button type="submit" class="btn-add">Tambah Admin</button>
        </form>

        <!-- Pesan Sukses atau Error -->
        <% if (request.getAttribute("success") != null) { %>
            <p class="message success"><%= request.getAttribute("success") %></p>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <p class="message error"><%= request.getAttribute("error") %></p>
        <% } %>

        <!-- Tabel Daftar Admin -->
        <div class="table-container">
            <h3>Daftar Admin</h3>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Nama</th>
                        <th>Aksi</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Admin> adminList = (List<Admin>) request.getAttribute("adminList");
                        if (adminList != null && !adminList.isEmpty()) {
                            for (Admin admin : adminList) {
                    %>
                    <tr>
                        <td><%= admin.getId() %></td>
                        <td><%= admin.getUsername() %></td>
                        <td><%= admin.getNama() %></td>
                        <td>
                            <!-- Tombol untuk Edit Admin -->
                            <a href="${pageContext.request.contextPath}/AdminController?action=editAdmin&id_user=<%= admin.getId() %>" class="btn-update">Edit</a>

                            <!-- Form untuk Delete Admin -->
                            <form action="${pageContext.request.contextPath}/AdminController" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="deleteAdmin">
                                <input type="hidden" name="id_user" value="<%= admin.getId() %>">
                                <button type="submit" class="btn-delete" onclick="return confirm('Apakah Anda yakin ingin menghapus admin ini?');">Hapus</button>
                            </form>
                        </td>
                    </tr>
                    <% 
                            }
                        } else { 
                    %>
                    <tr>
                        <td colspan="4" style="text-align:center;">Tidak ada admin yang terdaftar.</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>