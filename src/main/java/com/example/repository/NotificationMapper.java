package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.model.Notification;

@Mapper
public interface NotificationMapper {
	
	//通知新規登録
	public void insertNotification(Notification notification);
}
