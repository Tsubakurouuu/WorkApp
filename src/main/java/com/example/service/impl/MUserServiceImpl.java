package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.MUser;
import com.example.repository.MUserMapper;
import com.example.service.MUserService;

@Service
public class MUserServiceImpl implements MUserService {
	@Autowired
	private MUserMapper mapper;

	//ログインユーザー取得処理
	@Override
	public MUser getLoginUser(String userId) {
		return mapper.getLoginUser(userId);
	}
	//ユーザー一覧取得
	@Override
	public List<MUser> getUserList() {
		return mapper.getUserList();
	}
	//ユーザー詳細取得
	@Override
	public MUser getUserDetail(String userId) {
		return mapper.getUserDetail(userId);
	}
}
