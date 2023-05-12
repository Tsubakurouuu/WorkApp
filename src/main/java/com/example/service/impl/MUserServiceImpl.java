package com.example.service.impl;

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
}
