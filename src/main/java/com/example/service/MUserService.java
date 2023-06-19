package com.example.service;

import java.util.List;

import com.example.model.MUser;

public interface MUserService {
	
	//ユーザー一覧取得
	public List<MUser> selectUserList();
	
	//ユーザー詳細取得
	public MUser selectUserDetail(String userId);
	
	//ユーザー新規登録
	public void insertUser(MUser user);
	
}
