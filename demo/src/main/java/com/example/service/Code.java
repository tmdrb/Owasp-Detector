	package com.example.service;
	
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.regex.Matcher;
	import java.util.regex.Pattern;
	
	import org.json.simple.JSONArray;
	import org.json.simple.JSONObject;
	import org.springframework.stereotype.Service;
	
	
	@Service
	public class Code extends AnalyzeCode{
		
		
		private ArrayList<String> PackageManage = new ArrayList<String>();
		private ArrayList<String> ClassManage = new ArrayList<String>();
		private HashMap<String,String> map = new HashMap<String, String>();
		
		private JSONArray arr = new JSONArray();
		private JSONObject obj = new JSONObject();
		JSONArray varvalobj = null;
		private ArrayList<String> xmlresult = new ArrayList<String>();
		
		private Pattern Packagepattern = Pattern.compile("(package)\\s+[a-zA-Z0-9\\.]+");
		private Pattern ClassPattern = Pattern.compile("(public|protected|private)?\\s+([a-z]+)*\\s+(class)\\s+.+\\{");
		private Pattern VarPattern = Pattern.compile("^\\s*(public\\s+|protected\\s+|private\\s+)?(static\\s+|final\\s+)?([a-zA-Z\\[\\]\\<\\>]+\\s+)([a-zA-Z0-9]+\\s*)(\\;|\\=)");
		private Pattern ValPattern;
		private Pattern RemarkPattern = Pattern.compile("^\\/\\/.+|^\\/\\*.+");
		private Pattern MybatisPattern = Pattern.compile("\\s*\\<(insert|select|update|delete|union).+");
		
		private Matcher matcher;
		private static String usercode;
	
		private Code() {
			
		}
		public boolean isxml() {
			matcher = MybatisPattern.matcher(Code.usercode);
	
			if(matcher.find()) {
				return true;
			}
			return false;
		}
		public ArrayList<String> findsqlinxml(){
			xmlresult.clear();
			matcher = MybatisPattern.matcher(Code.usercode);
			while(matcher.find()) {
				String xmlquery = usercode.split(matcher.group())[1].split("\\s*\\<\\/(insert|select|update|delete|union)\\s*\\>")[0].trim();
				if(xmlquery.contains("$")) {
					System.out.println("취약함");
					xmlresult.add(xmlquery);
				}
				else if(xmlquery.contains("#")) {
					System.out.println("안취약함");
				}
			}
			return xmlresult;
		}
		public void delete() {
			arr.clear();
			map.clear();
	
			PackageManage.clear();
			ClassManage.clear();
			obj.clear();
		}
		public void getInstance(String usercode) {
			Code.usercode = usercode;
		}
		
		public String getUserCode() {
			return Code.usercode;
		}
		@Override
		public ArrayList<String> Packagesfind() {
			// TODO Auto-generated method stub
			matcher = Packagepattern.matcher(usercode);
			while(matcher.find()) {
				PackageManage.add(matcher.group());
			}
			if(PackageManage.isEmpty())
				PackageManage.add("none");
			return PackageManage;
		}
		@Override
		public void Classesfind()  {
			// TODO Auto-generated method stub
			matcher = ClassPattern.matcher(usercode);
			
			while(matcher.find()) {
				ClassManage.add(matcher.group().trim().replaceAll("\\{", ""));
				
			}
			
		}
	
		@Override
		public void varsfind() throws Exception {
			// TODO Auto-generated method stub
			
			for(int i=0;i<ClassManage.size();i++) {
				String tempcode = new String();
				JSONObject classobj = new JSONObject();
				HashMap map = new HashMap<String, String>();
				
				varvalobj = new JSONArray();
				String classname = ClassManage.get(i);
				
				
				if(i+1 < ClassManage.size())	
					tempcode = usercode.split(ClassManage.get(i))[1].split(ClassManage.get(i+1))[0];
				else
					tempcode= usercode.split(ClassManage.get(ClassManage.size()-1))[1];
					
				
					for(int j=0; j<tempcode.split("(\n)").length;j++) {
						String chechkedtempcode = tempcode.split("\n")[j].trim();
					
						Matcher mm = RemarkPattern.matcher(chechkedtempcode);
						
						matcher = VarPattern.matcher(chechkedtempcode);
						if(!mm.find()) {	
						while(matcher.find()) {
							String a = matcher.group().trim();
						
							a = a.replaceAll("=", "");
							a = a.replace(";", "");
							if(a.split("\\s+").length == 2) {
							
								varvalobj.add(a);
							}
							if(a.split("\\s+").length >2) {
								String b = a.split("\\s+")[a.split("\\s+").length-2]+" "+a.split("\\s+")[a.split("\\s+").length-1];
								varvalobj.add(b);
							}
							
						}
					}
						
						
					}
					System.out.println(varvalobj);
					valsFind(map);
					
					classobj.put("name",classname);
					classobj.put("var",varvalobj);
					classobj.put("val", map);
					
					arr.add(classobj);
					
				System.out.println(arr);
				
			}		
			
					obj.put("package", arr);
		}
		
		public void valsFind(HashMap map) throws Exception {
			
			String buff = new String();
			map.clear();
			for(int i=0;i<varvalobj.size();i++) {
				try {
				String tempcode = usercode.split("("+varvalobj.get(i).toString().split("\\s+")[1]+")\\s*\\=")[1];
				
				if(usercode.split("("+varvalobj.get(i).toString().split("\\s+")[1]+")\\s*\\=").length>2) {
					System.out.println(varvalobj.get(i).toString().split("\\s+")[1]);	
					tempcode = usercode.split("("+varvalobj.get(i).toString().split("\\s+")[1]+")\\s*\\=")[usercode.split("("+varvalobj.get(i).toString().split("\\s+")[1]+")\\s*\\=").length-1];
					for(int j=0;j<tempcode.split("\n").length;j++) {
						
						if(tempcode.split("\n")[j].trim().matches(".+\\;$")) {
							buff += tempcode.split("\n")[j].trim();
							
							map.put(varvalobj.get(i).toString().split("\\s+")[1],buff);
							break;
						}
						else {
								buff +=tempcode.split("\n")[j].trim();
						}
					
					}
					
				}
				else {
				for(int j=0;j<tempcode.split("\n").length;j++) {
					
					if(tempcode.split("\n")[j].trim().matches(".+\\;$")) {
						buff += tempcode.split("\n")[j].trim();
						
						map.put(varvalobj.get(i).toString().split("\\s+")[1],buff);
						break;
					}
					else {
							buff +=tempcode.split("\n")[j].trim();
					}
				
				}
				
				}
				buff = "";
				
			}
					catch(Exception e) {
						e.printStackTrace();
				map.put(varvalobj.get(i).toString().split("\\s+")[1], "null");
			}
	
			}
		
			
		}	public JSONObject getClassSt() {
			return this.obj;
		}
	
	}