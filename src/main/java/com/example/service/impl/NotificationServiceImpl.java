package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Notification;
import com.example.repository.NotificationMapper;
import com.example.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	@Autowired
	private NotificationMapper mapper;
	
	//通知新規登録
	@Override
	public void insertNotification(Notification notification) {
		mapper.insertNotification(notification);
	}
	
	//通知一覧取得
	@Override
	public List<Notification> selectNotificationList() {
		return mapper.selectNotificationList();
	}
	
	//通知削除
	@Override
	public void deleteNotification(int id) {
		mapper.deleteNotification(id);
	}

	
}
