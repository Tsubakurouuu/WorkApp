package com.example.service.impl;

import java.util.Calendar;

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
		//カレンダークラスのオブジェクトを生成
		Calendar calendar = Calendar.getInstance();
		//Workに現在日時をセット
		work.setYear(calendar.get(Calendar.YEAR));
		work.setMonth(calendar.get(Calendar.MONTH) + 1);
		work.setDate(calendar.get(Calendar.DATE));
		work.setAttendanceHour(calendar.get(Calendar.HOUR_OF_DAY));
		work.setAttendanceMinute(calendar.get(Calendar.MINUTE));
		mapper.insertAttendance(work);
	}
	
	//退勤時間登録（更新）
	@Override
	public void updateLeaving(Work work) {
		//カレンダークラスのオブジェクトを生成
		Calendar calendar = Calendar.getInstance();
		//Workに現在日時をセット
		work.setYear(calendar.get(Calendar.YEAR));
		work.setMonth(calendar.get(Calendar.MONTH) + 1);
		work.setDate(calendar.get(Calendar.DATE));
		work.setLeavingHour(calendar.get(Calendar.HOUR_OF_DAY));
		work.setLeavingMinute(calendar.get(Calendar.MINUTE));
		work.setRestHour(1);
		work.setRestMinute(0);
//		work.setWorkingTimeHour(work.getLeavingHour() - work.getAttendanceHour());
//		if(work.getLeavingMinute() - work.getAttendanceMinute() >= 0) {
//			work.setWorkingTimeMinute(work.getLeavingMinute() - work.getAttendanceMinute());
//		} else if (work.getLeavingMinute() - work.getAttendanceMinute() < 0) {
//			work.setWorkingTimeMinute(-(work.getLeavingMinute() - work.getAttendanceMinute()));
//		}
//		work.setOverTimeHour(work.getLeavingHour() - work.getAttendanceHour() - 8);
//		if(work.getLeavingMinute() - work.getAttendanceMinute() >= 0) {
//			work.setOverTimeMinute(work.getLeavingMinute() - work.getAttendanceMinute());
//		} else if (work.getLeavingMinute() - work.getAttendanceMinute() < 0) {
//			work.setOverTimeMinute(-(work.getLeavingMinute() - work.getAttendanceMinute()));
//		}
		mapper.updateLeaving(work);
	}
	
	//勤怠情報取得
	@Override
	public Work selectWork(Integer id) {
		return mapper.selectWork(id);
	}
}

