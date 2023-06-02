package com.example.service;

import java.util.List;

import com.example.model.Work;

public interface WorkService {
	//出勤時間登録
	public void insertAttendance(Work work);
	//退勤時間登録（更新）
	public void updateLeaving(Work work);
	//勤怠情報取得
	public Work selectWork(Integer id);
	//勤怠情報一覧取得（ユーザーごと）
	public List<Work> selectWorkList(Integer userId);
	//勤怠情報更新
	public void updateWork(Work work);
	//勤怠情報更新（申請フォーム）
	public void updateWorkRequestForm(Work work);
	//勤怠情報月毎取得
	public List<Work> selectWorkListMonth(Integer userId, Integer year, Integer month);
}
