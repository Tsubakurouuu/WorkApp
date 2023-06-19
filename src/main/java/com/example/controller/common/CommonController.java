package com.example.controller.common;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class CommonController {
	
	//就業時間と残業時間を計算するメソッド
	public static Integer[] calcWorkingOver(Integer attendanceHour, Integer attendanceMinute, Integer leavingHour, Integer leavingMinute, Integer restHour, Integer restMinute) {
		//出勤時間、退勤時間、休憩時間を分換算する
		Integer totalAttendanceMinutes = attendanceHour * 60 + attendanceMinute;
        Integer totalLeavingMinutes = leavingHour * 60 + leavingMinute;
        Integer totalRestMinutes = restHour * 60 + restMinute;
        //就業時間を分換算する
        Integer totalWorkMinutes = totalLeavingMinutes - totalAttendanceMinutes - totalRestMinutes;
        //就業時間（時）の計算
        Integer workingHour = totalWorkMinutes / 60;
		//就業時間（分）の計算
		Integer workingMinute = totalWorkMinutes % 60;
        //定時の就業時間を設定
        Integer standardWorkHour = 8;
        Integer standardWorkMinute = 0;
        //定時の就業時間を分換算する
        Integer totalStandardMinutes = standardWorkHour * 60 + standardWorkMinute;
        //残業時間を分換算する
        Integer totalOvertimeMinutes = totalWorkMinutes - totalStandardMinutes;
        //就業時間が定時の就業時間を下回っていたら0を設定
        if (totalOvertimeMinutes < 0) {
            totalOvertimeMinutes = 0;
        }
        //残業時間（時）の計算
        Integer overTimeHour = totalOvertimeMinutes / 60;
        //残業時間（分）の計算
        Integer overTimeMinute = totalOvertimeMinutes % 60;
        //就業時間と残業時間を配列にする
        Integer[] calcWorkingOver = { workingHour, workingMinute, overTimeHour, overTimeMinute };
        return calcWorkingOver;
	}
	/*----------------------------*/
	
	
	
	//5捨6入をして現在時分を返すメソッド
	public static Integer[] roundOff(Integer hour, Integer minute) {
		//時を取得
		if(minute >= 56) {
			hour += 1;
		}
		//分を取得
		if(minute >= 6 && minute <= 15) {
			minute = 10;
		} else if(minute >= 16 && minute <= 25) {
			minute = 20;
		} else if(minute >= 26 && minute <= 35) {
			minute = 30;
		} else if(minute >= 36 && minute <= 45) {
			minute = 40;
		} else if(minute >= 46 && minute <= 55) {
			minute = 50;
		} else {
			minute = 0;
		}
		//時分を配列にする
		Integer[] roundOff = { hour, minute };
		return roundOff;
	}
	/*----------------------------*/
	
	
	
	//時分フォーム入力用メソッド
	public static void formNumbers(Model model) {
		List<Integer> hourNumbers = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
		model.addAttribute("hourNumbers", hourNumbers);
		List<Integer> minuteNumbers = Arrays.asList(0, 10, 20, 30, 40, 50);
		model.addAttribute("minuteNumbers", minuteNumbers);
	}
	/*----------------------------*/
}
