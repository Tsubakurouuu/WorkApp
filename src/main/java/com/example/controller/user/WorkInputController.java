package com.example.controller.user;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.controller.common.CommonController;
import com.example.model.MUser;
import com.example.model.Work;
import com.example.service.WorkService;
import com.example.service.impl.UserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WorkInputController {
	
	@Autowired
	private WorkService workService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	/*--出退勤入力画面のメソッド一覧--*/	
	
	//★出退勤時間入力画面に遷移するためのメソッド
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
	
	//★出勤ボタン押下時のメソッド
	@PostMapping("/work/attendance")
	public String postUserWorkAttendance(Work work, MUser loginUser, RedirectAttributes redirectAttributes) {
		//ログインユーザー情報取得
		loginUser = userDetailsServiceImpl.selectLoginUser();
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
		loginUser = userDetailsServiceImpl.selectLoginUser();
		//Workにユーザーを登録
		work.setUserId(loginUser.getId());
		//Workに現在年月日をセット
		work.setYear(calendar.get(Calendar.YEAR));
		work.setMonth(calendar.get(Calendar.MONTH) + 1);
		work.setDate(calendar.get(Calendar.DATE));
		//現在時間の取得
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		Integer minute = calendar.get(Calendar.MINUTE);
		//現在分を5捨6入するメソッド
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
	
	//★退勤ボタン押下時のメソッド
	@PostMapping("/work/leaving")
	public String postUserWorkLeaving(Work work, MUser loginUser, RedirectAttributes redirectAttributes) {
		//ログインユーザー情報取得
		loginUser = userDetailsServiceImpl.selectLoginUser();
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
		//本日分の出勤打刻が登録されているかの確認
		if(workInfo == null) {
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "出勤打刻が登録されていません。");
			//出退勤時間入力画面にリダイレクト
			return "redirect:/work/input";
		}
		//本日分の退勤打刻が登録されていないかの確認
		if(workInfo.getLeavingHour() != null) {
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "本日分の退勤打刻は既にされています。");
			//出退勤時間入力画面にリダイレクト
			return "redirect:/work/input";
		}
		//Workにユーザーを登録
		work.setUserId(loginUser.getId());
		//Workに現在年月日をセット
		work.setYear(year);
		work.setMonth(month);
		work.setDate(date);
		//現在時間の取得
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		Integer minute = calendar.get(Calendar.MINUTE);
		//現在分を5捨6入するメソッド
		Integer[] roundOff = CommonController.roundOff(hour, minute);
		//上記結果を元に退勤時間を登録
		work.setLeavingHour(roundOff[0]);
		work.setLeavingMinute(roundOff[1]);
		//休憩時間にはデフォルトで１時間０分をセット
		work.setRestHour(1);
		work.setRestMinute(0);
		//出勤時間と退勤時間から就業時間と残業時間を計算するメソッド
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
	
}
