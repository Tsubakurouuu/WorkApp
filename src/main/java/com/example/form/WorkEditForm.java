package com.example.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class WorkEditForm {
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
	@Min(value = 0, groups = ValidGroup1.class)
	@Max(value = 23, groups = ValidGroup1.class)
	private Integer attendanceHour;
	
	//出勤（分）
	@Min(value = 0, groups = ValidGroup1.class)
	@Max(value = 59, groups = ValidGroup1.class)
	private Integer attendanceMinute;
	
	//退勤（時）
	@Min(value = 0, groups = ValidGroup1.class)
	@Max(value = 23, groups = ValidGroup1.class)
	private Integer leavingHour;
	
	//退勤（分）
	@Min(value = 0, groups = ValidGroup1.class)
	@Max(value = 59, groups = ValidGroup1.class)
	private Integer leavingMinute;
	
	//休憩（時）
	@Min(value = 0, groups = ValidGroup1.class)
	@Max(value = 23, groups = ValidGroup1.class)
	private Integer restHour;
	
	//休憩（分）
	@Min(value = 0, groups = ValidGroup1.class)
	@Max(value = 59, groups = ValidGroup1.class)
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
}
