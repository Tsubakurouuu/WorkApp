package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.model.Work;

@Mapper
public interface WorkMapper {
	
	//出勤時間登録
	public int insertAttendance(Work work);
	
	//退勤時間登録（更新）
	public void updateLeaving(@Param("work") Work work);
	
	//勤怠情報取得
	public Work selectWork(int id);
	
	//勤怠情報更新
	public void updateWork(@Param("work") Work work);
	
	//勤怠情報月毎取得
	public List<Work> selectWorkListMonth(int userId, int year, int month);
	
	//同日勤怠情報取得（退勤ボタン押下時）
	public Work selectWorkAttendance(int userId, int year, int month, int date);
	
	//勤怠情報新規登録
	public int insertWork(Work work);
	
	//勤怠情報取得
	public List<Work> selectWorkInfoList(int year, int month, int date);
	
}
