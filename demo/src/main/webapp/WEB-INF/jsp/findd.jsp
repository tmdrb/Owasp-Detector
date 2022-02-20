<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>


<!DOCTYPE html>
<html>

<head>
  <title>Detecting Tool</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
 
</head>
<body style="height:2500px">
<br>

<div class="container">
  <h2>웹 개발자를 위한 시큐어 코딩 가이드</h2>
  <br>
  <!-- Nav tabs -->
  <ul class="nav nav-tabs" role="tablist" name="tabs">
    <li class="nav-item">
      <a class="nav-link active" data-toggle="tab" href="#home">Home</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" data-toggle="tab" href="#menu1">취약점 진단하러가기(파일제출)</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" data-toggle="tab" href="#menu2">빠르게 진단하기</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" data-toggle="tab" href="#menu3">모의해킹 하러가기</a>
    </li>
  
  </ul>

  <div class="tab-content">
    <div id="home" class="container tab-pane active"><br>
      <h3>HOME</h3>
      <br><br>
      <h2>시큐어 코딩이란 ? </h2>
      <br>
      <p>안전한 소프트웨어 개발을 위해 소스 코드 등에 존재할수 있는 잠재적 보안 취약점을 제거하고,</p>
      <p>보안을 고려햐여 기능을 설계 및 구현하는 등 소프트웨어 개발과정에서 지켜야 할 일련의 보안활동 입니다.</p>
      <br>
      <h3 style="color:red">시큐어 코딩이 필요한 이유</h3>
      <br>
      <image src="/img/need.png"></image>
      <br><br><br>
      <h3>실제 해킹사례</h3><br>
     	<ul>
      <li><a>sql injection 피해사례</a><br><br>
      	<p>사례 1</p><br>
      	<image height="300" src = "/img/ex1.png"></image><br><br>
      	<p>사례2</p>
      	<br>
      	<image height ="300" src = "/img/ex2.png"></image>
      </li>
      </ul>
      <br>
      <h3>참고 자료</h3>
      <ul>
      <li><a href="https://www.owasp.org/images/b/bd/OWASP_Top_10-2017-ko.pdf" target="_blank">owasp 2017 top 10</a></li>
      </ul>
    </div>
    
    <div id="menu1" class="container tab-pane fade"><br>
      <h3>코드 취약점 진단</h3>
      <p>내코드에서 웹 취약점을 진단할 수 있습니다.</p>
      
      <p>아래 절차대로 진행하면 됩니다.</p>
      <p>단 mybatis사용시 xml 파일까지 첨부해야 합니다.</p>
      <br>
      <br>
      <br>
      <p>업로드할 파일을 선택해주세요.(복수 선택가능)</p><br>
  
<form name="form" action="upload" method="post" enctype="multipart/form-data" accept-charset="euc-kr">
<input id="if" multiple="multiple"  type="file" name="filename[]" accept=".java,.xml" /><br>    

 </form>
<br>
    <button id="submit">확인</button>  <br>
   	<br><div style="height: auto; width: 100%; border:1px solid gold;">
   	<h3 align="center"> 분석결과 확인하기</h3>
   	<div id="result">
   		
   	</div>
   	<p id="mention"></p>
	</div>
    
    
    </div>
    
    <div id="menu2" class="container tab-pane fade"><br>
      <h3>간단하게 취약점 진단</h3>
      <p>의심가는 코드를 아래 텍스트칸에 입력후 제출 버튼 클릭</p>
      <br>
      
<textarea rows="30" cols="70" id="inputtext" ></textarea><br><br>
<button id="sub">제출</button>
<br>
<br>
<div id="result1">
   		
   	</div>

    </div>
    
      <div id="menu3" class="container tab-pane fade"><br>
      <h3>모의 해킹을 통해 SQL INJECTION의 위험성을 알 수 있습니다.</h3>
      <br>
      <br>
      <ul>
      	<li><a href="test/sqlinjection">sql injection을 사용해서 모의해킹 하러가기</li>
      </ul>
      <br><br>
      <p>select * from db where id="" and password=""</p>
    </div>
  </div>
  
  
</div>


<h2 id='sol'></h2>


<script>

	var button = document.getElementById("sub");
	
	var xhttp = new XMLHttpRequest();
	var filearray = new Array();
	var showfiles = new Array();
	
	button.addEventListener("click",clicked);
	document.getElementById("submit").addEventListener("click",function(){
		
		var Data = new FormData(document.form);
		
		$.ajax({
            cache : false,
            url : "upload", 
            processData: false,
            contentType: false,
            type : 'POST',
            data : Data, 
            success : function(data) {
        		var result = document.getElementById("result");
        		result.removeChild(result.childNodes[0]);
				alert("success");
            	maketable(data);
            	
            }, // success 
    
            error : function(xhr, status) {
                alert(xhr + " : " + status);
            }
        }); 
		
	});
	function maketable(data){
		
		
		var array = new Array();
		
		var contrast = ["filename","request","Mybatis","prepared","statement","jdbctemplate","sqlinjection"];
		
		var trarr = ["filename","request.getParameter()사용여부","mybatis사용여부","PreparedStatment사용여부","Statement사용여부","jdbctemplate사용여부","sqlinjection취약여부"];
		var table = document.createElement("table");
        table.border="2";
        table.align ="center";
        var thead = document.createElement("thead");
          var tr = document.createElement("tr");
		for(var name in trarr){
            var th = document.createElement("th");
			th.innerHTML = trarr[name];
			
            tr.appendChild(th);
			
		}
        thead.appendChild(tr);
        table.appendChild(thead);
        
        var tbody = document.createElement("tbody");
        var tr;
        for(var i in data){
        	 
        	var mybatissolution = new Array();
    		var statementsolution = new Array();
    		var preparedsolution = new Array();
    		var jdbctemplatesolution = new Array();
    		var codesolution = new Array();
    		var filenames = new Array();
    	
        	
        	codesolution.push(data[i].code);
        	var jo = new Object();
        	tr = document.createElement("tr");
        	tr.align ="center";
        	  for(var j in contrast){
        		 
        		  var td = document.createElement("td");
        		  var subject = contrast[j];
        		
        		  if(subject !== "filename"){
        			 	
        			  if(data[i][subject] === "null" || data[i][subject] == false){
        				  td.innerHTML = "X";
        				  tr.appendChild(td);
        				  
        				  
        			  }
        			  else{
        				  var obj= JSON.stringify(data[i][subject]);
        				  if(subject === "Mybatis"){
        					  mybatissolution.push(obj);
        					  tr.style.backgroundColor = "red";
        				  }
        				  else if(subject === "statement"){
        					  
        					  statementsolution.push(obj);
        					  tr.style.backgroundColor = "red";
        				  }
        				  else if(subject === "prepared"){
        					
        					  if(obj !== "true"){
        					  preparedsolution.push(obj);
        					  tr.style.backgroundColor = "red"; 
        					 }
        					 
        				  }
        				  else if(subject === "jdbctemplate"){
        					  jdbctemplatesolution.push(obj);
        					  tr.style.backgroundColor = "red";
        				  }
        				  
        				  
        				  td.innerHTML = "O";
        				  
        				  
        				  tr.appendChild(td);
        			  }
        		  }
        		  else{
        			  jo.filename=data[i].filename;
        			 
        			  td.innerHTML=data[i].filename;
        			  tr.appendChild(td);
        		
        		  }
        		  
        	  }
        	
        	tbody.appendChild(tr);
        	
       
        	jo.my = mybatissolution;
        	jo.st = statementsolution;
        	jo.prepare = preparedsolution;
        	jo.code = codesolution;
        	
        	array.push(jo);
        }
        
        table.appendChild(tbody);
        document.getElementById("result").appendChild(table);
        
        var m="";

        for(var i in array){
        	
        if(array[i].my.length == 0 && array[i].st.length == 0 && array[i].prepare.length == 0){
        	
        	m += "<br><br><h3 style='color:blue'>SQL INJECTION에 대해서 의심갈 만한 코드가 발견 되지 않았습니다.</h3><br>"
        		m += "<br><h3>[코드 분석]</h3>"
        		m += "<h4>파일 이름 : <span style='font-weight:bold;''>"+array[i]["filename"]+"</span></h4>";
        	m += array[i].code[0]; 
        	
        	
        	document.getElementById("mention").innerHTML = m;
 
        }
        else{
        	m += "<br><br><h3 style='color:red'>SQL INJECTION이 의심가는 코드를 발견했습니다.</h3><br>"
        		m += "<br><h3>[코드 분석]</h3>"
        	m += "<h4>파일 이름 : <span style='font-weight:bold;''>"+array[i]["filename"]+"</span></h4>";
        	var tt = array[i].code[0];
        	var howto ="";
        	
        	
        	while(array[i].my.length != 0 || array[i].st.length != 0 || array[i].prepare.length != 0){
        		
        		if(array[i].my.length != 0){
        		
        			for(var j in array[i].my){
        				
        				var temp = new Array();
        				
        				var sta = JSON.stringify(array[i].my).split('[\\"')[1].split('\\"]')[0];
        				
        				for(var z=0;z<sta.split('\\",\\"').length; z++){
            				if(sta.split('\\",\\"').length == 1){
            				
            					temp.push(sta.replace(/\\\\n/g,'').replace(/\\\\/g,''));
            					
            				
            				}
            				else{
            					if(sta.split('\\",\\"')[z].indexOf('"')){
                    				temp.push(sta.split('\\",\\"')[z].replace(/\\\\n/g,'').replace(/\\\\/g,''))
                    				
                    				}else{
                    					temp.push(sta.split('\\",\\"')[z]);
                    				}
            					
            				}	
        				}
        			
        			}
        		var tt='';
        			for(var j in temp){
            			var te = temp[j]
            			
            			tt += "<mark style='color:blue'>"+temp[j]+"</mark><br><br>";
            		}
        			
    				howto += tt;	
    				howto +="<br><h3>[ 해결 방법 ]</h3><br>";
    				howto += "<br><p>위에 사용자 코드중에서 마크된 부분이 SQL INJECTION의 위험 요소일 수 있습니다.</p>"
    						+ "<p>아래 해결방법으로 코드를 수정하길 바랍니다.</p>" 
    						+ "<ul><li>$는 위험하니 #로 변경해 주세요</li></ul>"
    				
    				array[i].my = [];
        			
        		}
        /*	else if(jdbctemplatesolution.length != 0){
				for(var i in jdbctemplatesolution){
					m += "<h4>" + jdbctemplatesolution[i] + "<br></h4>";
        		}
				m += "<br>사용자 코드중에서 sql injection을 야기할수 있는 코드 입니다. <br>"
				m += ""
        	}*/
        	else if(array[i].st.length != 0){
        		
        		
        		
        		for(var j in array[i].st){
        			var temp = new Array();
        			var sta = JSON.stringify(array[i].st).split(':[\\"')[1].split('\\"]}')[0];
        			
        			for(var z=0;z<sta.split('\\",\\"').length; z++){
        			if(sta.split('\\",\\"').length == 1){
        				temp.push(sta[0].replace('\\"',''))
        			}
        			
        			else{
        				if(sta.split('\\",\\"')[z].indexOf('"')){
        				temp.push(sta.split('\\",\\"')[z].replace(/\\\\\\/g,''))
        				}else{
        					temp.push(sta.split('\\",\\"')[z]);
        				}
        			}
        			}
        			
        		}
        
        		for(var j in temp){
        			var te = temp[j]
        			
        			tt = tt.replace(te,"<mark style='color:red'>"+temp[j]+"</mark>");
        		}
				howto += tt;	
				howto +="<br><h3>[ 해결 방법 ]</h3><br>";
				howto += "<br><p>위에 사용자 코드중에서 마크된 부분이 SQL INJECTION의 위험 요소일 수 있습니다.</p>"
						+ "<p>아래 해결방법으로 코드를 수정하길 바랍니다.</p>" 
						+ "<ul><li>Statement는 SQL INJECTION을 발생시키는 치명적인 변수 입니다. PreparedStatement로 바꿔서 사용 바랍니다.</li><li>Statement 변수를 사용시 쿼리부분에 replace함수를 사용해서 특수문자를 제거하길 바랍니다.</li></ul>"
				
						array[i].st.splice(0,array[i].st.length);
        	}
        
        	else if(array[i].prepare.length != 0){
        		
        		for(var j in array[i].prepare){
        			var temp = new Array();
        			var sta = JSON.stringify(array[i].prepare).split(':[\\"')[1].split('\\"]}')[0];
        			
        			for(var z=0;z<sta.split('\\",\\"').length; z++){
        			if(sta.split('\\",\\"').length == 1){
        				temp.push(sta.replace(/\\/g,''))
        			}
        			
        			else{
        				if(sta.split('\\",\\"')[z].indexOf('"')){
        				temp.push(sta.split('\\",\\"').replace(/\\/g,''))
        				}else{
        					temp.push(sta.split('\\",\\"').replace(/\\/g,''));
        				}
        			}
        			}
        			
        		}
      
        		for(var j in temp){
        			var te = temp[j].split('=')[1]
        		
        			tt = tt.replace(te,"<mark style='color:gray'>"+temp[j]+"</mark>");
        		}
				howto += tt;	
				howto +="<br><h3>[ 해결 방법 ]</h3><br>";
				howto += "<br><p>위에 사용자 코드중에서 마크된 부분이 SQL INJECTION의 위험 요소일 수 있습니다.</p>"
						+ "<p>아래 해결방법으로 코드를 수정하길 바랍니다.</p>" 
						+ "<ul><li>PreparedStatement를 사용 하더라도 sql문 안에 ?를 넣어서 사용하셔야 합니다.</li><li>preparedStatment.setString()함수를 이용하시길 바랍니다</li></ul>"
				
						array[i].prepare.splice(0,array[i].prepare.length);
        		
        	}
        	
        	
        	m += howto;
        	document.getElementById("mention").innerHTML = m;
        	
        }
        
        }
	}
	}
	
	function clicked(){
		var param = 'content=';
		param += document.getElementById('inputtext').value;
		param = param.replace(/&/gi,"~");
		xhttp.open("post","/finding",true);
		xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		xhttp.send(param);
	}
	
	xhttp.addEventListener('load',function(){
		var solution = xhttp.responseText;
		var aa = JSON.parse(solution);
		var r = document.getElementById("result1");
		r.removeChild(r.childNodes[0]);
		simpleta(aa);
	})
	
	function simpleta(data){
		
var contrast = ["filename","request","Mybatis","prepared","statement","jdbctemplate","sqlinjection"];
		
		var t1 = ["filename","request.getParameter()사용여부","mybatis사용여부","PreparedStatment사용여부","Statement사용여부","jdbctemplate사용여부","sqlinjection취약여부"];
		var table = document.createElement("table");
        table.border="2";
        table.align ="center";
        var thead = document.createElement("thead");
          var tr = document.createElement("tr");
          tr.align ="center";
		for(var name in t1){
            var th = document.createElement("th");
			th.innerHTML = t1[name];
			
            tr.appendChild(th);
			
		}
        thead.appendChild(tr);
        table.appendChild(thead);
        
        var tbody = document.createElement("tbody");
        var tr = document.createElement("tr");
        tr.align ="center";
        for(var j in contrast){
        	
		 var td = document.createElement("td");
		 var subject =  contrast[j]
		 
		  if(subject !== "filename"){
			 	
			  if(data[0][subject] === "null" || data[0][subject] == false){
				  td.innerHTML = "X";
				  tr.appendChild(td);
				  tr.style.backgroundColor = "powderblue";
				  
			  }
			  else{
				  var obj= JSON.stringify(data[0][subject]);
				  if(subject === "Mybatis"){
					 
					  tr.style.backgroundColor = "red";
				  }
				  else if(subject === "statement"){
					  
					  
					  tr.style.backgroundColor = "red";
				  }
				  else if(subject === "prepared"){
					
					  if(obj !== "true"){
					 
					  tr.style.backgroundColor = "red"; 
					 }
					 
				  }
				  else if(subject === "jdbctemplate"){
					
					  tr.style.backgroundColor = "red";
				  }
				  
				 
				  td.innerHTML = "O";
				  
				  
				  tr.appendChild(td);
			  }
		  }
		  else{
			 
			 
			  td.innerHTML=data[0]["filename"];
			  tr.appendChild(td);
		
		  }
	}
    	tbody.appendChild(tr);
    	
    
    table.appendChild(tbody);
    document.getElementById("result1").appendChild(table);
        
        
	}
	

	

</script>
</body>

</html>