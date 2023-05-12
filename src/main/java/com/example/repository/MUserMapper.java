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
}
