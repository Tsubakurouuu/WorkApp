package com.example.model;

import lombok.Data;

@Data
public class RequestForm {
	//ID
	private Integer id;
	//ユーザーID
	private Integer userId;
	//勤怠ID
	private Integer workId;
	//年
	private Integer year;
	//月
	private Integer month;
	//日
	private Integer date;
	//出勤ステータス
	private Integer workStatus;
	//出勤(時)
	private Integer attendanceHour;
	//出勤(分)
	private Integer attendanceMinute;
	//退勤(時)
	private Integer leavingHour;
	//退勤(分)
	private Integer leavingMinute;
	//休憩(時)
	private Integer restHour;
	//休憩(分)
	private Integer restMinute;
	//申請理由
	private String comment;
	//勤怠テーブル
	private Work work;
	//ユーザーテーブル
	private MUser mUser;
}
