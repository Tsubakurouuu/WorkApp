package com.example.form;

import lombok.Data;

@Data
public class FormForm {
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
}
