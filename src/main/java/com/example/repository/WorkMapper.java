package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.model.Work;

@Mapper
public interface WorkMapper {
	//出勤時間登録
	public int insertAttendance(Work work);
}
