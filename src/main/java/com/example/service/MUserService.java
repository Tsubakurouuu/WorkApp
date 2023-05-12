package com.example.service;

import com.example.model.MUser;

public interface MUserService {
	//ログインユーザー取得
	public MUser getLoginUser(String userId);
}
