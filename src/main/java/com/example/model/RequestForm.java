package com.example.model;

import lombok.Data;

@Data
public class RequestForm {
	//ID
	private Integer id;
	//ユーザーID
	private Integer userId;
	//年
	private Integer year;
	//月
	private Integer month;
	//日
	private Integer date;
	//【新】出勤ステータス
	private Integer workStatusNew;
	//【新】出勤(時)
	private Integer attendanceHourNew;
	//【新】出勤(分)
	private Integer attendanceMinuteNew;
	//【新】退勤(時)
	private Integer leavingHourNew;
	//【新】退勤(分)
	private Integer leavingMinuteNew;
	//【新】休憩(時)
	private Integer restHourNew;
	//【新】休憩(分)
	private Integer restMinuteNew;
	//申請理由
	private String comment;
	//【旧】出勤ステータス
	private Integer workStatusOld;
	//【旧】出勤(時)
	private Integer attendanceHourOld;
	//【旧】出勤(分)
	private Integer attendanceMinuteOld;
	//【旧】退勤(時)
	private Integer leavingHourOld;
	//【旧】退勤(分)
	private Integer leavingMinuteOld;
	//【旧】休憩(時)
	private Integer restHourOld;
	//【旧】休憩(分)
	private Integer restMinuteOld;
}
