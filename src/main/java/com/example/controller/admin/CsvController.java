package com.example.controller.admin;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.model.MUser;
import com.example.model.Work;
import com.example.service.MUserService;
import com.example.service.WorkService;

@Controller
@RequestMapping("/admin")
public class CsvController {
	
	@Autowired
	private MUserService userService;
	
	@Autowired
	private WorkService workService;
	
	/*--csv出力画面のメソッド一覧--*/
	
	//★csv出力画面に遷移するためのメソッド
	@GetMapping("/{userId:.+}/{year}/{month}/csv")
	public String downloadCsv(HttpServletResponse response, @PathVariable("userId") String userId, @PathVariable("year") Integer year, @PathVariable("month") Integer month, Model model) {
		//レスポンスの設定("text/csv"とすることでcsv形式のテキストデータであることを認識する)
        response.setContentType("text/csv");
        //レスポンスの設定(Content-Disposition:ブラウザに対してレスポンスの内容の扱い方を伝えるために使用される※attachmentとすることでレスポンスの内容を「添付ファイル」としてダウンロードすることをブラウザに指示する)
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userId + "_" + year + "_" + month + "\"");
        //ユーザーを1件取得
  		MUser userDetail = userService.selectUserDetail(userId);
        //勤怠情報月毎取得
  		List<Work> workList = workService.selectWorkListMonth(userDetail.getId(), year, month);
  		//各月の最終日にちを取得
		Integer lastDateOfMonth = YearMonth.of(year, month).lengthOfMonth();
		//日付をキーとして勤怠情報を格納するための空のハッシュマップを作成
	    Map<Integer, Work> workMap = new HashMap<>();
	    //勤怠情報のリストworkList内の各Workオブジェクトに対して、繰り返し処理を行う
	    for (Work work : workList) {
	    	//日付をキーとして、該当する勤怠情報をマップに格納します。
	    	workMap.put(work.getDate(), work);
	    }
	    //曜日情報を格納するための空の文字列リストを作成
	    List<String> dayOfWeekList = new ArrayList<>();
	    //1から月の最終日までの各日に対して繰り返し処理
	    for (int date = 1; date <= lastDateOfMonth; date++) {
	    	//指定された年月日を使用してLocalDateオブジェクトを作成
	        LocalDate localDate = LocalDate.of(year, month, date);
	        //LocalDateオブジェクトから曜日情報を取得(getDayOfWeek()メソッドは曜日を表すDayOfWeek列挙型返し、getDisplayName()メソッドを使用して日本語の短縮形式の曜日名を取得する)
	        String dayOfWeek = localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPANESE);
	        //取得した曜日情報をdayOfWeekListに追加
	        dayOfWeekList.add(dayOfWeek);
	    }
	    //ヘッダーの配列を生成
  		String[] header = {"日付", "曜日", "出勤ステータス", "出勤時間", "退勤時間", "休憩時間", "就業時間", "残業時間"};
  		// CSVデータの内容を保持するStringJoiner
  		StringJoiner csvData = new StringJoiner("\n");
  		// ヘッダー行を追加
  		csvData.add(String.join(",", header));
  		//勤怠情報をcsvデータに変換(該当年月の1日から最終日にちまでのfor文)
  		for (Integer i = 1; i <= lastDateOfMonth; i++) {
  			//ユーザーID,年月日から出勤情報があるかを探す
            Work work = workService.selectWorkAttendance(userDetail.getId(), year, month, i);
            //年月日
            String day = year + "年" + month + "月" + i + "日";
            //dayOfWeekListから該当の曜日を選択
            String dayOfWeek = dayOfWeekList.get(i - 1);
            //各変数の宣言
            String workStatus;
            String attendance;
            String leaving;
            String rest;
            String working;
            String over;
            //該当年月日の勤怠情報がなかった時の処理
            if(work == null) {
            	//土日であれば休日と表示
            	if(dayOfWeek.equals("土") || dayOfWeek.equals("日")) {
            		workStatus = "休日";
            	//平日であれば出勤と表示
            	} else {
            		workStatus = "出勤";
            	}
            	//勤怠情報がなかったら各ステータスに-を表示
            	attendance = "----";
            	leaving = "----";
            	rest = "----";
            	working = "----";
            	over = "----";
            //該当年月日の勤怠情報があった時の処理
            } else {
            	//出勤ステータスのSwitch文
            	switch(work.getWorkStatus()) {
            	case 1:
            		workStatus = "出勤";
            		break;
            	case 2:
            		workStatus = "欠勤";
            		break;
            	case 3:
            		workStatus = "有休";
            		break;
            	default:
            		workStatus = "その他のステータス";
            		break;
            	}
            	//出勤時間がnullかどうかで処理を分岐(csvファイルにnullと表示されるのを防ぐ)
            	if(work.getAttendanceHour() == null || work.getAttendanceMinute() == null) {
            		attendance = "----";
            	} else {
            		attendance = work.getAttendanceHour() + "時" + work.getAttendanceMinute() + "分";
            	}
            	//退勤時間がnullかどうかで処理を分岐(csvファイルにnullと表示されるのを防ぐ)
            	if(work.getLeavingHour() == null || work.getLeavingMinute() == null) {
            		leaving = "----";
            	} else {
            		leaving = work.getLeavingHour() + "時" + work.getLeavingMinute() + "分";
            	}
            	//休憩時間がnullかどうかで処理を分岐(csvファイルにnullと表示されるのを防ぐ)
            	if(work.getRestHour() == null || work.getRestMinute() == null) {
            		rest = "----";
            	} else {
            		rest = work.getRestHour() + "時" + work.getRestMinute() + "分";
            	}
            	//就業時間がnullかどうかで処理を分岐(csvファイルにnullと表示されるのを防ぐ)
            	if(work.getWorkingTimeHour() == null || work.getWorkingTimeMinute() == null) {
            		working = "----";
            	} else {
            		working = work.getWorkingTimeHour() + "時" + work.getWorkingTimeMinute() + "分";
            	}
            	//残業時間がnullかどうかで処理を分岐(csvファイルにnullと表示されるのを防ぐ)
            	if(work.getOverTimeHour() == null || work.getOverTimeMinute() == null) {
            		over = "----";
            	} else {
            		over = work.getOverTimeHour() + "時" + work.getOverTimeMinute() + "分";
            	}
            }
            //該当年月日の勤怠データを配列にして格納する
		    String[] row = {day, dayOfWeek, workStatus, attendance, leaving, rest, working, over};
		    csvData.add(String.join(",", row));
  		}
  		//CSVデータを文字列として取得
  		String csvString = csvData.toString();
  		//Modelに登録
  		model.addAttribute("csvString", csvString);
  		model.addAttribute("userDetail", userDetail);
  		model.addAttribute("year", year);
  		model.addAttribute("month", month);
  		return "admin/csv";
	}

}
