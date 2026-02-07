package com.practice;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteEmployeeAPI implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        try {
            // ---- CORS headers ----
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            // ---- Handle preflight ----
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            // Only POST method allowed
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            // Read request body
            InputStream is = exchange.getRequestBody();
            StringBuilder body = new StringBuilder();
            int ch;
            while ((ch = is.read()) != -1) {
                body.append((char) ch);
            }
            String data = body.toString();

            // Example JSON: {"id":2}
            data = data.replaceAll("[{}\"]", "");
            String[] parts = data.split(":");
            int id = Integer.parseInt(parts[1].trim());

            // Delete from database
            Connection con = DBConnection.getConnection();
            String sql = "DELETE FROM employee WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            String response = (rows > 0) ? "Employee deleted successfully" : "Employee not found";

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
