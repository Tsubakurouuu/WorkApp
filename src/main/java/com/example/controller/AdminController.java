package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.model.MUser;
import com.example.service.MUserService;

@Controller
public class AdminController {
	@Autowired
	private MUserService service;
	
	//ユーザー一覧画面に遷移するための処理
	@GetMapping("/admin/users")
	public String getAdminUserIndex(Model model) {
		//ユーザー一覧取得
		List<MUser> userList = service.getUserList();
		//Modelに登録
		model.addAttribute("userList", userList);
		//admin/user_index.htmlを呼び出す
		return "admin/user_index";
	}
	
	//ユーザー詳細画面に遷移するための処理
	@GetMapping("/admin/{userId:.+}")
	public String getAdminUserDetail(Model model, @PathVariable("userId") String userId) {
		//ユーザーを1件取得
		MUser userDetail = service.getUserDetail(userId);
		//Modelに登録
		model.addAttribute("userDetail", userDetail);
		//admin/user_detail.htmlを呼び出す
		return "admin/user_detail";
	}
}
