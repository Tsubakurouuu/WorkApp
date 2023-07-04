package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.MUser;
import com.example.repository.MUserMapper;
import com.example.service.MUserService;

@Service
public class MUserServiceImpl implements MUserService {
	
	@Autowired
	private MUserMapper mapper;
	
	@Autowired
	private PasswordEncoder encoder;
	
	//ユーザー一覧取得
	@Override
	public List<MUser> selectUserList() {
		return mapper.selectUserList();
	}
	
	//ユーザー詳細取得
	@Override
	public MUser selectUserDetail(String userId) {
		return mapper.selectUserDetail(userId);
	}
	
	//ユーザー新規登録
	@Override
	public void insertUser(MUser user) {
		//ロールを"USER"でセット
		user.setRole("USER");
		//パスワードを取得
		String rawPassword = user.getPassword();
		//取得したパスワードを暗号化
		user.setPassword(encoder.encode(rawPassword));
		mapper.insertUser(user);
	}
	
	//ユーザーのuserId一覧取得
	@Override
	public List<String> selectUserIdList() {
		return mapper.selectUserIdList();
	}
	
	//ユーザーID(Str)詳細取得
	@Override
	public MUser selectUserIdStr(Integer id) {
		return mapper.selectUserIdStr(id);
	}
	
}
