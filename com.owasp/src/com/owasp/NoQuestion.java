package com.owasp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



//�⺻������
public class NoQuestion{
	private Connection con;
	
	private ResultSet rs =null;
	private PreparedStatement ps;
public NoQuestion(int empno, String ename, double sal){
   
    try{           
        Class.forName("oracle.jdbc.driver.OracleDriver");
       
        String url = "jdbc:oracle:thin:@192.168.0.3:1521:orcl";
        String db_id ="scott";
        String db_pw ="scott";
       
        con = DriverManager.getConnection(url, db_id, db_pw);
       
        System.out.println("DB ���� ����");
       
        String sql= "insert into emp(empno, ename, sal) values("+empno+",'"+ename+"',"+sal+")";
        
        ps = con.prepareStatement(sql);
        int result = ps.executeUpdate();
        System.out.print("ó���� ���ڵ��� ����: "+ result);
        System.out.println();
       
        //st.close();
        ps.close();
        con.close();
       
    }catch(ClassNotFoundException e){
        System.out.println(e + "=> ����̹� �ε� ����");
    }catch(SQLException e){
        System.out.println(e+ "=> Sql ���� ");
    }catch(Exception e){
        System.out.println(e + "=> �Ϲ� ����");
       
    }
}
public static void main(String args[]){
    new NoQuestion(1,"JavaPris", 100);
   
}

}