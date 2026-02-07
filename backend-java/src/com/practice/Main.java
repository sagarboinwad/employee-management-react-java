package com.practice;

import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
	
	static void validateEmployee(int id, String name, int salary)
	        throws InvalidInputException {

	    if (id <= 0) {
	        throw new InvalidInputException("ID must be positive");
	    }

	    if (name == null || name.trim().isEmpty()) {
	        throw new InvalidInputException("Name cannot be empty");
	    }

	    if (salary <= 0) {
	        throw new InvalidInputException("Salary must be greater than zero");
	    }
	}

	
	
    public static void main(String[] args) {
      
		Scanner sc = new Scanner(System.in);
		
		
		
		
		while(true) {
			System.out.println("\n1. Add Employee");
			System.out.println("2. View Employees");
			System.out.println("3. Update Salary");
			System.out.println("4. Delete Employee");
			System.out.println("5. Exit");
			System.out.print("Choose option: ");

			
			int choice = sc.nextInt();
			
			if(choice == 1) {
				try {
				    System.out.print("Enter ID: ");
				    int id = sc.nextInt();

				    System.out.print("Enter Name: ");
				    String name = sc.next();

				    System.out.print("Enter Salary: ");
				    int salary = sc.nextInt();

				    validateEmployee(id, name, salary);

				    Connection con = DBConnection.getConnection();
				    String sql = "INSERT INTO employee VALUES (?, ?, ?)";
				    PreparedStatement ps = con.prepareStatement(sql);

				    ps.setInt(1, id);
				    ps.setString(2, name);
				    ps.setInt(3, salary);

				    ps.executeUpdate();
				    System.out.println("Employee added");

				    con.close();

				 } catch (InvalidInputException e) {
				    System.out.println("Validation Error: " + e.getMessage());
				   } catch (Exception e) {
				        e.printStackTrace();
				     }
				


			
			}else if(choice == 2) {
				 
				try {
					Connection con = DBConnection.getConnection();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM employee");

                    while (rs.next()) {
                        System.out.println(
                            rs.getInt("id") + " " +
                            rs.getString("name") + " " +
                            rs.getInt("salary")
                         );
                     }

                      con.close();
                  } catch (Exception e) {
                       e.printStackTrace();
                    }
				
				}else if (choice == 3) {

				    System.out.print("Enter Employee ID: ");
				    int id = sc.nextInt();

				    System.out.print("Enter New Salary: ");
				    int salary = sc.nextInt();

				    try {
				        Connection con = DBConnection.getConnection();
				        String sql = "UPDATE employee SET salary=? WHERE id=?";
				        PreparedStatement ps = con.prepareStatement(sql);

				        ps.setInt(1, salary);
				        ps.setInt(2, id);

				        int rows = ps.executeUpdate();

				        if (rows > 0) {
				            System.out.println("Salary updated successfully");
				        } else {
				            System.out.println("Employee not found");
				        }

				        con.close();
				     } catch (Exception e) {
				        e.printStackTrace();
				     }
				 }else if (choice == 4) {

					    System.out.print("Enter Employee ID to delete: ");
					    int id = sc.nextInt();

					    try {
					        Connection con = DBConnection.getConnection();
					        String sql = "DELETE FROM employee WHERE id=?";
					        PreparedStatement ps = con.prepareStatement(sql);

					        ps.setInt(1, id);

					        int rows = ps.executeUpdate();

					        if (rows > 0) {
					            System.out.println("Employee deleted");
					        } else {
					            System.out.println("Employee not found");
					        }

					        con.close();
					    } catch (Exception e) {
					        e.printStackTrace();
					    }
					}else {
		                System.out.println("Exit");
		                break;
		            }

			
			
			


		}


    	sc.close();
    }
}


