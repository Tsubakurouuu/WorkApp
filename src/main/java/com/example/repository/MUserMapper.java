package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.model.MUser;

@Mapper
public interface MUserMapper {
	//ログインユーザー取得
	public MUser getLoginUser(String userId);
}
