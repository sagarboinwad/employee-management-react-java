package com.practice;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.practice.DBConnection;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class EmployeeAPI implements HttpHandler {
	
	 @Override
	    public void handle(HttpExchange exchange) {
	        try {
	            Connection con = DBConnection.getConnection();
	            Statement st = con.createStatement();

	            ResultSet rs = st.executeQuery("SELECT id, name, salary FROM employee");

	            StringBuilder json = new StringBuilder();
	            json.append("[");

	            boolean first = true;
	            while (rs.next()) {
	                if (!first) json.append(",");
	                json.append("{")
	                    .append("\"id\":").append(rs.getInt("id")).append(",")
	                    .append("\"name\":\"").append(rs.getString("name")).append("\",")
	                    .append("\"salary\":").append(rs.getInt("salary"))
	                    .append("}");
	                first = false;
	            }

	            json.append("]");

	            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
	            exchange.getResponseHeaders().add("Content-Type", "application/json");

	            exchange.sendResponseHeaders(200, json.length());
	            OutputStream os = exchange.getResponseBody();
	            os.write(json.toString().getBytes());
	            os.close();

	            rs.close();
	            st.close();
	            con.close();

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	

}
