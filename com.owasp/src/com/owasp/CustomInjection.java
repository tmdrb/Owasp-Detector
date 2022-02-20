package com.owasp;


import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class CustomInjection {
    //Declare variables
    static Connection con;
    static ResultSet rs = null;
    
    static Statement stmt;
    
    //SQL Connection method
    public static void getConnected(String input){
        try {
        	int i=0;
        	int j[] = {12,3};
        	int[] d = {1,1,5,3};
        	ArrayList a = new ArrayList();
        	a.add(3);
        	a.add(2);
        	j = a.;
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testMyNewPass" );
            stmt = con.createStatement();
            String sql = "SELECT * FROM crappy_table WHERE EmpNum = " + input;
            rs = stmt.executeQuery(sql);
            String firstname = rs.getString("FirstName");
            String lastname = rs.getString("LastName");
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ce){
            ce.printStackTrace();
        }
    }
    
    
    //Main
    public static void main(String args[]){
        
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter Employeee Number of employee: ");
        String input = scanner.next();
        
        getConnected(input);
      
        try{
        con.close();
        }catch (SQLException se){
            
        }
    }
}

