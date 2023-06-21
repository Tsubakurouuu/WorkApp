package com.example.controller.admin;

import java.util.List;
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
  		// CSVデータのヘッダー
  		String[] header = {"日付", "出勤ステータス", "出勤時間", "退勤時間", "休憩時間", "就業時間", "残業時間"};
  		// CSVデータの内容を保持するStringJoiner
  		StringJoiner csvData = new StringJoiner("\n");
  		// ヘッダー行を追加
  		csvData.add(String.join(",", header));
  		//勤怠情報をcsvデータに変換
  		for (Work work : workList) {
  		    String day = work.getYear().toString() + "年" + work.getMonth().toString() + "月" + work.getDate().toString() + "日";
  		    String workStatus;
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
  		    	case 4:
  		    		workStatus = "半休";
  		    		break;
  		    	default:
  		    		workStatus = "その他のステータス";
  		    		break;
  		    }
  		    String attendance = work.getAttendanceHour() + "時" + work.getAttendanceMinute() + "分";
  		    String leaving = work.getLeavingHour() + "時" + work.getLeavingMinute() + "分";
  		    String rest = work.getRestHour() + "時間" + work.getRestMinute() + "分";
  		    String working = work.getWorkingTimeHour() + "時間" + work.getWorkingTimeMinute() + "分";
  		    String over = work.getOverTimeHour() + "時間" + work.getOverTimeMinute() + "分";

  		    String[] row = {day, workStatus, attendance, leaving, rest, working, over};
  		    csvData.add(String.join(",", row));
  		}
  		
  		//CSVデータを文字列として取得
  		String csvString = csvData.toString();
  		model.addAttribute("csvString", csvString);
  		return "admin/csv";
	}

}
