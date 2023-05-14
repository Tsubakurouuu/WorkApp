package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.form.RequestFormForm;
import com.example.model.MUser;
import com.example.model.RequestForm;
import com.example.service.RequestFormService;
import com.example.service.impl.UserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {
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
	
	//申請フォーム登録処理
	@PostMapping("/form/confirm")
	public String postUserFormConfirm(@ModelAttribute RequestFormForm form, MUser loginUser) {
		//RequestFormFormをRequestFormに変換
//		RequestForm requestForm = modelMapper.map(form, RequestForm.class);
		//ログインユーザー情報取得
		loginUser = userDetailsServiceImpl.getLoginUser();
		//formの内容をmodelに詰め替える
		RequestForm requestForm = new RequestForm();
		requestForm.setUserId(loginUser.getUserId());
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
		return "redirect:/form/confirm";
	}
}
