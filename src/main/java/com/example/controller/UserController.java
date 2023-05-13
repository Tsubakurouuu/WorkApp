package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.form.FormForm;
import com.example.service.FormService;
import com.example.service.impl.UserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {
	@Autowired
	private FormService service;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	//出退勤申請画面に遷移するための処理
	@GetMapping("/form")
	public String getUserForm(@ModelAttribute FormForm form) {
		//user/form.htmlを呼び出す
		return "user/form";
	}
	
	//申請フォーム登録処理
	@PostMapping("/form/confirm")
	public String postUserFormConfirm() {
		return "";
	}
}
