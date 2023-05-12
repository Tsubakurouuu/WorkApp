package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	//ログインページに遷移するための処理
	@GetMapping("/login")
	public String getLogin() {
		//login/login.htmlを呼び出す
		return "login/login";
	}
}
