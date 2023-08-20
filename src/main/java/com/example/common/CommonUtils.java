package com.example.common;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.ui.Model;

import com.example.form.RequestFormForm;
import com.example.form.WorkEditForm;

public class CommonUtils {
	
	//★就業時間と残業時間を計算するメソッド
	public static int[] calcWorkingOver(int attendanceHour, int attendanceMinute, int leavingHour, int leavingMinute, int restHour, int restMinute) {
		//出勤時間、退勤時間、休憩時間を分換算する
		int totalAttendanceMinutes = attendanceHour * 60 + attendanceMinute;
        int totalLeavingMinutes = leavingHour * 60 + leavingMinute;
        int totalRestMinutes = restHour * 60 + restMinute;
        //就業時間を分換算する
        int totalWorkMinutes = totalLeavingMinutes - totalAttendanceMinutes - totalRestMinutes;
        //就業時間（時）の計算
        int workingHour = totalWorkMinutes / 60;
		//就業時間（分）の計算
		int workingMinute = totalWorkMinutes % 60;
        //定時の就業時間を設定
        int standardWorkHour = 8;
        int standardWorkMinute = 0;
        //定時の就業時間を分換算する
        int totalStandardMinutes = standardWorkHour * 60 + standardWorkMinute;
        //残業時間を分換算する
        int totalOvertimeMinutes = totalWorkMinutes - totalStandardMinutes;
        //就業時間が定時の就業時間を下回っていたら0を設定
        if (totalOvertimeMinutes < 0) {
            totalOvertimeMinutes = 0;
        }
        //残業時間（時）の計算
        int overTimeHour = totalOvertimeMinutes / 60;
        //残業時間（分）の計算
        int overTimeMinute = totalOvertimeMinutes % 60;
        //就業時間と残業時間を配列にする
        int[] calcWorkingOver = { workingHour, workingMinute, overTimeHour, overTimeMinute };
        return calcWorkingOver;
	}
	/*----------------------------*/
	
	
	
	//★5捨6入をして現在時分を返すメソッド
	public static int[] roundOff(int hour, int minute) {
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
		int[] roundOff = { hour, minute };
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
	public static int calcRest(int attendanceHour, int attendanceMinute, int leavingHour, int leavingMinute) {
		//出勤時間、退勤時間を分換算する
		int totalAttendanceMinutes = attendanceHour * 60 + attendanceMinute;
        int totalLeavingMinutes = leavingHour * 60 + leavingMinute;
        //出勤時間、退勤時間の分換算が60より大きいかどうかで処理を分岐
        if(totalLeavingMinutes - totalAttendanceMinutes > 60) {
        	//60より大きければ1を返す
        	return 1;
        } else {
        	//60以下なら0を返す
        	return 0;
        }
	}
	
	/*----------------------------*/
	
	
	//confirmWorkFormメソッドで使用する定数を宣言
	public static final int SUCCESS = 0;
	public static final int INCORRECT_TARGET_DATE = 1;
	public static final int INCOMPLETE_WORK_FORM = 2;
	public static final int INCORRECT_WORK_TIME = 3;
	public static final int INCORRECT_REST_TIME = 4;
	public static final int UNNECESSARY_WORK_FORM = 5;
	//★入力された出勤時間、退勤時間、休憩時間が時間軸として正しいかどうかを判断するメソッド
	public static int confirmWorkForm(Integer year, Integer month, Integer date, Integer workStatus, Integer attendanceHour, Integer attendanceMinute, Integer leavingHour, Integer leavingMinute, Integer restHour, Integer restMinute) {
		//カレンダークラスのオブジェクトを生成
		Calendar calendar = Calendar.getInstance();
		//現在の日付と対象の日付の比較(yyyyMMdd形式)
		int today = Integer.parseInt(String.format("%04d%02d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE)));
		int targetDay = Integer.parseInt(String.format("%04d%02d%02d", year, month, date));
		//出勤ステータスが「出勤」であれば
		if(workStatus == 1) {
			//現在の日付より対象の日付の方が大きければエラーを返す
			if(today < targetDay) {
				return INCORRECT_TARGET_DATE;
			}
			//時間入力フォームにnullがあればエラーを返す
			if(attendanceHour == null || attendanceMinute == null || leavingHour == null || leavingMinute == null || restHour == null || restMinute == null) {
                return INCOMPLETE_WORK_FORM;
            }
            //出勤時間、退勤時間、休憩時間を分換算する
            int totalAttendanceMinutes = attendanceHour * 60 + attendanceMinute;
        	int totalLeavingMinutes = leavingHour * 60 + leavingMinute;
       		int totalRestMinutes = restHour * 60 + restMinute;
			//出勤時間が退勤時間よりも遅い時間になっていたらエラーを返す
			if(totalAttendanceMinutes > totalLeavingMinutes) {
				return INCORRECT_WORK_TIME;
	        }
	        //出勤時間と退勤時間の差分よりも休憩時間の値が大きければエラーを返す
	        if(totalLeavingMinutes - totalAttendanceMinutes < totalRestMinutes) {
	        	return INCORRECT_REST_TIME;
	        }
	      //出勤ステータスが「出勤」以外であれば
	    } else {
	    	//時間入力フォームに値が入力されているとエラーになる
	    	if(attendanceHour != null || attendanceMinute != null || leavingHour != null || leavingMinute != null || restHour != null || restMinute != null) {
	    		return UNNECESSARY_WORK_FORM;
	    	}
	    }
	    //問題なければ処理を抜ける
        return SUCCESS;
	}
	
	/*----------------------------*/
	
	//★入力チェックエラー時にリダイレクトして値を保持するメソッド(出退勤申請画面)
	public static void formRedirect(RequestFormForm form, RequestFormForm formRedirect) {
		//formRedirectがnullかどうかの判定(フォーム入力時にエラーがあった場合にはformRedirectに値が格納されている)
		if (formRedirect != null) {
			//フォームに値が入っていれば各変数に値を格納する(入っていなければnullが入る)
			String workStatusStr = formRedirect.getWorkStatus() != null ? formRedirect.getWorkStatus().toString() : null;
	        String attendanceHourStr = formRedirect.getAttendanceHour() != null ? formRedirect.getAttendanceHour().toString() : null;
	        String attendanceMinuteStr = formRedirect.getAttendanceMinute() != null ? formRedirect.getAttendanceMinute().toString() : null;
	        String leavingHourStr = formRedirect.getLeavingHour() != null ? formRedirect.getLeavingHour().toString() : null;
	        String leavingMinuteStr = formRedirect.getLeavingMinute() != null ? formRedirect.getLeavingMinute().toString() : null;
	        String restHourStr = formRedirect.getRestHour() != null ? formRedirect.getRestHour().toString() : null;
	        String restMinuteStr = formRedirect.getRestMinute() != null ? formRedirect.getRestMinute().toString() : null;
	        //出勤ステータスがnullではなく空文字でもなければフォームに値を格納する
	        if (workStatusStr != null && !workStatusStr.isEmpty()) {
	        	form.setWorkStatus(Integer.parseInt(workStatusStr));
	        }
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
	
	
	
	
	//★入力チェックエラー時にリダイレクトして値を保持するメソッド(出退勤修正画面)
	public static void formRedirect(WorkEditForm form, WorkEditForm formRedirect) {
		//formRedirectがnullかどうかの判定(フォーム入力時にエラーがあった場合にはformRedirectに値が格納されている)
		if (formRedirect != null) {
			//フォームに値が入っていれば各変数に値を格納する(入っていなければnullが入る)
			String workStatusStr = formRedirect.getWorkStatus() != null ? formRedirect.getWorkStatus().toString() : null;
	        String attendanceHourStr = formRedirect.getAttendanceHour() != null ? formRedirect.getAttendanceHour().toString() : null;
	        String attendanceMinuteStr = formRedirect.getAttendanceMinute() != null ? formRedirect.getAttendanceMinute().toString() : null;
	        String leavingHourStr = formRedirect.getLeavingHour() != null ? formRedirect.getLeavingHour().toString() : null;
	        String leavingMinuteStr = formRedirect.getLeavingMinute() != null ? formRedirect.getLeavingMinute().toString() : null;
	        String restHourStr = formRedirect.getRestHour() != null ? formRedirect.getRestHour().toString() : null;
	        String restMinuteStr = formRedirect.getRestMinute() != null ? formRedirect.getRestMinute().toString() : null;
	        //出勤ステータスがnullではなく空文字でもなければフォームに値を格納する
	        if (workStatusStr != null && !workStatusStr.isEmpty()) {
	        	form.setWorkStatus(Integer.parseInt(workStatusStr));
	        } else {
	        	form.setWorkStatus(null);
	        }
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
