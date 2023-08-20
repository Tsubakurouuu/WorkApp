package com.example.service;

import java.util.List;

import com.example.model.Work;

public interface WorkService {
	
	//出勤時間登録
	public void insertAttendance(Work work);
	
	//退勤時間登録（更新）
	public void updateLeaving(Work work);
	
	//勤怠情報取得
	public Work selectWork(int id);
	
	//勤怠情報更新
	public void updateWork(Work work);
	
	//勤怠情報月毎取得
	public List<Work> selectWorkListMonth(int userId, int year, int month);
	
	//同日勤怠情報取得（退勤ボタン押下時）
	public Work selectWorkAttendance(int userId, int year, int month, int date);
	
	//勤怠情報新規登録
	public void insertWork(Work work);
	
	//勤怠情報取得
	public List<Work> selectWorkInfoList(int year, int month, int date);
	
}
