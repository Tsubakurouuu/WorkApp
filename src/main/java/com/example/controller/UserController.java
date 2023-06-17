package com.example.controller;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.application.service.WorkStatusService;
import com.example.form.GroupOrder;
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
	
	@Autowired
	private WorkStatusService workStatusService;
	
	/*--出退勤入力画面のメソッド一覧--*/	
	
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
	public String postUserWorkAttendance(Work work, MUser loginUser, RedirectAttributes redirectAttributes) {
		//ログインユーザー情報取得
		loginUser = userDetailsServiceImpl.getLoginUser();
		//カレンダークラスのオブジェクトを生成
		Calendar calendar = Calendar.getInstance();
		//現在の年を取得
        Integer year = calendar.get(Calendar.YEAR);
        //現在の月を取得
        Integer month = calendar.get(Calendar.MONTH) + 1;
        //現在の日を取得
        Integer date = calendar.get(Calendar.DATE);
        //同日勤怠情報取得（退勤ボタン押下時）
		Work workInfo = workService.selectWorkAttendance(loginUser.getId(), year, month, date);
		//本日分の打刻登録がされていないかの確認
		if(workInfo != null) {
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "本日分の出勤打刻は既にされています。");
			//出退勤時間入力画面にリダイレクト
			return "redirect:/work/input";
		}
		//ログインユーザー情報取得
		loginUser = userDetailsServiceImpl.getLoginUser();
		//Workにユーザーを登録
		work.setUserId(loginUser.getId());
		//Workに現在日をセット
		work.setYear(calendar.get(Calendar.YEAR));
		work.setMonth(calendar.get(Calendar.MONTH) + 1);
		work.setDate(calendar.get(Calendar.DATE));
		//現在時間の取得
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		Integer minute = calendar.get(Calendar.MINUTE);
		//5捨6入するメソッド
		Integer[] roundOff = CommonController.roundOff(hour, minute);
		//上記結果を元に出勤時間を登録
		work.setAttendanceHour(roundOff[0]);
		work.setAttendanceMinute(roundOff[1]);
		//出勤ステータスを【出勤】で登録
		work.setWorkStatus(1);
		//出勤時間登録
		workService.insertAttendance(work);
		//ログを表示
		log.info(work.toString());
		//フラッシュスコープ
		redirectAttributes.addFlashAttribute("complete", "出勤打刻が完了しました。");
		//出退勤時間入力画面にリダイレクト
		return "redirect:/work/input";
	}
	
	//退勤ボタン押下時の処理
	@PostMapping("/work/leaving")
	public String postUserWorkLeaving(Work work, Model model, MUser loginUser, RedirectAttributes redirectAttributes) {
		//ログインユーザー情報取得
		loginUser = userDetailsServiceImpl.getLoginUser();
		//カレンダークラスのオブジェクトを生成
		Calendar calendar = Calendar.getInstance();
		//現在の年を取得
		Integer year = calendar.get(Calendar.YEAR);
		//現在の月を取得
		Integer month = calendar.get(Calendar.MONTH) + 1;
		//現在の日を取得
		Integer date = calendar.get(Calendar.DATE);
		//同日勤怠情報取得（退勤ボタン押下時）
		Work workInfo = workService.selectWorkAttendance(loginUser.getId(), year, month, date);
		//出勤打刻が登録されているかの確認
		if(workInfo == null) {
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "出勤打刻が登録されていません。");
			//出退勤時間入力画面にリダイレクト
			return "redirect:/work/input";
		}
		//本日分の打刻登録がされていないかの確認
		if(workInfo.getLeavingHour() != null) {
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "本日分の退勤打刻は既にされています。");
			//出退勤時間入力画面にリダイレクト
			return "redirect:/work/input";
		}
		//Workにユーザーを登録
		work.setUserId(loginUser.getId());
		//Workに現在日をセット
		work.setYear(year);
		work.setMonth(month);
		work.setDate(date);
		//現在時間の取得
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		Integer minute = calendar.get(Calendar.MINUTE);
		//5捨6入するメソッド
		Integer[] roundOff = CommonController.roundOff(hour, minute);
		//上記結果を元に退勤時間を登録	
		work.setLeavingHour(roundOff[0]);
		work.setLeavingMinute(roundOff[1]);
		//休憩時間にはデフォルトで１時間０分をセット
		work.setRestHour(1);
		work.setRestMinute(0);
		//就業時間と残業時間を計算するメソッド
		Integer[] calcWorkingOver = CommonController.calcWorkingOver(workInfo.getAttendanceHour(), workInfo.getAttendanceMinute(), work.getLeavingHour(), work.getLeavingMinute(), work.getRestHour(),work.getRestMinute());
		//上記結果を元に就業時間と残業時間を登録
		work.setWorkingTimeHour(calcWorkingOver[0]);
		work.setWorkingTimeMinute(calcWorkingOver[1]);
		work.setOverTimeHour(calcWorkingOver[2]);
		work.setOverTimeMinute(calcWorkingOver[3]);
		//ログを表示
		log.info(work.toString());
		//フラッシュスコープ
		redirectAttributes.addFlashAttribute("complete", "退勤打刻が完了しました。");
		//退勤時間登録（更新）
		workService.updateLeaving(work);
		//出退勤時間入力画面にリダイレクト
		return "redirect:/work/input";
	}
	
	/*----------------------------*/
	
	
	
	/*--出退勤一覧画面のメソッド一覧--*/
	
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
		//user/work_index.htmlを呼び出す
		return "user/work_index";
	}
	
	//先月ボタン(◀︎)押下時の処理
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
	
	//翌月ボタン(▶︎)押下時の処理
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
		
	/*----------------------------*/
		
		
		
	/*--出退勤申請画面のメソッド一覧--*/	
	
	//出退勤申請画面に遷移するための処理
	@GetMapping("/form/{id}")
	public String getUserForm(@ModelAttribute RequestFormForm form, @PathVariable("id") Integer id, Model model, MUser loginUser) {
		//勤怠情報取得
		Work workDetail = workService.selectWork(id);
		//Modelに登録
		model.addAttribute("workDetail", workDetail);
		//RequestFormのworkIdカラムに値をセット
		form.setWorkId(id);
		//出勤ステータスのMap
		Map<String, Integer> workStatusMap = workStatusService.getWorkStatusMap();
		//Modelに登録
		model.addAttribute("workStatusMap", workStatusMap);
		model.addAttribute("year", workDetail.getYear());
		model.addAttribute("month", workDetail.getMonth());
		model.addAttribute("date", workDetail.getDate());
		//時分フォーム入力用メソッド
		CommonController.formNumbers(model);
		//user/form.htmlを呼び出す
		return "user/form";
	}
	
	@GetMapping("/form/{year}/{month}/{date}")
	public String getUserForm(@ModelAttribute RequestFormForm form, @PathVariable("year") Integer year, @PathVariable("month") Integer month, @PathVariable("date") Integer date, Model model, MUser loginUser) {
//		//勤怠情報取得
//		Work workDetail = workService.selectWork(id);
//		//Modelに登録
//		model.addAttribute("workDetail", workDetail);
//		//RequestFormのworkIdカラムに値をセット
//		form.setWorkId(id);
		//出勤ステータスのMap
		Map<String, Integer> workStatusMap = workStatusService.getWorkStatusMap();
		//Modelに登録
		model.addAttribute("workStatusMap", workStatusMap);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("date", date);
		//時分フォーム入力用メソッド
		CommonController.formNumbers(model);
		//user/form.htmlを呼び出す
		return "user/form";
	}
	
	//確認画面へボタン押下時の処理
	@PostMapping("/form/confirm")
	public String postUserFormConfirm(@ModelAttribute @Validated(GroupOrder.class) RequestFormForm form, BindingResult bindingResult, Integer id, Model model, RedirectAttributes redirectAttributes) {
		//勤怠情報取得
		Work workDetail = workService.selectWork(id);
		if(workDetail != null) {
			//年月日をformにセット
			form.setYear(workDetail.getYear());
			form.setMonth(workDetail.getMonth());
			form.setDate(workDetail.getDate());
			//Modelに登録
			model.addAttribute("workDetail", workDetail);
			model.addAttribute("year", workDetail.getYear());
			model.addAttribute("month", workDetail.getMonth());
			model.addAttribute("date", workDetail.getDate());
		}
		//出勤ステータスのMap
		Map<String, Integer> workStatusMap = workStatusService.getWorkStatusMap();
		//Modelに登録
		model.addAttribute("workStatusMap", workStatusMap);
		//入力チェック結果
		if(bindingResult.hasErrors()) {
			//NGがあれば出退勤申請画面に戻る
			return "user/form";
		}
		//user/form_confirm.htmlを呼び出す
		return "user/form_confirm";
	}
	
	/*----------------------------*/
	
		
		
	/*--出退勤申請確認画面のメソッド一覧--*/
	
	//出退勤申請確認画面に遷移するための処理
	@GetMapping("/form/confirm")
	public String getUserFormConfirm(@ModelAttribute RequestFormForm form, Integer id, Model model, Integer year, Integer month, Integer date, Work workDetail) {
		//勤怠情報取得
		workDetail = workService.selectWork(id);
		if(workDetail != null) {
			//Modelに登録
			model.addAttribute("workDetail", workDetail);
		}
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("date", date);
		//user/form_confirm.htmlを呼び出す
		return "user/form_confirm";
	}
	
	//↓エラー発生
	//戻るボタン押下時の処理
	@PostMapping("/form/{id}")
	public String postUserForm(@ModelAttribute RequestFormForm form, Model model, Integer year, Integer month, Integer date, @PathVariable("id") Integer id) {
		//勤怠情報取得
		Work workDetail = workService.selectWork(id);
		if(workDetail != null) {
			//年月日をformにセット
			form.setYear(workDetail.getYear());
			form.setMonth(workDetail.getMonth());
			form.setDate(workDetail.getDate());
			//Modelに登録
			model.addAttribute("workDetail", workDetail);
			model.addAttribute("year", workDetail.getYear());
			model.addAttribute("month", workDetail.getMonth());
			model.addAttribute("date", workDetail.getDate());
		}
		//出勤ステータスのMap
		Map<String, Integer> workStatusMap = workStatusService.getWorkStatusMap();
		//Modelに登録
		model.addAttribute("workStatusMap", workStatusMap);
		//時分フォーム入力用メソッド
		CommonController.formNumbers(model);
		//user/form.htmlを呼び出す
		return "user/form";
	}
	
	//申請ボタン押下時の処理
	@PostMapping("/form/complete")
	public String postUserFormComplete(@ModelAttribute RequestFormForm form, MUser loginUser, Model model, RedirectAttributes redirectAttributes, Integer year, Integer month) {
		//ログインユーザー情報取得
		loginUser = userDetailsServiceImpl.getLoginUser();
		//フラッシュスコープ
		redirectAttributes.addFlashAttribute("complete", "出退勤修正の申請が完了しました。");
		//formをRequestFormクラスに変換
		RequestForm requestForm = new RequestForm();
		requestForm.setUserId(loginUser.getId());
		requestForm.setWorkId(form.getWorkId());
		requestForm.setYear(form.getYear());
		requestForm.setMonth(form.getMonth());
		requestForm.setDate(form.getDate());
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
		//カレンダークラスのオブジェクトを作成
		Calendar calendar = Calendar.getInstance();
		//現在の年を取得
		year = calendar.get(Calendar.YEAR);
		//現在の月を取得
		month = calendar.get(Calendar.MONTH) + 1;
		//Modelに登録
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		//ログを表示
		log.info(form.toString());
		//出退勤一覧画面にリダイレクト
		return "redirect:/work/" + year + "/" + month;
	}
	
	/*----------------------------*/
	
}
