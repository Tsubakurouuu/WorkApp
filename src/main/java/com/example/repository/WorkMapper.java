package com.example.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.model.Work;

@Mapper
public interface WorkMapper {
	//出勤時間登録
	public int insertAttendance(Work work);
	//退勤時間登録（更新）
	public void updateLeaving(@Param("work") Work work);
}
