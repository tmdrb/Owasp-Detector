import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class F {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Work w = new Work();
		w.getToken();
	}

}
class Work{
	Stack stack;
	Pattern EOL ;
	Pattern identi ;
	Pattern vals ;
	Pattern Eq ;
	Pattern semi ;
	Pattern lcomment ;
	String a;
	Work(){
		stack = new Stack<String>();
		EOL = Pattern.compile("\r|\n|\r\n");
		identi = Pattern.compile("[a-zA-Z\\_]");
	 vals = Pattern.compile(".+");
		 Eq = Pattern.compile("=");
	semi = Pattern.compile(";");
		 lcomment = Pattern.compile("(/*)|//");
		 a = "class Work{\r\n" + 
				"	//Work(){\r\n" + 
				"		Stack stack = new Stack<String>();\r\n" + 
				"		Pattern skip = Pattern.compile(\"\\\\s+|\\r|\\n|\\r\\n\");\r\n" + 
				"		Pattern identi = Pattern.compile(\"[a-zA-Z\\\\_]\");\r\n" + 
				"	/*	Pattern vals = Pattern.compile(\".+\");\r\n" + 
				"		Pattern Eq = Pattern.compile(\"=\");\r\n" + 
				"		Pattern semi = Pattern.compile(\";\");\r\n" + 
				"		\r\n" + 
				"	}\r\n" + 
				"	public void getToken() {\r\n" + 
				"		String a = \"\";\r\n" + 
				"	}\r\n" + 
				"	\r\n" + 
				"}";
		
	}
	public void getToken() {
		
		
		Matcher m = lcomment.matcher(a);
		
		while(m.find()) {
			System.out.println(m.group());
			switch(m.group()) {
			case"/*":
				System.out.println("zz");
				break;
			case"//":
				
				String b = a.split("//")[1].split("\r\n")[0];
				
				a = a.replaceAll("Work", " ");
				System.out.println(a.trim());
				break;
			}
		}
	}
	
}