package com.example.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	/*--ログイン画面のメソッド一覧--*/	
	
	//★ログイン画面に遷移するためのメソッド
	@GetMapping("/login")
	public String getLogin() {
		//login/login.htmlを呼び出す
		return "login/login";
	}
	
	/*----------------------------*/
	
}
