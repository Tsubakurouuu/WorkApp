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
	public Work selectWork(Integer id);
	//勤怠情報更新
	public void updateWork(@Param("work") Work work);
	//勤怠情報更新（申請フォーム）
	public void updateWorkRequestForm(@Param("work") Work work);
	//勤怠情報月毎取得
	public List<Work> selectWorkListMonth(Integer userId, Integer year, Integer month);
}
