package com.example.controller.common;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.form.RequestFormForm;
import com.example.form.WorkEditForm;

@Controller
public class CommonController {
	
	//★就業時間と残業時間を計算するメソッド
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
	
	
	
	//★5捨6入をして現在時分を返すメソッド
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
	
	
	
	//★時分フォーム入力用メソッド
	public static void formNumbers(Model model) {
		List<Integer> hourNumbers = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
		model.addAttribute("hourNumbers", hourNumbers);
		List<Integer> minuteNumbers = Arrays.asList(0, 10, 20, 30, 40, 50);
		model.addAttribute("minuteNumbers", minuteNumbers);
	}
	/*----------------------------*/
	
	
	
	//★休憩時間に1or0をセットするメソッド
	public static Integer calcRest(Integer attendanceHour, Integer attendanceMinute, Integer leavingHour, Integer leavingMinute) {
		//出勤時間、退勤時間を分換算する
		Integer totalAttendanceMinutes = attendanceHour * 60 + attendanceMinute;
        Integer totalLeavingMinutes = leavingHour * 60 + leavingMinute;
        //出勤時間、退勤時間の分換算が60より大きいかどうかで処理を分岐
        if(totalLeavingMinutes - totalAttendanceMinutes > 60) {
        	//60より大きければ1を返す
        	return 1;
        } else {
        	//60未満なら0を返す
        	return 0;
        }
	}
	
	/*----------------------------*/
	
	
	
	//★入力された出勤時間、退勤時間、休憩時間が時間軸として正しいかどうかを判断するメソッド
	public static Integer confirmWorkForm(Integer workStatus, Integer attendanceHour, Integer attendanceMinute, Integer leavingHour, Integer leavingMinute, Integer restHour, Integer restMinute) {
		//出勤ステータスが「出勤」であれば
		if(workStatus == 1) {
			//時間入力部分が全て入力されていないとエラーを返す
			if(attendanceHour == null || attendanceMinute == null || leavingHour == null || leavingMinute == null || restHour == null || restMinute  == null) {
				return 1;
			}
		}
		//出勤ステータスが「出勤」以外であれば
		if(workStatus != 1) {
			//時間入力部分に値が入力されているとエラーになる
			if(attendanceHour != null || attendanceMinute != null || leavingHour != null || leavingMinute != null || restHour != null || restMinute  != null) {
				return 2;
			}
			//問題なければ処理を抜ける
			return 0;
		}
		//出勤時間、退勤時間、休憩時間を分換算する
		Integer totalAttendanceMinutes = attendanceHour * 60 + attendanceMinute;
        Integer totalLeavingMinutes = leavingHour * 60 + leavingMinute;
        Integer totalRestMinutes = restHour * 60 + restMinute;
        //出勤時間が退勤時間よりも遅い時間になっていたらエラーを返す
        if(totalAttendanceMinutes > totalLeavingMinutes) {
			return 3;
        }
        //出勤時間と退勤時間の差分よりも休憩時間の値が大きければエラーを返す
        if(totalLeavingMinutes - totalAttendanceMinutes < totalRestMinutes) {
			return 4;
        }
        //問題なければ処理を抜ける
        return 0;
	}
	
	/*----------------------------*/

	
	
	//★入力チェックエラー時にリダイレクトして値を保持するメソッド(出退勤申請画面)
	public static void formRedirect(RequestFormForm form, RequestFormForm formRedirect) {
		//formRedirectがnullかどうかの判定(フォーム入力時にエラーがあった場合にはformRedirectに値が格納されている)
		if (formRedirect != null) {
			//フォームに値が入っていれば各変数に値を格納する(入っていなければnullが入る)
	        String attendanceHourStr = formRedirect.getAttendanceHour() != null ? formRedirect.getAttendanceHour().toString() : null;
	        String attendanceMinuteStr = formRedirect.getAttendanceMinute() != null ? formRedirect.getAttendanceMinute().toString() : null;
	        String leavingHourStr = formRedirect.getLeavingHour() != null ? formRedirect.getLeavingHour().toString() : null;
	        String leavingMinuteStr = formRedirect.getLeavingMinute() != null ? formRedirect.getLeavingMinute().toString() : null;
	        String restHourStr = formRedirect.getRestHour() != null ? formRedirect.getRestHour().toString() : null;
	        String restMinuteStr = formRedirect.getRestMinute() != null ? formRedirect.getRestMinute().toString() : null;
	        //出勤（時）がnullではなく空文字でもなければフォームに値を格納する
	        if (attendanceHourStr != null && !attendanceHourStr.isEmpty()) {
	            form.setAttendanceHour(Integer.parseInt(attendanceHourStr));
	        }
	        //出勤（分）がnullではなく空文字でもなければフォームに値を格納する
	        if (attendanceMinuteStr != null && !attendanceMinuteStr.isEmpty()) {
	            form.setAttendanceMinute(Integer.parseInt(attendanceMinuteStr));
	        }
	        //退勤（時）がnullではなく空文字でもなければフォームに値を格納する
	        if (leavingHourStr != null && !leavingHourStr.isEmpty()) {
	            form.setLeavingHour(Integer.parseInt(leavingHourStr));
	        }
	        //退勤（分）がnullではなく空文字でもなければフォームに値を格納する
	        if (leavingMinuteStr != null && !leavingMinuteStr.isEmpty()) {
	            form.setLeavingMinute(Integer.parseInt(leavingMinuteStr));
	        }
	        //休憩（時）がnullではなく空文字でもなければフォームに値を格納する
	        if (restHourStr != null && !restHourStr.isEmpty()) {
	            form.setRestHour(Integer.parseInt(restHourStr));
	        }
	        //休憩（分）がnullではなく空文字でもなければフォームに値を格納する
	        if (restMinuteStr != null && !restMinuteStr.isEmpty()) {
	            form.setRestMinute(Integer.parseInt(restMinuteStr));
	        }
	    }
	}
	
	/*----------------------------*/
	
	
	
	
	//★入力チェックエラー時にリダイレクトして値を保持するメソッド
	public static void formRedirect(WorkEditForm form, WorkEditForm formRedirect) {
		//formRedirectがnullかどうかの判定(フォーム入力時にエラーがあった場合にはformRedirectに値が格納されている)
		if (formRedirect != null) {
			//フォームに値が入っていれば各変数に値を格納する(入っていなければnullが入る)
	        String attendanceHourStr = formRedirect.getAttendanceHour() != null ? formRedirect.getAttendanceHour().toString() : null;
	        String attendanceMinuteStr = formRedirect.getAttendanceMinute() != null ? formRedirect.getAttendanceMinute().toString() : null;
	        String leavingHourStr = formRedirect.getLeavingHour() != null ? formRedirect.getLeavingHour().toString() : null;
	        String leavingMinuteStr = formRedirect.getLeavingMinute() != null ? formRedirect.getLeavingMinute().toString() : null;
	        String restHourStr = formRedirect.getRestHour() != null ? formRedirect.getRestHour().toString() : null;
	        String restMinuteStr = formRedirect.getRestMinute() != null ? formRedirect.getRestMinute().toString() : null;
	        //出勤（時）がnullではなく空文字でもなければフォームに値を格納する
	        if (attendanceHourStr != null && !attendanceHourStr.isEmpty()) {
	            form.setAttendanceHour(Integer.parseInt(attendanceHourStr));
	        } else {
	        	form.setAttendanceHour(null);
	        }
	        //出勤（分）がnullではなく空文字でもなければフォームに値を格納する
	        if (attendanceMinuteStr != null && !attendanceMinuteStr.isEmpty()) {
	            form.setAttendanceMinute(Integer.parseInt(attendanceMinuteStr));
	        } else {
	        	form.setAttendanceMinute(null);
	        }
	        //退勤（時）がnullではなく空文字でもなければフォームに値を格納する
	        if (leavingHourStr != null && !leavingHourStr.isEmpty()) {
	            form.setLeavingHour(Integer.parseInt(leavingHourStr));
	        } else {
	        	form.setLeavingHour(null);
	        }
	        //退勤（分）がnullではなく空文字でもなければフォームに値を格納する
	        if (leavingMinuteStr != null && !leavingMinuteStr.isEmpty()) {
	            form.setLeavingMinute(Integer.parseInt(leavingMinuteStr));
	        } else {
	        	form.setLeavingMinute(null);
	        }
	        //休憩（時）がnullではなく空文字でもなければフォームに値を格納する
	        if (restHourStr != null && !restHourStr.isEmpty()) {
	            form.setRestHour(Integer.parseInt(restHourStr));
	        } else {
	        	form.setRestHour(null);
	        }
	        //休憩（分）がnullではなく空文字でもなければフォームに値を格納する
	        if (restMinuteStr != null && !restMinuteStr.isEmpty()) {
	            form.setRestMinute(Integer.parseInt(restMinuteStr));
	        } else {
	        	form.setRestMinute(null);
	        }
	    }
	}
		
	/*----------------------------*/
	
}
