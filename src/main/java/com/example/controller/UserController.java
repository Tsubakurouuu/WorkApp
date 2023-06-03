package com.example.controller;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.form.RequestFormForm;
import com.example.model.MUser;
import com.example.model.RequestForm;
import com.example.model.Work;
import com.example.service.RequestFormService;
import com.example.service.WorkService;
import com.example.service.impl.UserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {
	@Autowired
	private WorkService workService;
	
	@Autowired
	private RequestFormService requestFormService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	//出退勤申請画面に遷移するための処理
	@GetMapping("/form/{id}")
	public String getUserForm(@ModelAttribute RequestFormForm form, @PathVariable("id") Integer id, Model model, MUser loginUser) {
		//勤怠情報取得
		Work workDetail = workService.selectWork(id);
		//Modelに登録
		model.addAttribute("workDetail", workDetail);
		//RequestFormのworkIdカラムに値をセット
		form.setWorkId(id);
		//user/form.htmlを呼び出す
		return "user/form";
	}
	
	//確認画面へボタン押下時の処理
	@PostMapping("/form/confirm")
	public String postUserFormConfirm(@ModelAttribute RequestFormForm form) {
		//user/form_confirm.htmlを呼び出す
		return "user/form_confirm";
	}
	
	//出退勤申請確認画面に遷移するための処理
	@GetMapping("/form/confirm")
	public String getUserFormConfirm(@ModelAttribute RequestFormForm form) {
		//user/form_confirm.htmlを呼び出す
		return "user/form_confirm";
	}
	
	//↓エラー発生
	//戻るボタン押下時の処理
	@PostMapping("/form")
	public String postUserForm(@ModelAttribute RequestFormForm form) {
		//user/form.htmlを呼び出す
		return "user/form";
	}
	
	//申請ボタン押下時の処理
	@PostMapping("/form/complete")
	public String postUserFormComplete(@ModelAttribute RequestFormForm form, MUser loginUser, Model model, RedirectAttributes redirectAttributes) {
		//フラッシュスコープ
		redirectAttributes.addFlashAttribute("complete", "申請が完了しました。");
		//formをRequestFormクラスに変換
		RequestForm requestForm = new RequestForm();
		requestForm.setWorkId(form.getWorkId());
		requestForm.setWorkStatus(form.getWorkStatus());
		requestForm.setAttendanceHour(form.getAttendanceHour());
		requestForm.setAttendanceMinute(form.getAttendanceMinute());
		requestForm.setLeavingHour(form.getLeavingHour());
		requestForm.setLeavingMinute(form.getLeavingMinute());
		requestForm.setRestHour(form.getRestHour());
		requestForm.setRestMinute(form.getRestMinute());
		requestForm.setComment(form.getComment());
		//申請フォーム登録
		requestFormService.insertForm(requestForm);
		//ログを表示
		log.info(form.toString());
		//出退勤一覧画面にリダイレクト
		return "redirect:/works";
	}
	
	//出退勤時間入力画面に遷移するための処理
	@GetMapping("/work/input")
	public String getUserWorkInput(Model model, Integer year, Integer month) {
		//カレンダークラスのオブジェクトを生成
		Calendar calendar = Calendar.getInstance();
		//現在の年を取得
        year = calendar.get(Calendar.YEAR);
        //現在の月を取得
        month = calendar.get(Calendar.MONTH) + 1;
        //現在の日を取得
        Integer date = calendar.get(Calendar.DATE);
        //現在の曜日を取得
        Integer week = calendar.get(Calendar.DAY_OF_WEEK);
        //曜日をModelに登録
        switch (week) {
        case Calendar.SUNDAY:
        	model.addAttribute("week", "日");
            break;
        case Calendar.MONDAY:
        	model.addAttribute("week", "月");
            break;
        case Calendar.TUESDAY:
        	model.addAttribute("week", "火");
            break;
        case Calendar.WEDNESDAY:
        	model.addAttribute("week", "水");
            break;
        case Calendar.THURSDAY:
        	model.addAttribute("week", "木");
            break;
        case Calendar.FRIDAY:
        	model.addAttribute("week", "金");
            break;
        case Calendar.SATURDAY:
        	model.addAttribute("week", "土");
            break;
        }
        //Modelに登録
        model.addAttribute("year", year);
		model.addAttribute("month", month);	
		model.addAttribute("date", date);
		//user/work_input.htmlを呼び出す
		return "user/work_input";
	}
	
	//出勤ボタン押下時の処理
	@PostMapping("/work/attendance")
	public String postUserWorkAttendance(Work work, MUser loginUser) {
		//ログインユーザー情報取得
		loginUser = userDetailsServiceImpl.getLoginUser();
		//Workにユーザーを登録
		work.setUserId(loginUser.getId());
		//出勤時間登録
		workService.insertAttendance(work);
		//ログを表示
		log.info(work.toString());
		//出退勤時間入力画面にリダイレクト
		return "redirect:/work/input";
	}
	
	//退勤ボタン押下時の処理
	@PostMapping("/work/leaving")
	public String postUserWorkLeaving(Work work, Model model, MUser loginUser) {
		//ログインユーザー情報取得
		loginUser = userDetailsServiceImpl.getLoginUser();
		//Workにユーザーを登録
		work.setUserId(loginUser.getId());
		//カレンダークラスのオブジェクトを生成
		Calendar calendar = Calendar.getInstance();
		//Workに現在日時をセット
		work.setYear(calendar.get(Calendar.YEAR));
		work.setMonth(calendar.get(Calendar.MONTH) + 1);
		work.setDate(calendar.get(Calendar.DATE));
		work.setLeavingHour(calendar.get(Calendar.HOUR_OF_DAY));
		int c = calendar.get(Calendar.HOUR_OF_DAY);
		//5捨6入処理
		if(calendar.get(Calendar.MINUTE) >= 6 && calendar.get(Calendar.MINUTE) <= 15) {
			work.setLeavingMinute(10);
		} else if(calendar.get(Calendar.MINUTE) >= 16 && calendar.get(Calendar.MINUTE) <= 25) {
			work.setLeavingMinute(20);
		} else if(calendar.get(Calendar.MINUTE) >= 26 && calendar.get(Calendar.MINUTE) <= 35) {
			work.setLeavingMinute(30);
		} else if(calendar.get(Calendar.MINUTE) >= 36 && calendar.get(Calendar.MINUTE) <= 45) {
			work.setLeavingMinute(40);
		} else if(calendar.get(Calendar.MINUTE) >= 46 && calendar.get(Calendar.MINUTE) <= 55) {
			work.setLeavingMinute(50);
		}else if(calendar.get(Calendar.MINUTE) <= 5) {
			work.setLeavingMinute(0);
		}else if(calendar.get(Calendar.MINUTE) >= 56) {
			work.setLeavingHour(calendar.get(Calendar.HOUR_OF_DAY) + 1);
			work.setLeavingMinute(0);
		}
		work.setRestHour(1);
		work.setRestMinute(0);
		//現在の年を取得
        Integer year = calendar.get(Calendar.YEAR);
        //現在の月を取得
        Integer month = calendar.get(Calendar.MONTH) + 1;
        //現在の日を取得
        Integer date = calendar.get(Calendar.DATE);
        //同日勤怠情報取得（退勤ボタン押下時）
		Work workInfo = workService.selectWorkAttendance(loginUser.getId(), year, month, date);
		//就業時間と残業時間を計算する処理
		work.setWorkingTimeHour(work.getLeavingHour() - workInfo.getAttendanceHour());
		if(work.getLeavingMinute() - workInfo.getAttendanceMinute() >= 0) {
			work.setWorkingTimeMinute(work.getLeavingMinute() - workInfo.getAttendanceMinute());
		} else if (work.getLeavingMinute() - workInfo.getAttendanceMinute() < 0) {
			work.setWorkingTimeMinute(-(work.getLeavingMinute() - workInfo.getAttendanceMinute()));
		}
		work.setOverTimeHour(work.getLeavingHour() - workInfo.getAttendanceHour() - 8);
		if(work.getLeavingMinute() - workInfo.getAttendanceMinute() >= 0) {
			work.setOverTimeMinute(work.getLeavingMinute() - workInfo.getAttendanceMinute());
		} else if (work.getLeavingMinute() - workInfo.getAttendanceMinute() < 0) {
			work.setOverTimeMinute(-(work.getLeavingMinute() - workInfo.getAttendanceMinute()));
		}
		//ログを表示
		log.info(work.toString());
		//退勤時間登録（更新）
		workService.updateLeaving(work);
		//出退勤時間入力画面にリダイレクト
		return "redirect:/work/input";
	}
	
	//出退勤一覧画面に遷移するための処理
	@GetMapping("/work/{year}/{month}")
	public String getUserWorkIndex(Integer userId, @PathVariable("year") Integer year, @PathVariable("month") Integer month, Model model, MUser loginUser) {
		//年と月が指定されていない場合、現在の年と月を取得
		if (year == null || month == null) {
			Calendar calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH) + 1;
	    }
		//ログインユーザー情報取得
		loginUser = userDetailsServiceImpl.getLoginUser();
		//Modelに登録
		model.addAttribute("loginUser", loginUser);
		//ログインユーザーのIDを取得
		userId = loginUser.getId();
		//勤怠情報月毎取得
		List<Work> workList = workService.selectWorkListMonth(userId, year, month);
		//各月の最終日にちを取得
		Integer lastDateOfMonth = YearMonth.of(year, month).lengthOfMonth();
		//日付ごとの勤怠情報をMapに変換する
	    Map<Integer, Work> workMap = new HashMap<>();
	    for (Work work : workList) {
	    	workMap.put(work.getDate(), work);
	    }
		//Modelに登録
		model.addAttribute("workMap", workMap);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("lastDateOfMonth", lastDateOfMonth);
		//user/work_index.htmlを呼び出す
		return "user/work_index";
	}
	
	//先月ボタン押下時の処理
	@GetMapping("/work/{year}/{month}/previous")
	public String showPreviousMonthAttendance(@PathVariable("year") Integer year, @PathVariable("month") Integer month) {
		//1ヶ月前の年と月を計算
	    if (month == 1) {
	    	year--;
	    	month = 12;
	    } else {
	    	month--;
	    }
	    //指定した年月日画面へリダイレクト
	    return "redirect:/work/" + year + "/" + month;
	}
	
	//翌月ボタン押下時の処理
	@GetMapping("/work/{year}/{month}/next")
	public String showNextMonthAttendance(@PathVariable("year") Integer year, @PathVariable("month") Integer month) {
		//1ヶ月後の年と月を計算
	    if (month == 12) {
	    	year++;
	    	month = 1;
	    } else {
	    	month++;
	    }
	    //指定した年月日画面へリダイレクト
	    return "redirect:/work/" + year + "/" + month;
	}
}
