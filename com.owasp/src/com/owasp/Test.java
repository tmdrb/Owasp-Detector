package com.owasp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Test {

	static String makePattern = "\\{|\\}";
	static String sqlPattern = ".*\\\"\\s*[s|S][e|E][l|L][e|E][c|C][t|T]\\s*.+|.*\\\"\\s*[i|I][n|N][s|S][e|E][r|R][t|T]\\s*.+|.*\\\"\\s*[u|U][p|P][d|D][a|A][T|t][e|E]\\s*.+";
	static String requestPattern = ".*[^\\\"]+[request\\.getParameter\\(][\\\"]*[a-zA-Z0-9]+[\\\"]*[*\\)].*";	
	static String functionPattern = "\\s*[a-z]*\\s*[a-zA-Z]*\\s+[a-zA-Z]+\\s+[a-zA-Z0-9]+\\(.+\\).*";
	
	static HashMap vars = new HashMap<String,String>();
	static HashMap vals = new HashMap<String,String>();
	
	static ArrayList<String> errors  = new ArrayList();
	static ArrayList<String> sqlstring = new ArrayList();
	
	public static String[] distributeUsercode(String usercode) {
		
		ArrayList<String> lines = new ArrayList();
		String li="";
		
		for(int i=0; i<usercode.split("\r\n").length;i++) {
			if(!usercode.split("\r\n")[i].trim().equals("")) {
				//System.out.println(usercode.split("\r\n")[i].trim());
				lines.add(usercode.split("\r\n")[i].trim()+"\n");
				
			}
		}
	
		for (int i=0;i<lines.size();i++)
			li += lines.get(i);
		
		String fi = li.replaceAll(makePattern, "");
		
		String[] f = new String[fi.split("\n").length];
	
		f = fi.split("\n");
		
		return f;
		
	}
	
	public static String inputUsercode() {
		
		Scanner sc = new Scanner(System.in);
		StringBuffer buffer = new StringBuffer();
		String a;
		while(sc.hasNextLine()) {
			buffer.append(sc.nextLine());
		}
		return "success";
	}
	
	public static void distributeVarVal(String[] splitedusercode) {
		
	
		for(int i=0;i<splitedusercode.length;i++) {
			
			try {
				
				
			if(Pattern.matches(".*[a-zA-Z]+\\s+[a-zA-Z0-9]+\\s*\\=[^\\=].*\\;$|.*[a-zA-Z]+\\s+[a-zA-Z0-9]+\\s*\\;$|.*[a-zA-Z0-9]+\\s*\\=[^\\=].*\\;$|\\s*[a-z]+\\s+[a-zA-Z\\[\\]]+\\s+[a-zA-z0-9]+.+|\\s*[a-z]+\\s+[a-z]+\\s+[a-zA-Z\\[\\]]+\\s+[a-zA-Z0-9]+.+", splitedusercode[i].trim().toString())) {
		
				if(!splitedusercode[i].contains("return")) {
					
					if(splitedusercode[i].contains("=")) {
						
						if(splitedusercode[i].split("=")[0].trim().matches("[a-zA-Z]+\\s+[a-zA-Z0-9]+")){
							
							String vartype = splitedusercode[i].split("=")[0].trim().split(" ")[splitedusercode[i].split("=")[0].trim().split(" ").length-2];
							String var = splitedusercode[i].split("=")[0].trim().split(" ")[splitedusercode[i].split("=")[0].trim().split(" ").length-1].replace(";", "");
							String val = splitedusercode[i].split("=")[1].trim();
							
							if(vartype.equals("String")){
								
								
								if(splitedusercode[i].contains("\"")) {
								
									val =splitedusercode[i].split(var)[1].replace("=", "").trim();
									
									
								}
								
							}
							
							vals.put(var, val);
							if(vars.containsKey(vartype)) {
								
								vars.put(vartype, vars.get(vartype)+"~"+var);
							}
							
							else {
								vars.put(vartype, var);
							}
							
						
						}
						
						else if(splitedusercode[i].split("=")[0].trim().matches("[a-zA-Z0-9]+\\s*")){
							
							
							if(splitedusercode[i].matches(".+\\=\\s*\\\".+")) {
								
								vals.put(splitedusercode[i].split("=")[0].trim(), splitedusercode[i].split(splitedusercode[i].split("=")[0].trim())[1].replace("=", "").trim());
							}
							else {
								vals.put(splitedusercode[i].split("=")[0].trim(), splitedusercode[i].split("=")[1].trim());
								
							}
							
						}
					
						
					}
					
					
					else {
						
						if(vars.containsKey(splitedusercode[i].split(" ")[splitedusercode[i].split(" ").length-2])) {
						 
							
							
							String a = (String) vars.get(splitedusercode[i].split(" ")[splitedusercode[i].split(" ").length-2]);
							a += "~"+ splitedusercode[i].split(" ")[splitedusercode[i].split(" ").length-1].replace(";", "");
							
							vars.put(splitedusercode[i].split(" ")[splitedusercode[i].split(" ").length-2], a);
						}
						
						else {
							String a =splitedusercode[i].split(" ")[splitedusercode[i].split(" ").length-1].replace(";", "");
							vars.put(splitedusercode[i].split(" ")[splitedusercode[i].split(" ").length-2], a);
						}
					
					}
					
					
				}	
			}
			
			}
			catch(Exception e) {
			
			}
						
				
			
			
		}
		
		System.out.println("var ,val 분리 완료");
	}
	
	public static boolean useRequest(){
		
		boolean userequest = false;
		
		if(vars.containsKey("String")) {
			
			if(vars.get("String").toString().contains("~")) {
				
			for(int i =0;i<vars.get("String").toString().split("~").length;i++) {
				
				
				if(vals.get(vars.get("String").toString().split("~")[i]).toString().matches(requestPattern)) {
					
					System.out.println("request사용");
					userequest = true;
					break;
					
				}
			
			}
			
			}
			
			else {
				
				if( vals.get(vars.get("String").toString()).toString().matches(requestPattern)){
					System.out.println("request사용");
					userequest = true;
				}
			}
			
			
			
		}
		
		return userequest;
	}
	
	public static void useSql() {
		
		if(vars.containsKey("String")) {
			
			
			for(int i =0;i<vars.get("String").toString().split("~").length;i++) {
				
				
				if(vals.get(vars.get("String").toString().split("~")[i]).toString().matches(sqlPattern)) {
					
					System.out.println("sql문사용");
					sqlstring.add(vars.get("String").toString().split("~")[i]);
					
				}
			}
		
		}
		
	}
	
	public static void findSqlInjection() {
		
		
		if(vars.containsKey("PreparedStatement")) {
			
			System.out.println(vars.get("PreparedStatement"));
			try {
				
				for(int i=0;i<vars.get("PreparedStatement").toString().split("~").length;i++) {
					
				
					
					for(int j=0;j<sqlstring.size();j++) {
						
						if(vals.get(vars.get("PreparedStatement").toString().split("~")[i]).toString().contains(sqlstring.get(j))) {
							
							if(vals.get(sqlstring.get(j)).toString().contains("?")) {
									
							}
							else {
								
								errors.add(sqlstring.get(j)+"~PreparedStatement를 사용하였지만 sql문에 ?를 사용하길 권장합니다.");
							}
						}
						
						
						
					}
					
				}
				
			}
			catch(Exception e) {
				
				e.printStackTrace();
			}

		}
		
		//preparedstatement가 안쓰였을 경우
		else if(vars.containsKey("Statement") && !vars.containsKey("PreparedStatement")) {
			
			errors.add(vars.get("Statement").toString()+"~Statement보다는 PreparedStatemennt를 사용하길 권장합니다.");
			//Statement만 쓰였지만 패턴을 이용해서 필터링할 경우
		
		}
		
	} 
	
	
	
	public static void solution() {
		
		useSql();
		if(useRequest()) {
			
			findSqlInjection();
			
			if(errors.size()>0) {
				System.out.println("sql injection 취약가능성 있음");
				
				for(int i=0;i<errors.size();i++) {
					System.out.println("취약부분 : " + errors.get(i).split("~")[0] +"권장방법 : "+ errors.get(i).split("~")[1]);
				}
				
			}
			
			else {
				
				System.out.println("-----------sql injection 취약가능성 없음 --------");
			}
		}
		else {
			
			System.out.println("-------request.getParameter()함수를 사용하지 않아서 위험 없음------");
		}
	}
	
	
	public static void main(String[] args) {
		
		
		
	
		 //distributeVarVal(distributeUsercode(usercode));
			
		//solution();
		
	
		distributeVarVal(distributeUsercode("package com.h3xstream.findsecbugs.spring;\r\n" + 
				"\r\n" + 
				"import org.apache.bcel.Repository;\r\n" + 
				"import org.apache.bcel.classfile.JavaClass;\r\n" + 
				"\r\n" + 
				"import java.util.ArrayList;\r\n" + 
				"import java.util.List;\r\n" + 
				"import java.util.regex.Matcher;\r\n" + 
				"import java.util.regex.Pattern;\r\n" + 
				"\r\n" + 
				"/**\r\n" + 
				" * Similar to <code>edu.umd.cs.findbugs.ba.SignatureParser</code>\r\n" + 
				" *\r\n" + 
				" * It support the extraction of type in format such as:\r\n" + 
				" *  - java/util/List&lt;java/lang/String&gt; => java.util.List & java.lang.String\r\n" + 
				" */\r\n" + 
				"public class SignatureParserWithGeneric {\r\n" + 
				"\r\n" + 
				"    private String[] argumentsTypes;\r\n" + 
				"    private String returnType;\r\n" + 
				"\r\n" + 
				"    public SignatureParserWithGeneric(String signature) {\r\n" + 
				"        String sigOnlyArgs = signature.substring(signature.indexOf(\"(\")+1);\r\n" + 
				"        String[] paramAndReturn = sigOnlyArgs.split(\"\\\\)\");\r\n" + 
				"        argumentsTypes = paramAndReturn[0].split(\",\");\r\n" + 
				"\r\n" + 
				"        returnType = paramAndReturn[1];\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public List<JavaClass[]> getArgumentsClasses() {\r\n" + 
				"        List<JavaClass[]> types = new ArrayList<>();\r\n" + 
				"\r\n" + 
				"        for(String argumentType : argumentsTypes) {\r\n" + 
				"            if(argumentType.equals(\"\")) continue;\r\n" + 
				"            types.add(typeToJavaClass(argumentType));\r\n" + 
				"        }\r\n" + 
				"        return types;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public JavaClass[] getReturnClasses() {\r\n" + 
				"        return typeToJavaClass(returnType);\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    private JavaClass[] typeToJavaClass(String type) {\r\n" + 
				"        Matcher m = Pattern.compile(\"([^<]+)(<([^>]+)>)?\").matcher(type);\r\n" + 
				"\r\n" + 
				"        List<JavaClass> types = new ArrayList<>();\r\n" + 
				"        if(m.find()) {\r\n" + 
				"            try {\r\n" + 
				"                types.add(Repository.lookupClass(cleanClassName(m.group(1))));\r\n" + 
				"            } catch (ClassNotFoundException e) {\r\n" + 
				"                //System.out.println(e.getMessage());\r\n" + 
				"            }\r\n" + 
				"            if(m.groupCount() == 3 && m.group(3) != null) {\r\n" + 
				"                try {\r\n" + 
				"                    types.add(Repository.lookupClass(cleanClassName(m.group(3))));\r\n" + 
				"                } catch (ClassNotFoundException e) {\r\n" + 
				"                    //System.out.println(e.getMessage());\r\n" + 
				"                }\r\n" + 
				"            }\r\n" + 
				"\r\n" + 
				"        }\r\n" + 
				"        return types.toArray(new JavaClass[types.size()]);\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    private String cleanClassName(String classname) {\r\n" + 
				"        return classname.replaceAll(\"^L\", \"\").replaceAll(\";$\",\"\").replaceAll(\"/\",\".\");\r\n" + 
				"    }\r\n" + 
				"}"));
		System.out.println(vars.toString());
		System.out.println(vals.toString());
			

		
		
	
	
		
		
		
	
		
		
	
		
		
		

	}
}
