package com.example.controller.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.application.service.WorkStatusService;
import com.example.controller.common.CommonController;
import com.example.form.GroupOrder;
import com.example.form.WorkEditForm;
import com.example.model.MUser;
import com.example.model.Work;
import com.example.service.MUserService;
import com.example.service.WorkService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class UserWorkEditController {
	
	@Autowired
	private MUserService userService;
	
	@Autowired
	private WorkService workService;
	
	@Autowired
	private WorkStatusService workStatusService;
	
	/*--出退勤修正画面のメソッド一覧--*/
	
	//★出退勤修正画面に遷移するためのメソッド(勤怠情報登録時)
	@GetMapping("/{id}/edit")
	public String getAdminUserWorkEdit(@ModelAttribute WorkEditForm form, @PathVariable("id") Integer id, Model model, @ModelAttribute("model") ModelMap modelMap) {
		//勤怠情報取得
		Work workDetail = workService.selectWork(id);
		form.setId(workDetail.getId());
		form.setUserId(workDetail.getUserId());
		form.setYear(workDetail.getYear());
		form.setMonth(workDetail.getMonth());
		form.setDate(workDetail.getDate());
		form.setAttendanceHour(workDetail.getAttendanceHour());
		form.setAttendanceMinute(workDetail.getAttendanceMinute());
		form.setLeavingHour(workDetail.getLeavingHour());
		form.setLeavingMinute(workDetail.getLeavingMinute());
		form.setRestHour(workDetail.getRestHour());
		form.setRestMinute(workDetail.getRestMinute());
		form.setWorkingTimeHour(workDetail.getWorkingTimeHour());
		form.setWorkingTimeMinute(workDetail.getWorkingTimeMinute());
		form.setOverTimeHour(workDetail.getOverTimeHour());
		form.setOverTimeMinute(workDetail.getOverTimeMinute());
		form.setWorkStatus(workDetail.getWorkStatus());
		//出勤ステータスのMap
		Map<String, Integer> workStatusMap = workStatusService.getWorkStatusMap();
		//Modelに登録
		model.addAttribute("workStatusMap", workStatusMap);
		model.addAttribute("workDetail", workDetail);
		//エラー時にリダイレクトされてきた値を受け取る
		WorkEditForm formRedirect = (WorkEditForm) modelMap.get("form");
		//formRedirectメソッドの呼び出し
		CommonController.formRedirect(form, formRedirect);
		//Modelに登録
		model.addAttribute("workEditForm", form);
		//時分フォーム入力用メソッド
		CommonController.formNumbers(model);
		//admin/user_work_edit.htmlを呼び出す
		return "admin/user_work_edit";
	}
	
	//★出退勤修正画面に遷移するためのメソッド(勤怠情報未登録時)
	@GetMapping("/{userId}/{year}/{month}/{date}/edit")
	public String getAdminUserWorkEdit(@PathVariable("userId") String userId, @PathVariable("year") Integer year, @PathVariable("month") Integer month, @PathVariable("date") Integer date, Model model, @ModelAttribute("model") ModelMap modelMap) {
		//Workをformに変換
		WorkEditForm form = new WorkEditForm();
		//エラー時にリダイレクトされてきた値を受け取る
		WorkEditForm formRedirect = (WorkEditForm) modelMap.get("form");
		//formRedirectメソッドの呼び出し
		CommonController.formRedirect(form, formRedirect);
		//ユーザーを1件取得
		MUser userDetail = userService.selectUserDetail(userId);
		//Modelに登録
		model.addAttribute("userDetail", userDetail);
		//formにユーザーIDと年月日をセット
		form.setUserId(userDetail.getId());
		form.setYear(year);
		form.setMonth(month);
		form.setDate(date);
		//Modelに登録
		model.addAttribute("workEditForm", form);
		//出勤ステータスのMap
		Map<String, Integer> workStatusMap = workStatusService.getWorkStatusMap();
		//Modelに登録
		model.addAttribute("workStatusMap", workStatusMap);
		//時分フォーム入力用メソッド
		CommonController.formNumbers(model);
		//admin/user_work_edit.htmlを呼び出す
		return "admin/user_work_edit";
	}
	
	//★登録,修正ボタン押下時のメソッド
	@PostMapping("/edit")
	public String postAdminUserWorkEdit(@ModelAttribute @Validated(GroupOrder.class) WorkEditForm form, BindingResult bindingResult, Integer id, Model model, RedirectAttributes redirectAttributes, Integer year, Integer month, Integer date, Integer userId, @RequestParam(name = "workStatus", required = false) String workStatus, @RequestParam("attendanceHour") String attendanceHour, @RequestParam("attendanceMinute") String attendanceMinute, @RequestParam("leavingHour") String leavingHour, @RequestParam("leavingMinute") String leavingMinute, @RequestParam("restHour") String restHour, @RequestParam("restMinute") String restMinute) {
		//ModelMapインスタンスを生成
		ModelMap modelMap = new ModelMap();
		//エラー時にリダイレクトされてきた値をModelMapに格納する
		modelMap.addAttribute("form", form);
		//エラー時にリダイレクト先に値を渡すためのModelをセット
		redirectAttributes.addFlashAttribute("model", modelMap);
		//入力チェック結果
		if(bindingResult.hasErrors()) {
			//出勤ステータスのMap
			Map<String, Integer> workStatusMap = workStatusService.getWorkStatusMap();
			//Modelに登録
			model.addAttribute("workStatusMap", workStatusMap);
			//時分フォーム入力用メソッド
			CommonController.formNumbers(model);
			//NGがあれば出退勤修正画面に戻る
			return "admin/user_work_edit";
		}
		//勤怠情報取得
		Work workDetail = workService.selectWork(id);
		MUser userIdStr = userService.selectUserIdStr(form.getUserId());
		//errorNumber(switch分を使用するための変数)を宣言
		Integer errorNumber;
		//CommonController.confirmWorkFormメソッドで使用する変数の宣言
		Integer targetYear;
		Integer targetMonth;
		Integer targetDate;
		//入力された内容があっているかどうかを判断するメソッド(勤怠情報が登録されているか否かで引数が変わる)
		if(workDetail != null) {
		    targetYear = workDetail.getYear();
		    targetMonth = workDetail.getMonth();
		    targetDate = workDetail.getDate();
		} else {
		    targetYear = year;
		    targetMonth = month;
		    targetDate = date;
		}
		//入力された内容があっているかどうかを判断するメソッド
		errorNumber = CommonController.confirmWorkForm(targetYear, targetMonth, targetDate, form.getWorkStatus(), form.getAttendanceHour(), form.getAttendanceMinute(), form.getLeavingHour(), form.getLeavingMinute(), form.getRestHour(), form.getRestMinute());
		//入力された年月日、出勤時間、退勤時間、休憩時間が時間軸として正しいかどうかを判断するswitch文
		switch (errorNumber) {
		case 1:
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "出勤の場合、未来日付の指定はできません");
			//時分フォーム入力用メソッド
			CommonController.formNumbers(model);
			//出勤情報があるかどうかでリダイレクト先URLが異なる
			if(workDetail != null) {
				return "redirect:/admin/" + id + "/edit";
			}
			return "redirect:/admin/" + userIdStr.getUserId() + "/" + year + "/" + month + "/" + date + "/edit";
		case 2:	
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "出勤の場合はフォームを全て入力してください。");
			//時分フォーム入力用メソッド
			CommonController.formNumbers(model);
			//出勤情報があるかどうかでリダイレクト先URLが異なる
			if(workDetail != null) {
				return "redirect:/admin/" + id + "/edit";
			}
			return "redirect:/admin/" + userIdStr.getUserId() + "/" + year + "/" + month + "/" + date + "/edit";
		case 3:
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "出勤以外の場合はフォームを全て入力しないでください。");
			//時分フォーム入力用メソッド
			CommonController.formNumbers(model);
			//出勤情報があるかどうかでリダイレクト先URLが異なる
			if(workDetail != null) {
				return "redirect:/admin/" + id + "/edit";
			}
			return "redirect:/admin/" + userIdStr.getUserId() + "/" + year + "/" + month + "/" + date + "/edit";
		case 4:
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "出勤時間が退勤時間よりも大きい値になっています。");
			//時分フォーム入力用メソッド
			CommonController.formNumbers(model);
			//出勤情報があるかどうかでリダイレクト先URLが異なる
			if(workDetail != null) {
				return "redirect:/admin/" + id + "/edit";
			}
			return "redirect:/admin/" + userIdStr.getUserId() + "/" + year + "/" + month + "/" + date + "/edit";
		case 5:
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "休憩時間の値を修正してください。");
			//時分フォーム入力用メソッド
			CommonController.formNumbers(model);
			//出勤情報があるかどうかでリダイレクト先URLが異なる
			if(workDetail != null) {
				return "redirect:/admin/" + id + "/edit";
			}
			return "redirect:/admin/" + userIdStr.getUserId() + "/" + year + "/" + month + "/" + date + "/edit";
		}
		//formをWorkクラスに変換
		Work work = new Work();
		work.setId(form.getId());
		work.setAttendanceHour(form.getAttendanceHour());
		work.setAttendanceMinute(form.getAttendanceMinute());
		work.setLeavingHour(form.getLeavingHour());
		work.setLeavingMinute(form.getLeavingMinute());
		work.setRestHour(form.getRestHour());
		work.setRestMinute(form.getRestMinute());
		//RequestFormに出勤時間(時)がnullかどうかで分岐処理を行う(有休申請の場合はnullになる)
		if(form.getAttendanceHour() != null) {
			//出勤時間、退勤時間、休憩時間から就業時間と残業時間を計算するメソッド
			Integer[] calcWorkingOver = CommonController.calcWorkingOver(form.getAttendanceHour(), form.getAttendanceMinute(), form.getLeavingHour(), form.getLeavingMinute(), form.getRestHour(), form.getRestMinute());
			//上記の結果から就業時間、残業時間をセット
			work.setWorkingTimeHour(calcWorkingOver[0]);
			work.setWorkingTimeMinute(calcWorkingOver[1]);
			work.setOverTimeHour(calcWorkingOver[2]);
			work.setOverTimeMinute(calcWorkingOver[3]);
		}
		//出勤ステータスをセット
		work.setWorkStatus(form.getWorkStatus());
		//該当日の勤怠情報を取得できたかどうかで処理を分岐(勤怠情報がすでに存在していれば更新処理)
		if(workDetail != null) {
			//勤怠情報更新
			workService.updateWork(work);
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("complete", "勤怠情報を修正しました。");
		} 
		//該当日の勤怠情報を取得できたかどうかで処理を分岐(勤怠情報が存在しなければ登録処理)
		if(workDetail == null) {
			//ユーザーIDと年月日をセット
			work.setUserId(userId);
			work.setYear(year);
			work.setMonth(month);
			work.setDate(date);
			//勤怠情報登録
			workService.insertWork(work);
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("complete", "勤怠情報を登録しました。");
		}
		//ログの表示
		log.info(form.toString());
		//ユーザー一覧画面にリダイレクト
		return "redirect:/admin/" + userIdStr.getUserId() + "/" + year + "/" + month;
	}
	
	/*----------------------------*/
	
}
