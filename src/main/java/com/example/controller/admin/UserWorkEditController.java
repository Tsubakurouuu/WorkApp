package com.example.controller.admin;

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
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	//出退勤修正画面に遷移するための処理(勤怠情報登録時)
	@GetMapping("/{id}/edit")
	public String getAdminUserWorkEdit(@PathVariable("id") Integer id, Model model) {
		//勤怠情報取得
		Work workDetail = workService.selectWork(id);
		//Workをformに変換
		WorkEditForm form = new WorkEditForm();
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
	
	//出退勤修正画面に遷移するための処理(勤怠情報未登録時)
	@GetMapping("/{userId}/{year}/{month}/{date}/edit")
	public String getAdminUserWorkEdit(@PathVariable("userId") String userId, @PathVariable("year") Integer year, @PathVariable("month") Integer month, @PathVariable("date") Integer date, Model model) {
		//ユーザーを1件取得
		MUser userDetail = userService.selectUserDetail(userId);
		//Modelに登録
		model.addAttribute("userDetail", userDetail);
		//Workをformに変換
		WorkEditForm form = new WorkEditForm();
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
	
	//登録/修正ボタン押下時の処理
	@PostMapping("/edit")
	public String postAdminUserWorkEdit(@ModelAttribute @Validated(GroupOrder.class) WorkEditForm form, BindingResult bindingResult, Integer id, Model model, RedirectAttributes redirectAttributes, Integer year, Integer month, Integer date, Integer userId) {
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
		//formをWorkクラスに変換
		Work work = new Work();
		work.setId(form.getId());
		work.setAttendanceHour(form.getAttendanceHour());
		work.setAttendanceMinute(form.getAttendanceMinute());
		work.setLeavingHour(form.getLeavingHour());
		work.setLeavingMinute(form.getLeavingMinute());
		work.setRestHour(form.getRestHour());
		work.setRestMinute(form.getRestMinute());
		if(form.getAttendanceHour() != null) {
			//就業時間と残業時間を計算するメソッド
			Integer[] calcWorkingOver = CommonController.calcWorkingOver(form.getAttendanceHour(), form.getAttendanceMinute(), form.getLeavingHour(), form.getLeavingMinute(), form.getRestHour(), form.getRestMinute());
			work.setWorkingTimeHour(calcWorkingOver[0]);
			work.setWorkingTimeMinute(calcWorkingOver[1]);
			work.setOverTimeHour(calcWorkingOver[2]);
			work.setOverTimeMinute(calcWorkingOver[3]);
		}
		work.setWorkStatus(form.getWorkStatus());
		//勤怠情報取得
		Work workDetail = workService.selectWork(id);
		if(workDetail != null) {
			//勤怠情報更新
			workService.updateWork(work);
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("complete", "勤怠情報を修正しました。");
		} 
		if(workDetail == null) {
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
		return "redirect:/admin/users";
	}
	
	/*----------------------------*/
}