package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.Member;

@Mapper
public interface MemberDao {
	
	public ArrayList<Member> getMember(Member m);
}
