package com.example.model;

import lombok.Data;

@Data
public class Notification {
	
	//ID
	private Integer id;
		
	//ユーザーID
	private Integer userId;
	
	//勤怠ID
	private Integer workId;
	
	//メッセージ
	private String message;
}
