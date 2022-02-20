package com.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class FindSqlInjection {
	
	
	private HashMap<String,ArrayList > errors;
	private HashMap<String,ArrayList> findtemplate;
	private HashMap<String,ArrayList> findreplace;
	private HashMap<String,ArrayList> findquestion;
	private HashMap<String,ArrayList> findprepared;
	private HashMap<String,ArrayList> findstatement;
	
	private String usercode;
	private JSONObject ClassManage;
	private Pattern sqlPattern = Pattern.compile("(select|SELECT|update|UPDATE)\\s*(.+)\\s*(from|FROM|set|SET)\\s*(.+)\\s*");
	private Pattern sqlPattern1 = Pattern.compile("(insert|INSERT|delete|DELETE)\\s*(from|FROM|INTO|into).+");
	
	private ArrayList<String> result;
	private int countStatement;
	
	FindSqlInjection(){
		
		
		this.countStatement =0;
		this.result = new ArrayList<String>();
		this.errors = new HashMap<String, ArrayList>();
		this.findtemplate = new HashMap<String, ArrayList>();
		findstatement = new HashMap<String, ArrayList>();
		findreplace = new HashMap<String, ArrayList>();
		findquestion = new HashMap<String, ArrayList>();
		findprepared = new HashMap<String, ArrayList>();
	}
	public void delete() {
		this.ClassManage.clear();
		
		this.countStatement =0;
		this.errors.clear();
				
	
	}
	
	public void init(String usercode,JSONObject object) {
		this.usercode = usercode;
		this.ClassManage = object;
	}
	public HashMap findtemplate(String usercode) {
		this.findtemplate.clear();

		String code = usercode;
		ArrayList<String> var = new ArrayList<String>();
		ArrayList<String> ch = new ArrayList<String>();
		Pattern pattern = Pattern.compile(".+(JdbcTemplate)\\s*[a-zA-Z0-9]+");
		Matcher m = pattern.matcher(code);
		
		/*while(m.find()) {
			String v = m.group().split("\\s+")[m.group().split("\\s+").length-1].toString();
			if(var.contains(v)) {
				
			}
			else {
				var.add(v);
			}
		}	
		
		for(String a : var) {
			
			Pattern pp = Pattern.compile("("+a+")\\.[a-zA-Z]+");
			Matcher m1 = pp.matcher(code);
			while(m1.find()) {
				String v = code.split(m1.group())[1].split("\\;\\n")[0];
				
				if(ch.contains(v)) {
					
				}
				else {
					ch.add(v);
				}
				
			}
		}
		findtemplate.put("1", ch);
	
		for(int i=0;i<classes.size();i++) {
			JSONObject obj = (JSONObject) classes.get(i);
			JSONArray vars = (JSONArray)obj.get("var");
			HashMap vals =  (HashMap) obj.get("val");
			ArrayList<String> aa = new ArrayList();
			for(int j=0;j<vars.size();j++) {
				
				if(vars.get(j).toString().contains("JdbcTemplate")) {
					String jdbctemvar = vars.get(j).toString().split("\\s+")[1];
					Pattern p = Pattern.compile(jdbctemvar+"\\.[a-zA-Z]+\\(");
					
					Matcher m = p.matcher(usercode);
					
					
				}
				else {
					
				}
				
					
				}	
		
			if(!aa.isEmpty()) {
				findtemplate.put(obj.get("name").toString(), aa);
			}
		}
		
		if(findtemplate.isEmpty()) {
			return null;
		}
		return findtemplate;*/
		return findtemplate;
	}
	
	
	public boolean FindReqeust() throws Exception{
	
		JSONArray classes = (JSONArray) ClassManage.get("package");
	
		Iterator iter;
	
		for(int i=0;i<classes.size();i++) {
			JSONObject obj = (JSONObject) classes.get(i);
			iter = ((HashMap)obj.get("val")).values().iterator();
			ArrayList<String> aa = new ArrayList();
			while(iter.hasNext()) {
				String value = (String) iter.next();
				
				if(value != null&& value.contains("request.getParameter(")) {
					String saved = "request:"+value;
					aa.add(saved);
					
				}
			}
			if(!aa.isEmpty())
				errors.put(obj.get("name").toString(), aa);

		}
		
		if(errors.isEmpty()) {
			return false;
		}
		return true;
		
	}
	
	public ArrayList<String> FindSql() throws Exception {
		JSONArray classes = (JSONArray) ClassManage.get("package");
		Iterator iter;
		ArrayList<String> noquestion = new ArrayList();
		for(int i=0;i<classes.size();i++) {
			JSONObject obj = (JSONObject) classes.get(i);
			HashMap sqlmap = (HashMap)obj.get("val");
			try {
				
			iter = (Iterator) sqlmap.keySet().iterator();
			while(iter.hasNext()) {
				
				String var = iter.next().toString();
				String value = sqlmap.get(var).toString();
				
		if(value != null) {
				Matcher matcher = sqlPattern.matcher(value);
				Matcher matcher1 = sqlPattern1.matcher(value);
				
				if(matcher.find() || matcher1.find()) {
				
					noquestion.add(var+"~"+value);
					
				}}
			}
			}catch(Exception e) {e.printStackTrace();}
		
		}
		
		return noquestion;
	}
	
	public HashMap FindStatement() {
		
		JSONArray classes = (JSONArray) ClassManage.get("package");
				for(int i=0;i<classes.size();i++) {
			JSONObject obj = (JSONObject) classes.get(i);
			JSONArray array = (JSONArray)obj.get("var");
			ArrayList<String> hasStatement = new ArrayList<String>();
			this.findstatement.clear();
		
			
			for(int j=0; j<array.size();j++) {
				if(array.get(j).toString().contains("Statement")) {
					String varname = array.get(j).toString().split("\\s+")[1];
					HashMap map = (HashMap) obj.get("val");
					Iterator itz = map.entrySet().iterator();
					
					while(itz.hasNext()) {
						
						String zzz = itz.next().toString();
						if(zzz.split("=")[1].contains("executeQuery(")) {
							
							hasStatement.add(zzz);
						}
					}
					
					
				}
				else {
					
				}
				
				
			}
			
			if(!hasStatement.isEmpty())
				findstatement.put(obj.get("name").toString(),hasStatement);
		}
		
		if(!findstatement.isEmpty())
			return findstatement;
		return null;
	}
	public HashMap FindPreparedStatement() throws Exception {
		JSONArray classes = (JSONArray) ClassManage.get("package");
		this.findprepared.clear();
	
		for(int i=0;i<classes.size();i++) {
			JSONObject obj = (JSONObject) classes.get(i);
			JSONArray array = (JSONArray)obj.get("var");
		    ArrayList<String> hasPrepared = new ArrayList<String>();
		    
			for(int j=0; j<array.size();j++) {
				if(array.get(j).toString().contains("PreparedStatement")) {
					String varname = array.get(j).toString().split("\\s+")[1];
					
					hasPrepared.add(varname+"~"+((HashMap)obj.get("val")).get(varname));
				}
				
			}
			
			if(!hasPrepared.isEmpty())
				findprepared.put(obj.get("name").toString(),hasPrepared);
		}
		
		
		if(!findprepared.isEmpty()) {
			return findprepared;
		}
		return null;
	}
	
	public HashMap Findquestion(ArrayList<String> sql) throws Exception{

		
		this.findquestion.clear();
		Iterator it = findprepared.keySet().iterator();
		
		while(it.hasNext()) {
			String classname = it.next().toString();
			ArrayList<String> question = findprepared.get(classname);
			ArrayList<String> sa = new ArrayList<String>();
			for(String s: question) {
				
				String sqlinprepared = s.split("prepareStatement\\(")[1].split("\\)")[0].trim();
				
				if(sqlinprepared.matches("(select|SELECT|update|UPDATE)\\\\s*(.+)\\\\s*(from|FROM|set|SET)\\\\s*(.+)\\\\s*") || 
						sqlinprepared.matches("(insert|INSERT|delete|DELETE)\\\\s*(from|FROM|INTO|into).+")) {
					
					if(!sqlinprepared.contains("?")) {
						sa.add(s);
					}
				}
				
				else {
					
					for(String sq: sql) {
						
						if(sq.contains(sqlinprepared+"~")) {
							
							if(!sq.split("~")[1].contains("?")) {
								sa.add(sq.replaceAll("\\~", "="));
								break;
							}
						}
						else {
							
						}
					}
				}
			}
			
			if(!sa.isEmpty())
				findquestion.put(classname,sa);
		}
		if(!findquestion.isEmpty())
			return findquestion;
		return null;
		/*if(sql !=null) {
		for(String s:hasPrepared) {
			
			
		
			for(String d: sql) {
				if(d.contains(sqlinprepared+"~")) {
					
					if(!d.split("~")[1].contains("?")) {
						
						result.add(d.replaceAll("\\~", "="));
					}
				}
			}
			
		}
		}
		
		else {
			for(String s:hasPrepared) {
				String sqlinprepared = s.split("preparedStatement\\(")[1].split("\\)")[0].trim();
				
						
						if(!sqlinprepared.contains("?")) {
							
							result.add(s);
						}
			
				
				
			}
		}
		return result;*/
	}
	public HashMap FindReplace(ArrayList<String> sql) throws Exception{
		this.findreplace.clear();
		Iterator it = findstatement.keySet().iterator();
		while(it.hasNext()) {
			String classname = it.next().toString();
			ArrayList<String> replace = findstatement.get(classname);
			ArrayList<String> sa = new ArrayList<String>();
			for(String s: replace) {
				Matcher m = sqlPattern.matcher(s);
				Matcher m1 = sqlPattern1.matcher(s);
				
				if(m.find() || m1.find()) {
					
					if(!s.contains(".replace(") || !s.contains(".replaceAll(")) {
						sa.add(s);
					}
				}
				else {

					for(String sq: sql) {
						if(s.split("\\(")[1].split("\\)")[0].contains(sq.split("~")[0])) {
							if(sq.split("~")[0].contains("replace\\(") || sq.split("~")[0].contains("replaceAll\\(")) {
								
							}
							else {
								sa.add(s.split("\\.")[0].split("\\=")[1]);
								
								sa.add(sq.split("~")[1].replaceAll("\\\\",""));
								
							}
						}
						else {
							sa.add("executeQuery 함수에서 sql 변수를 찾을수 없습니다.");
						}
						
					}
					
				}
				
				}
			
			if(!sa.isEmpty())
				findreplace.put(classname, sa);
		}
		
		if(findreplace.isEmpty())
			return null;
		return findreplace;
				/*ArrayList<String> resultset = new ArrayList<String>();
		JSONArray classes = (JSONArray) ClassManage.get("package");
		for(int i=0;i<classes.size();i++) {
			JSONObject obj = (JSONObject) classes.get(i);
			JSONArray vars = (JSONArray)obj.get("var");
			HashMap map = (HashMap) obj.get("val");
			
			for(int j=0;j<vars.size();j++) {
				if(vars.get(i).toString().contains("ResultSet")) {
					resultset.add(vars.get(i).toString().split(" ")[1].trim());
				}
			}
			
			
		
		
		
			for(String s:resultset) {
				String pre = map.get(s).toString().split("executeQuery\\(")[1].split("\\)")[0].trim();
				if(sql!=null) {
					
					for(String sq:sql) {
						if(sq.contains(pre+"~")) {
							if(sq.contains("replace(")){
								System.out.println("예빵함");
								return true;
							}
							else {
							System.out.println("sqlinjection 위험 있음");
							return false;
						}
						}
						
						
						
					}
					
				}
				else {
					if(pre.contains("replace(")) {
						System.out.println("잘 예방함");
						return true;
					}
					else {
						System.out.println("injection  위험 있음");
						return false;
					}
				}
		}
		
		
		}
		return true;*/
	}
	public void analyzeJava() throws Exception {
		JSONArray classes = (JSONArray) ClassManage.get("package");
		for(int i=0;i<classes.size();i++) {
			JSONObject obj = (JSONObject) classes.get(i);
			JSONArray vars = (JSONArray)obj.get("var");
			
			for(int j=0;j<vars.size();j++) {
				
				if(vars.get(j).toString().contains("Statement") || vars.get(j).toString().contains("PreparedStatement")) {
					System.out.println("jdbc");
				}
			}
		}
	}
	
	
}