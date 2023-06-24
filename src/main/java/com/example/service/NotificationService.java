package com.example.service;

import java.util.List;

import com.example.model.Notification;

public interface NotificationService {
	
	//通知新規登録
	public void insertNotification(Notification notification);
	
	//通知一覧取得
	public List<Notification> selectNotificationList();
	
}
