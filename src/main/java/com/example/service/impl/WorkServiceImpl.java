package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Work;
import com.example.repository.WorkMapper;
import com.example.service.WorkService;

@Service
public class WorkServiceImpl implements WorkService {
	@Autowired
	private WorkMapper mapper;
	
	//出勤時間登録
	@Override
	public void insertAttendance(Work work) {
		mapper.insertAttendance(work);
	}
	
	//退勤時間登録（更新）
	@Override
	public void updateLeaving(Work work) {
		mapper.updateLeaving(work);
	}
	
	//勤怠情報取得
	@Override
	public Work selectWork(Integer id) {
		return mapper.selectWork(id);
	}
	
	//勤怠情報更新
	@Override
	public void updateWork(Work work) {
		mapper.updateWork(work);
	}
	
	//勤怠情報月毎取得
	@Override
	public List<Work> selectWorkListMonth(Integer userId, Integer year, Integer month) {
		return mapper.selectWorkListMonth(userId, year, month);
	}
	
	//同日勤怠情報取得（退勤ボタン押下時）
	@Override
	public Work selectWorkAttendance(Integer userId, Integer year, Integer month, Integer date) {
		return mapper.selectWorkAttendance(userId, year, month, date);
	}
}

