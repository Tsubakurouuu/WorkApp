package com.example.service.impl;

import java.util.Calendar;
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
		//カレンダークラスのオブジェクトを生成
		Calendar calendar = Calendar.getInstance();
		//Workに現在日時をセット
		work.setYear(calendar.get(Calendar.YEAR));
		work.setMonth(calendar.get(Calendar.MONTH) + 1);
		work.setDate(calendar.get(Calendar.DATE));
		work.setAttendanceHour(calendar.get(Calendar.HOUR_OF_DAY));
		//5捨6入処理
		if(calendar.get(Calendar.MINUTE) >= 6 && calendar.get(Calendar.MINUTE) <= 15) {
			work.setAttendanceMinute(10);
		} else if(calendar.get(Calendar.MINUTE) >= 16 && calendar.get(Calendar.MINUTE) <= 25) {
			work.setAttendanceMinute(20);
		} else if(calendar.get(Calendar.MINUTE) >= 26 && calendar.get(Calendar.MINUTE) <= 35) {
			work.setAttendanceMinute(30);
		} else if(calendar.get(Calendar.MINUTE) >= 36 && calendar.get(Calendar.MINUTE) <= 45) {
			work.setAttendanceMinute(40);
		} else if(calendar.get(Calendar.MINUTE) >= 46 && calendar.get(Calendar.MINUTE) <= 55) {
			work.setAttendanceMinute(50);
		}else if(calendar.get(Calendar.MINUTE) <= 5) {
			work.setAttendanceMinute(0);
		}else if(calendar.get(Calendar.MINUTE) >= 56) {
			work.setAttendanceHour(calendar.get(Calendar.HOUR_OF_DAY) + 1);
			work.setAttendanceMinute(0);
		}
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
		//5捨6入処理
		if(calendar.get(Calendar.MINUTE) >= 6 && calendar.get(Calendar.MINUTE) <= 15) {
			work.setLeavingMinute(10);
		} else if(calendar.get(Calendar.MINUTE) >= 16 && calendar.get(Calendar.MINUTE) <= 25) {
			work.setLeavingMinute(20);
		} else if(calendar.get(Calendar.MINUTE) >= 26 && calendar.get(Calendar.MINUTE) <= 35) {
			work.setLeavingMinute(30);
		} else if(calendar.get(Calendar.MINUTE) >= 36 && calendar.get(Calendar.MINUTE) <= 45) {
			work.setLeavingMinute(40);
		} else if(calendar.get(Calendar.MINUTE) >= 46 && calendar.get(Calendar.MINUTE) <= 55) {
			work.setLeavingMinute(50);
		}else if(calendar.get(Calendar.MINUTE) <= 5) {
			work.setLeavingMinute(0);
		}else if(calendar.get(Calendar.MINUTE) >= 56) {
			work.setLeavingHour(calendar.get(Calendar.HOUR_OF_DAY) + 1);
			work.setLeavingMinute(0);
		}
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
	
	//勤怠情報一覧取得（ユーザーごと）
	@Override
	public List<Work> selectWorkList(Integer userId) {
		return mapper.selectWorkList(userId);
	}
	
	//勤怠情報更新
	@Override
	public void updateWork(Work work) {
		mapper.updateWork(work);
	}
	
	//勤怠情報更新（申請フォーム）
	@Override
	public void updateWorkRequestForm(Work work) {
		mapper.updateWorkRequestForm(work);
	}
	
	
	@Override
	public Work selectWorkLeaving(Integer userId, Integer year, Integer month, Integer date) {
		return mapper.selectWorkLeaving(userId, year, month, date);
	}
}

