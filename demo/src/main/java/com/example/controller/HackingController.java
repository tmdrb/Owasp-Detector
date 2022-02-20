package com.example.controller;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.Member;
import com.example.service.Service1;

@RequestMapping("/test")
@Controller
public class HackingController {
	
	@Autowired
	Service1 service;
	
	
	@GetMapping("sqlinjection")
	public String hacking() {
		
		return "sqlinjection";
	}
	@GetMapping("remove")
	public String remove(HttpSession session) {
		session.removeAttribute("logined");
		return "redirect:sqlinjection";
	}
	@GetMapping("login")
	public String login() {
		return "login";
	}
	
	@PostMapping("createaccount")
	public String create() {
		return "create";
	}
	@PostMapping("processing")
	public String process(@RequestParam("id") String id,@RequestParam("pass") String password,Model model,HttpSession session) {
		System.out.println(id+" : "+password);
		JSONArray jar = new JSONArray();
		
		Member m = new Member();
		m.setId(id);
		m.setPassword(password);
		try {
		if(service.getMember(m) == null) {
			System.out.println("fail");
		}
		else {	
			
			if(id.equals( service.getMember(m).get(0).getId()) &&  password.equals( service.getMember(m).get(0).getPassword())) {
				
				System.out.println("로그인 성공");
				session.setAttribute("logined", service.getMember(m).get(0).getName());
				return "sqlinjection";
			}
			else {
				for(Member me : service.getMember(m)) {
					JSONObject obj = new JSONObject();
					obj.put("id",me.getId());
					obj.put("pass",me.getPassword());
					obj.put("address",me.getAddress());
					obj.put("phone",me.getPhonenumber());
					obj.put("name",me.getName());
					obj.put("account",me.getAccountnumber());
					jar.add(obj);}
				model.addAttribute("sql", jar);
				System.out.println(jar);
			}
				return "dbres";
			}
			
			
		
		}
		catch(Exception e) {e.printStackTrace();}
		return "redirect:login";
	}
}
