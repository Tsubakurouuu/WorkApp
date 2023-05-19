package com.example.model;

import java.util.Calendar;

public class MyCalendarLogic {
	//カレンダーインスタンスを生成するメソッド
	public MyCalendar createMyCalendar(int... args) {
		//マイカレンダーインスタンスを生成
		MyCalendar myCalendar = new MyCalendar();
		//現在日時でカレンダーインスタンスを生成
		Calendar calendar = Calendar.getInstance();
		//2つの引数が来ていたら
		if(args.length == 2) {
			//最初の引数で年を設定
			calendar.set(Calendar.YEAR, args[0]);
			//次の引数で月を設定
			calendar.set(Calendar.MONTH, args[1] - 1);
		}
		//マイカレンダーに年を設定
		myCalendar.setYear(calendar.get(Calendar.YEAR));
		//マイカレンダーに月を設定
		myCalendar.setMonth(calendar.get(Calendar.MONTH) + 1);
		//マイカレンダーに日を設定
		Integer[] daysCount = new Integer[calendar.getActualMaximum(Calendar.DAY_OF_MONTH)];
		for(int day : daysCount) {
			int a = 1;
			daysCount[day] = a;
			a++;
		}
		myCalendar.setDays(daysCount);
		return myCalendar;
	}
}
