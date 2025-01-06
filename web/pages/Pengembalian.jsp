<%@ page import="models.PengembalianModel" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <meta charset="UTF-8">
    <title>Pengembalian Mobil</title>
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
        margin-top: 15px;
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
    
    .form-group-inline {
        display: flex;
        justify-content: space-between;
        gap: 20px; 
    }

    .form-group-inline .form-group {
        flex: 1; 
    }

    button {
        padding: 10px 20px;
        border: none;
        border-radius: 5px;
        font-size: 16px;
        cursor: pointer;
    }
    
    .btn-search {
        background-color: #007BFF;
        color: white;
    }
    .btn-search:hover {
        background-color: #0056b3;
    }
    
    .btn-update {
        background-color: #DC3545;
        color: white;
    }
    .btn-update:hover {
        background-color: #b02a37;
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
    .input-group {
        position: relative;
        display: flex;
        align-items: center;
    }
    .input-group input {
        padding-right: 40px;
        box-sizing: border-box;
        width: 100%;
    }
    .input-group .calendar-icon {
        position: absolute;
        right: 100px; 
        top: 50%;
        transform: translateY(-50%);
        color: #004080;
        pointer-events: none;
        font-size: 1.2em;
    }
</style>

</head>

<body>
    <a href="<%= request.getContextPath() %>/pages/Menu.jsp"class="back-button">BACK</a>
    <h1>Pengembalian Mobil</h1>
    <div class="container">
        <!-- Form untuk Search -->
        <form action="${pageContext.request.contextPath}/PengembalianController" method="post">
            <input type="hidden" name="action" value="getTransaction">
            <div class="form-group">
                <label for="idTransaksi">ID Transaksi</label>
                <input type="text" id="idTransaksi" name="idTransaksi" placeholder="Masukkan ID Transaksi"
                       value="<%= request.getAttribute("idTransaksi") != null ? request.getAttribute("idTransaksi") : "" %>">
                <button type="submit" class="btn-search" >Search</button>
            </div>
        </form>

        <!-- Data Transaksi -->
        <form action="${pageContext.request.contextPath}/PengembalianController" method="post">
            <input type="hidden" name="action" value="calculateAndUpdate">
            <input type="hidden" name="idTransaksi" value="<%= request.getAttribute("idTransaksi") != null ? request.getAttribute("idTransaksi") : "" %>">

            <div class="form-group">
                <label for="nama">Nama</label>
                <input type="text" id="nama" name="nama" 
                       value="<%= request.getAttribute("nama") != null ? request.getAttribute("nama") : "" %>" readonly>
            </div>
            <div class="form-group">
                <label for="noPlat">No. PLAT</label>
                <input type="text" id="noPlat" name="noPlat"
                       value="<%= request.getAttribute("no_plat") != null ? request.getAttribute("no_plat") : "" %>" readonly>
            </div>
            <div class="form-group">
                <label for="tglPinjam">Tanggal Pinjam</label>
                <input type="text" id="tglPinjam" name="tglPinjam"
                       value="<%= request.getAttribute("tgl_pinjam") != null ? request.getAttribute("tgl_pinjam") : "" %>" readonly>
            </div>
            <div class="form-group">
                <label for="lama">Lama</label>
                <input type="text" id="lama" name="lama"
                       value="<%= request.getAttribute("lama") != null ? request.getAttribute("lama") : "" %>" readonly>
            </div>
            
            <div class="form-group">
                <label for="tanggalKembali">Tanggal Kembali</label>
                <div class="input-group">
                <input type="date" id="tanggalKembali" name="tanggalKembali" placeholder="YYYY-MM-DD" required>
                <i class="fa-solid fa-calendar-days calendar-icon"></i>
            </div>
            <script>
                flatpickr("#tanggalKembali", {
                    dateFormat: "Y-m-d", 
                    allowInput: true
                });
            </script>
            <button type="submit" class="btn-update">Hitung & Update</button>
            <div class="form-group-inline">
                <div class="form-group">
                    <label for="denda">Denda</label>
                    <input type="text" id="denda" name="denda"
                           value="<%= request.getAttribute("denda") != null ? request.getAttribute("denda") : "" %>" readonly>
                    <small class="form-text text-muted">*Denda 50000/hari</small>
                </div>

                <div class="form-group">
                    <label for="totalHarga">Total Harga</label>
                    <input type="text" id="totalHarga" name="totalHarga"
                           value="<%= request.getAttribute("total_harga") != null ? request.getAttribute("total_harga") : "" %>" readonly>
                </div>
            </div>
        </form>
    </div>
 
<h3>Daftar Transaksi Belum Dikembalikan</h3>
<table border="1" cellspacing="0" cellpadding="8" style="width:100%; border-collapse:collapse;">
    <thead>
        <tr>
            <th>ID Transaksi</th>
            <th>Merk Mobil</th>
            <th>No. PLAT</th>
            <th>Peminjam</th>
            <th>Harga</th>
            <th>Tanggal Pinjam</th>
            <th>Lama (Hari)</th>
        </tr>
    </thead>
    <tbody>
        
        <% 
            // Menampilkan data transaksi yang belum dikembalikan
            List<PengembalianModel> transaksiList = (List<PengembalianModel>) request.getAttribute("unreturnedTransactions");
            if (transaksiList != null && !transaksiList.isEmpty()) {
                for (PengembalianModel transaksi : transaksiList) { 
        %>
            <tr>
                <td><%= transaksi.getIdTransaksi() %></td>
                <td><%= transaksi.getMerk() %></td>
                <td><%= transaksi.getNoPlat() %></td>
                <td><%= transaksi.getNama() %></td>
                <td><%= transaksi.getHarga() %></td>
                <td><%= transaksi.getTglPinjam() %></td>
                <td><%= transaksi.getLama() %></td>
            </tr>
        <% 
                }
            } else { 
        %>
            <tr>
                <td colspan="7" style="text-align:center;">Tidak ada transaksi yang belum dikembalikan.</td>
            </tr>
        <% 
            } 
        %>
    </tbody>
</table>


    <%
        if (request.getAttribute("success") != null) {
    %>
        <p style="color: green;"><%= request.getAttribute("success") %></p>
    <%
        }
        if (request.getAttribute("error") != null) {
    %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
    <%
        }
    %>
</body>
</html>