
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RentCar - Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: url('img/Bg.png') no-repeat center center fixed;
            background-color: #f5f5dc; /* Warna cream */
            color: #002b5c; /* Warna biru gelap */
        }
        .header {
            background-color: #002b5c;
            color: white;
            padding: 20px 0;
        }
        .login-container {
            margin-top: 50px;
            background-color: white;
            border-radius: 10px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            padding: 30px;
        }
        .btn-primary {
            background-color: #002b5c;
            border-color: #002b5c;
        }
        .btn-primary:hover {
            background-color: #004080;
        }
       
    </style>
</head>
<body>
    <header class="header text-center">
        <h1>RentCar</h1>
        <p>Menyediakan solusi penyewaan mobil yang nyaman dan terpercaya</p>
    </header>

    <main class="container d-flex justify-content-center">
        <div class="login-container col-md-6">
            <h2 class="text-center mb-4">Login</h2>
            <form action="LoginController" method="post">
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name="username" placeholder="Masukkan username Anda" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Masukkan password Anda" required>
                </div>
                <div class="d-grid">
                    <button type="submit" class="btn btn-primary">Login</button>
                </div>
            </form>

            <%-- Menampilkan pesan error jika login gagal --%>
            <%
                String error = request.getParameter("error");
                if (error != null) {
                    if (error.equals("invalid")) {
            %>
            <p style="color: red;" class="mt-3">Invalid username or password!</p>
            <%
                    } else if (error.equals("error")) {
            %>
            <p style="color: red;" class="mt-3">There was an error while processing your request.</p>
            <%
                    }
                }
            %>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>