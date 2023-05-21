package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	@GetMapping("/form")
	public String getUserForm(@ModelAttribute RequestFormForm form) {
		//user/form.htmlを呼び出す
		return "user/form";
	}
	
	//戻るボタン押下時の処理
	@PostMapping("/form")
	public String postUserForm(@ModelAttribute RequestFormForm form) {
		//user/form.htmlを呼び出す
		return "user/form";
	}
	
	//確認画面へボタン押下時の処理
	@PostMapping("/form/confirm")
	public String postUserFormConfirm(@ModelAttribute RequestFormForm form, Model model) {
		//画面から受け取った文字列をModelに登録
		model.addAttribute("requestFormForm", form);
		//user/form_confirm.htmlを呼び出す
		return "user/form_confirm";
	}
	
	//出退勤申請確認画面に遷移するための処理
	@GetMapping("/form/confirm")
	public String getUserFormConfirm(@ModelAttribute RequestFormForm form) {
		//user/form_confirm.htmlを呼び出す
		return "user/form_confirm";
	}
	
	//申請ボタン押下時の処理
	@PostMapping("/form/complete")
	public String postUserFormComplete(@ModelAttribute RequestFormForm form, MUser loginUser, Model model, RedirectAttributes redirectAttributes) {
		//フラッシュスコープ
		redirectAttributes.addFlashAttribute("complete", "申請が完了しました。");
		//ログインユーザー情報取得
		loginUser = userDetailsServiceImpl.getLoginUser();
		//formの内容をmodelに詰め替える
		RequestForm requestForm = new RequestForm();
		requestForm.setUserId(loginUser.getId());
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
	
	//出退勤一覧画面に遷移するための処理
	@GetMapping("/works")
	public String getUserWorkIndex(@ModelAttribute("complete") String complete, Model model, MUser loginUser) {
		//ログインユーザー情報取得
		loginUser = userDetailsServiceImpl.getLoginUser();
		//Modelに登録
		model.addAttribute("loginUser", loginUser);
		//勤怠情報一覧取得
		List<Work> workList = workService.selectWorkList();
		//Modelに登録
		model.addAttribute("workList", workList);
		//user/work_index.htmlを呼び出す
		return "user/work_index";
	}
	
	//出退勤時間入力画面に遷移するための処理
	@GetMapping("/work/input")
	public String getUserWorkInput() {
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
		//退勤時間登録（更新）
		workService.updateLeaving(work);
		//ログを表示
		log.info(work.toString());
		//出退勤時間入力画面にリダイレクト
		return "redirect:/work/input";
	}
	
	//仮
//	@GetMapping("/{id}")
//	public String getExample(@PathVariable("id") Integer id, Model model) {
//		Work work = workService.selectWork(id);
//		model.addAttribute("work", work);
//		return "example";
//	}
}
