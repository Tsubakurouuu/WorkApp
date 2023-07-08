package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.model.Notification;

@Mapper
public interface NotificationMapper {
	
	//通知新規登録
	public void insertNotification(Notification notification);
	
	//通知一覧取得
	public List<Notification> selectNotificationList();
	
	//通知削除
	public void deleteNotification(Integer id);
	
}
