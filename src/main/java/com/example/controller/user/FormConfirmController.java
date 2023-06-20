package com.example.controller.user;

import java.util.Calendar;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.application.service.WorkStatusService;
import com.example.controller.common.CommonController;
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
public class FormConfirmController {
	
	@Autowired
	private WorkService workService;
	
	@Autowired
	private RequestFormService requestFormService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private WorkStatusService workStatusService;
	
	/*--出退勤申請確認画面のメソッド一覧--*/
	
	//★出退勤申請確認画面に遷移するためのメソッド
	@GetMapping("/form/confirm")
	public String getUserFormConfirm(@ModelAttribute RequestFormForm form, Integer id, Model model, Integer year, Integer month, Integer date, Work workDetail) {
		//勤怠情報取得
		workDetail = workService.selectWork(id);
		//該当日の勤怠情報が存在するかどうかの条件分岐
		if(workDetail != null) {
			//Modelに登録
			model.addAttribute("workDetail", workDetail);
		}
		//Modelに登録
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("date", date);
		//user/form_confirm.htmlを呼び出す
		return "user/form_confirm";
	}
	
	//★戻るボタン押下時のメソッド(勤怠情報登録時)
	@PostMapping("/form/{id}")
	public String postUserForm(@ModelAttribute RequestFormForm form, Model model, @PathVariable("id") Integer id) {
		//勤怠情報取得
		Work workDetail = workService.selectWork(id);
		//該当日の勤怠情報が存在するかどうかの条件分岐
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
	
	//★戻るボタン押下時のメソッド(勤怠情報未登録時)
	@PostMapping("/form/{year}/{month}/{date}")
	public String PostUserForm(@ModelAttribute RequestFormForm form, Integer year, Integer month, Integer date, Model model) {
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
	
	//★申請ボタン押下時のメソッド
	@PostMapping("/form/complete")
	public String postUserFormComplete(@ModelAttribute RequestFormForm form, MUser loginUser, Model model, RedirectAttributes redirectAttributes, Integer year, Integer month) {
		//ログインユーザー情報取得
		loginUser = userDetailsServiceImpl.selectLoginUser();
		//フラッシュスコープ
		redirectAttributes.addFlashAttribute("complete", "出退勤修正の申請が完了しました。");
		//formの値をRequestFormに詰め替える
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
