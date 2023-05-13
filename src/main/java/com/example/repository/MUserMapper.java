package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.model.MUser;

@Mapper
public interface MUserMapper {
	//ログインユーザー取得
	public MUser getLoginUser(String userId);
	//ユーザー一覧取得
	public List<MUser> getUserList();
	//ユーザー詳細取得
	public MUser getUserDetail(String userId);
	//ユーザー新規登録
	public int insertUser(MUser user);
}
