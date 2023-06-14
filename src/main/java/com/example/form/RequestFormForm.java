package com.example.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.example.model.Work;

import lombok.Data;

@Data
public class RequestFormForm {
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
	@NotNull(groups = ValidGroup1.class)
	private Integer workStatus;
	
	//出勤(時)
	@Min(value = 0, groups = ValidGroup1.class)
	@Max(value = 23, groups = ValidGroup1.class)
	private Integer attendanceHour;
	
	//出勤(分)
	@Min(value = 0, groups = ValidGroup1.class)
	@Max(value = 59, groups = ValidGroup1.class)
	private Integer attendanceMinute;
	
	//退勤(時)
	@Min(value = 0, groups = ValidGroup1.class)
	@Max(value = 23, groups = ValidGroup1.class)
	private Integer leavingHour;
	
	//退勤(分)
	@Min(value = 0, groups = ValidGroup1.class)
	@Max(value = 59, groups = ValidGroup1.class)
	private Integer leavingMinute;
	
	//休憩(時)
	@Min(value = 0, groups = ValidGroup1.class)
	@Max(value = 23, groups = ValidGroup1.class)
	private Integer restHour;
	
	//休憩(分)
	@Min(value = 0, groups = ValidGroup1.class)
	@Max(value = 59, groups = ValidGroup1.class)
	private Integer restMinute;
	
	//申請理由
	@Length(max = 100, groups = ValidGroup1.class)
	private String comment;
	
	//勤怠テーブル
	private Work work;
}
