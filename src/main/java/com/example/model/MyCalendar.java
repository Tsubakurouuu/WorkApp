package com.example.model;

import lombok.Data;

@Data
public class MyCalendar {
	//カレンダー年
	private Integer year;
	//カレンダー月
	private Integer month;
	//カレンダー日
	private Integer[] days;
}
