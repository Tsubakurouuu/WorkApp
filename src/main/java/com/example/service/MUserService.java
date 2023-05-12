package com.example.service;

import java.util.List;

import com.example.model.MUser;

public interface MUserService {
	//ログインユーザー取得
	public MUser getLoginUser(String userId);
	//ユーザー一覧取得
	public List<MUser> getUserList();
}
