package com.example.model;

import java.util.Date;

import lombok.Data;

@Data
public class MUser {
	
	//ID
	private Integer id;
	
	//ユーザーID
	private String userId;
	
	//パスワード
	private String password;
	
	//姓
	private String lastName;
	
	//名
	private String firstName;
	
	//誕生日
	private Date birthday;
	
	//ロール
	private String role;
	
}
