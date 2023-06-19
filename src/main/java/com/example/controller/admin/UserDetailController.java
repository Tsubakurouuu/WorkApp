package com.example.controller.admin;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
public class UserDetailController {
	
	@Autowired
	private MUserService userService;
	
	@Autowired
	private WorkService workService;
	
	/*--ユーザー詳細画面のメソッド一覧--*/
	
	//ユーザー詳細画面に遷移するための処理
	@GetMapping("/{userId:.+}/{year}/{month}")
	public String getAdminUserDetail(@PathVariable("userId") String userId, Integer id, @PathVariable("year") Integer year, @PathVariable("month") Integer month, Model model) {
		//年と月が指定されていない場合、現在の年と月を取得
		if (year == null || month == null) {
			Calendar calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH) + 1;
	    }
		//ユーザーを1件取得
		MUser userDetail = userService.selectUserDetail(userId);
		//Modelに登録
		model.addAttribute("userDetail", userDetail);
		//勤怠情報月毎取得
		List<Work> workList = workService.selectWorkListMonth(userDetail.getId(), year, month);
		//各月の最終日にちを取得
		Integer lastDateOfMonth = YearMonth.of(year, month).lengthOfMonth();
		//日付ごとの勤怠情報をMapに変換する
	    Map<Integer, Work> workMap = new HashMap<>();
	    for (Work work : workList) {
	    	workMap.put(work.getDate(), work);
	    }
	    // 曜日情報を作成
	    List<String> dayOfWeekList = new ArrayList<>();
	    for (int date = 1; date <= lastDateOfMonth; date++) {
	        LocalDate localDate = LocalDate.of(year, month, date);
	        String dayOfWeek = localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPANESE);
	        dayOfWeekList.add(dayOfWeek);
	    }
	    //Modelに登録
		model.addAttribute("workMap", workMap);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("lastDateOfMonth", lastDateOfMonth);
		model.addAttribute("dayOfWeekList", dayOfWeekList); 
		//admin/user_detail.htmlを呼び出す
		return "admin/user_detail";
	}
	
	//先月ボタン(◀︎)押下時の処理
	@GetMapping("/{userId}/{year}/{month}/previous")
	public String showPreviousMonthAttendance(@PathVariable("userId") String userId, @PathVariable("year") Integer year, @PathVariable("month") Integer month) {
		//1ヶ月前の年と月を計算
	    if (month == 1) {
	    	year--;
	    	month = 12;
	    } else {
	    	month--;
	    }
	    //指定した年月日画面へリダイレクト
	    return "redirect:/admin/" + userId + "/" + year + "/" + month;
	}
	
	//翌月ボタン(▶︎)押下時の処理
	@GetMapping("/{userId}/{year}/{month}/next")
	public String showNextMonthAttendance(@PathVariable("userId") String userId, @PathVariable("year") Integer year, @PathVariable("month") Integer month) {
		//1ヶ月後の年と月を計算
	    if (month == 12) {
	    	year++;
	    	month = 1;
	    } else {
	    	month++;
	    }
	    //指定した年月日画面へリダイレクト
	    return "redirect:/admin/" + userId + "/" + year + "/" + month;
	}
	
	/*----------------------------*/
	

}
