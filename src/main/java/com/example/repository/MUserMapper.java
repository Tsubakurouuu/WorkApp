package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.model.MUser;

@Mapper
public interface MUserMapper {
	
	//ユーザー一覧取得
	public List<MUser> selectUserList();
	
	//ユーザー詳細取得
	public MUser selectUserDetail(String userId);
	
	//ユーザー新規登録
	public int insertUser(MUser user);
	
	//ユーザーのuserId一覧取得
	public List<String> selectUserIdList();
	
}
