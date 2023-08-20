package com.example.model;

import lombok.Data;

@Data
public class Notification {
	
	//ID
	private int id;
		
	//ユーザーID
	private int userId;
	
	//勤怠ID
	private int workId;
	
	//メッセージ
	private String message;
	
	//ユーザーテーブル
	private MUser mUser;
	
	//勤怠テーブル
	private Work work;
}
