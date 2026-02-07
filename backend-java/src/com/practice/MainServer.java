package com.practice;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.net.InetSocketAddress;

public class MainServer {

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);

            server.createContext("/test", new TestHandler());
            server.createContext("/salaries", new SalaryHandler());
            server.createContext("/employees", new com.practice.EmployeeAPI());
            server.createContext("/add-employee", new com.practice.AddEmployeeAPI());
            server.createContext("/update-employee", new UpdateEmployeeAPI());
            server.createContext("/delete-employee", new DeleteEmployeeAPI());



     
            server.start();

            System.out.println("Server started at http://localhost:9090");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/* ---------- TEST API ---------- */
class TestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String response = "Backend is working";

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, response.length());

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/* ---------- SALARY API (JSON) ---------- */
class SalaryHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String jsonResponse = "[10000, 20000, 30000, 40000, 50000]";

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            exchange.sendResponseHeaders(200, jsonResponse.length());

            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
