package com.practice;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.practice.DBConnection;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddEmployeeAPI implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        try {
            // ---- CORS headers ----
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            // ---- Handle preflight OPTIONS request ----
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1); // No content
                return;
            }

            // ---- Only allow POST for adding employee ----
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }

            // ---- Read request body ----
            InputStream is = exchange.getRequestBody();
            StringBuilder body = new StringBuilder();
            int ch;
            while ((ch = is.read()) != -1) {
                body.append((char) ch);
            }
            String data = body.toString();

            // data is JSON like: {"id":2,"name":"Alice","salary":20000}
            data = data.replaceAll("[{}\"]", ""); // remove { } and "
            String[] parts = data.split(",");
            int id = Integer.parseInt(parts[0].split(":")[1].trim());
            String name = parts[1].split(":")[1].trim();
            int salary = Integer.parseInt(parts[2].split(":")[1].trim());

            // ---- Insert into database ----
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO employee(id, name, salary) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, salary);
            ps.executeUpdate();

            String response = "Employee added successfully";

            // ---- Send response ----
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
