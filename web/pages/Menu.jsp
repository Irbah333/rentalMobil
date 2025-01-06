<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RentCar - Menu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: url('../img/Bg.png') no-repeat center center fixed;
            background-size: cover;
            color: #fff;
            font-family: Arial, sans-serif;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            margin: 0;
        }
        .header {
            background: linear-gradient(90deg, #002b5c, #00509e);
            color: white;
            padding: 20px 0;
            text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.5);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }
        .main-container {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .menu-container {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
            padding: 40px;
            text-align: center;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            margin-top: -50px; 
        }
        .menu-container:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.4);
        }
        .menu-container h3 {
            font-weight: bold;
            color: #003366;
            font-size: 28px; 
            border-bottom: 2px solid #003366; 
            display: inline-block;
            padding-bottom: 5px;
        }
        .menu-container ul {
            list-style-type: none;
            padding: 0;
        }
        .menu-container ul li {
            margin-bottom: 20px;
        }
        .menu-container ul li a {
            text-decoration: none;
            color: #00509e;
            font-weight: bold;
            font-size: 20px; 
            padding: 10px 15px;
            border-radius: 8px;
            transition: background 0.3s ease, color 0.3s ease;
        }
        .menu-container ul li a:hover {
            background: #00509e;
            color: #fff;
            text-shadow: 0 1px 3px rgba(0, 0, 0, 0.4);
        }
        .footer {
            text-align: center;
            padding: 15px 0;
            background: rgba(0, 43, 92, 0.8);
            color: white;
            border-radius: 10px 10px 0 0;
            font-size: 14px;
        }
    </style>
</head>
<body>

    <header class="header text-center">
        <h1>Welcome to RentCar</h1>
    </header>

    <main class="main-container">
        <div class="menu-container col-md-6">
            <h3 class="text-center mb-4">Menu Utama</h3>
            <ul>
                <li><a href="${pageContext.request.contextPath}/AdminController">Data Admin</a></li>
                <li><a href="${pageContext.request.contextPath}/MobilController">Data Mobil</a></li>
                <li><a href="Peminjaman.jsp">Peminjaman</a></li>
                <li><a href="${pageContext.request.contextPath}/PengembalianController">Pengembalian</a></li>
                <li><a href="${pageContext.request.contextPath}/RiwayatController">Riwayat</a></li>
                <li><a href="${pageContext.request.contextPath}/LogoutController">Logout</a></li>
            </ul>
        </div>
    </main>

    <footer class="footer">
        <p>&copy; 2025 RentCar. All Rights Reserved.</p>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
