package com.example.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.core.ApplicationContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.service.Code;
import com.example.service.FindSqlInjection;



@Controller
public class MainController {
	public static final int both =0;
	public static final int onlyPrepared = 1;
	public static final int onlyStatement = 2;
	
	@Autowired
	FindSqlInjection sqlinjection;
	@Autowired
	Code analyzecode;
	
	
	@GetMapping("login")
	public String hello() {
		System.out.println("hello");
		return "hello";
	}
	@PostMapping("upload")
	public @ResponseBody JSONArray upload(@RequestParam("filename[]") MultipartFile[] files) {
		 HashMap<String,MultipartFile> map = new HashMap<String, MultipartFile>();
		 HashMap<String,ArrayList<String>> results = new HashMap<String, ArrayList<String>>();
		
		 JSONObject sub=null;
		 JSONArray total = new JSONArray();
		 total.clear();
		
		for(MultipartFile f:files) {
			 map.put(f.getOriginalFilename(), f);
		 }
		
		 Iterator iter = map.keySet().iterator();
		 
		 try {
		 while(iter.hasNext()) {
			
			 sub = new JSONObject();
			 HashMap ma = new HashMap();
			 MultipartFile file =map.get(iter.next());
			
			 sub.put("filename",file.getOriginalFilename());
			 BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(),"euc-kr"));
			 String a="";
			 String b="";
			 String exportcode = "";
		     while((a=reader.readLine())!=null) {
				b+="\n"+a;
				exportcode += "<br>"+a;
		     }
		     
		     sub.put("code",exportcode);
		     sub.put("Mybatis","null");
    		 sub.put("sqlinjection",false);
    		 sub.put("prepared","null");
			 sub.put("statement","null");
			 sub.put("jdbctemplate", "null");
			 sub.put("request", "null");
			 
			System.out.println("1");
		     analyzecode.delete();
		    
		     analyzecode.getInstance(b);
		     
		     if(analyzecode.isxml()) {
		    	 System.out.println("2");
		    	 if(analyzecode.findsqlinxml() ==null) {
		    		 
		    		 sub.replace("Mybatis","null");
		    		 sub.replace("sqlinjection",false);
		    		 
		    	 }
		    	 else {
		    		 System.out.println("3");
		    		 sub.replace("Mybatis",analyzecode.findsqlinxml());
		    		 sub.replace("sqlinjection",true);
		    	 }
		    	 
		    	 total.add(sub);
		     }
		   /*  else if(sqlinjection.findtemplate(b) == null) {
		    	 System.out.println(sqlinjection.findtemplate(b));
		    	 sub.replace("jdbctemplate", sqlinjection.findtemplate(b));
				 sub.replace("sqlinjection", true);
				 System.out.println("5");
				 
				 total.add(sub);
		     }*/
		     else {
		    	 System.out.println("4");
			 analyzecode.Packagesfind();
		 	 analyzecode.Classesfind();
		   	 analyzecode.varsfind();
		   	 JSONObject ob = analyzecode.getClassSt();
		   	 JSONArray ja = (JSONArray) ob.get("package");
		   	System.out.println(ob);
		   	 for(int i =0;i<ja.size();i++) {
		   	
			 sqlinjection.init(b, ob);
				 System.out.println("6");
				 sub.replace("jdbctemplate", "null");
				 ArrayList<String> sql = sqlinjection.FindSql();
				 
				 if(sqlinjection.FindReqeust()) {
					 System.out.println("7");
					 sub.replace("request",true);}
				 else {
					 System.out.println("8");
					 sub.replace("request", false);
				 }
				 if(sqlinjection.FindStatement() != null) {
						 System.out.println("9");
						 if(sqlinjection.FindReplace(sql) != null) {
							 System.out.println("10");
							 sub.replace("sqlinjection", true);
							 sub.replace("statement",sqlinjection.FindReplace(sql));
						 }
						 else {
							 System.out.println("11");
							 sub.replace("statement","null");
							 sub.replace("sqlinjection", false);
							 
						 }
					 }
					 else if(sqlinjection.FindPreparedStatement() != null) {
						 System.out.println("12");
						 if(sqlinjection.Findquestion(sql) != null) {
							 sub.replace("sqlinjection", true);
							 System.out.println("13");
							 sub.replace("prepared",sqlinjection.Findquestion(sql));
							 
						 }
						 
						 else {
							 System.out.println("14");
							 sub.replace("prepared",true);
							
							 sub.replace("sqlinjection", false);
						 }
					 }
				
					 
				 
			 
			 
		
		
		     }
		   	
		   
		     total.add(sub);
		    
		     
		 }

		     
		 }
		 
		 }
		 
		 
		 
		 catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(total);
		return total;
		
	
		
	}
	
	@PostMapping("finding")
	public @ResponseBody JSONArray finding(@RequestParam("content") String usercode) {
		
		
		 HashMap<String,ArrayList<String>> results = new HashMap<String, ArrayList<String>>();
		
		 JSONObject sub=null;
		 JSONArray total = new JSONArray();
		 total.clear();
		 
		 try {
		
			 sub = new JSONObject();
			
		     
		     sub.put("code",usercode);
		     sub.put("Mybatis","null");
		     sub.put("sqlinjection",false);
		     sub.put("prepared","null");
			 sub.put("statement","null");
			 sub.put("filename","quick");
			 sub.put("jdbctemplate", "null");
			 sub.put("request", "null");
			 
			System.out.println("1");
		     analyzecode.delete();
		    
		     analyzecode.getInstance(usercode);
		     
		     if(analyzecode.isxml()) {
		    	 System.out.println("2");
		    	 if(analyzecode.findsqlinxml() ==null) {
		    		 
		    		 sub.replace("Mybatis","null");
		    		 sub.replace("sqlinjection",false);
		    		 
		    	 }
		    	 else {
		    		 System.out.println("3");
		    		 sub.replace("Mybatis",analyzecode.findsqlinxml());
		    		 sub.replace("sqlinjection",true);
		    	 }
		    	 
		    	 total.add(sub);
		     }
		   /*  else if(sqlinjection.findtemplate(b) == null) {
		    	 System.out.println(sqlinjection.findtemplate(b));
		    	 sub.replace("jdbctemplate", sqlinjection.findtemplate(b));
				 sub.replace("sqlinjection", true);
				 System.out.println("5");
				 
				 total.add(sub);
		     }*/
		     else {
		    	 System.out.println("4");
			 analyzecode.Packagesfind();
		 	 analyzecode.Classesfind();
		   	 analyzecode.varsfind();
		   	 JSONObject ob = analyzecode.getClassSt();
		   	 JSONArray ja = (JSONArray) ob.get("package");
		   	System.out.println(ob);
		   	 for(int i =0;i<ja.size();i++) {
		   	
			 sqlinjection.init(usercode, ob);
				 System.out.println("6");
				 sub.replace("jdbctemplate", "null");
				 ArrayList<String> sql = sqlinjection.FindSql();
				 
				 if(sqlinjection.FindReqeust()) {
					 System.out.println("7");
					 sub.replace("request",true);}
				 else {
					 System.out.println("8");
					 sub.replace("request", false);
				 }
				 if(sqlinjection.FindStatement() != null) {
						 System.out.println("9");
						 if(sqlinjection.FindReplace(sql) != null) {
							 System.out.println("10");
							 sub.replace("sqlinjection", true);
							 sub.replace("statement",sqlinjection.FindReplace(sql));
						 }
						 else {
							 System.out.println("11");
							 sub.replace("statement","null");
							 sub.replace("sqlinjection", false);
							 
						 }
					 }
					 else if(sqlinjection.FindPreparedStatement() != null) {
						 System.out.println("12");
						 if(sqlinjection.Findquestion(sql) != null) {
							 sub.replace("sqlinjection", true);
							 System.out.println("13");
							 sub.replace("prepared",sqlinjection.Findquestion(sql));
							 
						 }
						 
						 else {
							 System.out.println("14");
							 sub.replace("prepared",true);
							
							 sub.replace("sqlinjection", false);
						 }
					 }
				
					 
				 
			 
			 
		
		
		     }
		   	
		   
		     total.add(sub);
		    
		     
		 }

		     
		 
		 
		 }
		 
		 
		 
		 catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(total);
		return total;
		
	
	}
	
	@GetMapping("find")
	public String findsql(Model m) {
		
		
		return "findd";
	}
}
