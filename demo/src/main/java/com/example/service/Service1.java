package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.MemberDao;
import com.example.dto.Member;

@Service
public class Service1 implements MainService{

	@Autowired
	MemberDao mapper;
	
	
	@Override
	public ArrayList<Member> getMember(Member m) {
		// TODO Auto-generated method stub
		return mapper.getMember(m);	
	}

}
