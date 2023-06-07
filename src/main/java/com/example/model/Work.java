package com.example.model;

import lombok.Data;

@Data
public class Work {
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
	//出勤（時）
	private Integer attendanceHour;
	//出勤（分）
	private Integer attendanceMinute;
	//退勤（時）
	private Integer leavingHour;
	//退勤（分）
	private Integer leavingMinute;
	//休憩（時）
	private Integer restHour;
	//休憩（分）
	private Integer restMinute;
	//就業時間（時）
	private Integer workingTimeHour;
	//就業時間（分）
	private Integer workingTimeMinute;
	//残業時間（時）
	private Integer overTimeHour;
	//残業時間（分）
	private Integer overTimeMinute;
	//出勤ステータス
	private Integer workStatus;
	//ユーザーテーブル
	private MUser mUser;
	//申請フォームテーブル
//	private RequestForm requestForm;

}
