<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Riwayat Transaksi</title>
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
        .table-container {
            width: 100%;
            overflow-x: auto;
            margin-top: 20px;
        }
        table {
            border-collapse: collapse;
            width: 100%;
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
    <h1>Riwayat Transaksi</h1>
    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th>ID-Transaksi</th>
                    <th>Merk Mobil</th>
                    <th>No. PLAT</th>
                    <th>Peminjam</th>
                    <th>Harga Mobil</th>
                    <th>Tanggal Pinjam</th>
                    <th>Lama</th>
                    <th>Tanggal Kembali</th>
                    <th>Denda</th>
                    <th>Total Harga</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    List<models.PengembalianModel> transaksiList = (List<models.PengembalianModel>) request.getAttribute("transaksiList");
                    if (transaksiList != null && !transaksiList.isEmpty()) {
                        for (models.PengembalianModel transaksi : transaksiList) {
                %>
                    <tr>
                        <td><%= transaksi.getIdTransaksi() %></td>
                        <td><%= transaksi.getMerk() %></td>
                        <td><%= transaksi.getNoPlat() %></td>
                        <td><%= transaksi.getNama() %></td>
                        <td><%= transaksi.getHarga() %></td>
                        <td><%= transaksi.getTglPinjam() %></td>
                        <td><%= transaksi.getLama() %></td>
                        <td><%= transaksi.getTglKembali() %></td>
                        <td><%= transaksi.getDenda() %></td>
                        <td><%= transaksi.getTotal_harga() %></td>
                    </tr>
                <% 
                        }
                    } else { 
                %>
                    <tr>
                        <td colspan="9" style="text-align: center;">Tidak ada data transaksi</td>
                    </tr>
                <% 
                    } 
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
