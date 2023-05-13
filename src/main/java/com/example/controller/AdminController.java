package com.example.controller;

import java.util.List;

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

import com.example.form.GroupOrder;
import com.example.form.UserNewForm;
import com.example.model.MUser;
import com.example.service.MUserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
	@Autowired
	private MUserService service;
	
	//ユーザー一覧画面に遷移するための処理
	@GetMapping("/users")
	public String getAdminUserIndex(Model model) {
		//ユーザー一覧取得
		List<MUser> userList = service.getUserList();
		//Modelに登録
		model.addAttribute("userList", userList);
		//admin/user_index.htmlを呼び出す
		return "admin/user_index";
	}
	
	//ユーザー詳細画面に遷移するための処理
	@GetMapping("/{userId:.+}")
	public String getAdminUserDetail(Model model, @PathVariable("userId") String userId) {
		//ユーザーを1件取得
		MUser userDetail = service.getUserDetail(userId);
		//Modelに登録
		model.addAttribute("userDetail", userDetail);
		//admin/user_detail.htmlを呼び出す
		return "admin/user_detail";
	}
	
	//ユーザー新規登録画面に遷移するための処理
	@GetMapping("/user/new")
	public String getAdminUserNew(@ModelAttribute UserNewForm form) {
		//admin/user_new.htmlを呼び出す
		return "admin/user_new";
	}
	
	//ユーザー新規登録処理
	@PostMapping("/user/new")
	public String postAdminUserNew(@ModelAttribute @Validated(GroupOrder.class) UserNewForm form, BindingResult bindingResult) {
		//入力チェック結果
		if(bindingResult.hasErrors()) {
			//NGがあれば新規登録画面に戻る
			return "admin/user_new";
		}
		//ログを表示
		log.info(form.toString());
		//formの内容をmodelに詰め替える
		MUser user = new MUser();
		user.setUserId(form.getUserId());
		user.setPassword(form.getPassword());
		user.setLastName(form.getLastName());
		user.setFirstName(form.getFirstName());
		user.setBirthday(form.getBirthday());
		//ユーザー新規登録
		service.insertUser(user);
		//ユーザー一覧画面にリダイレクト
		return "redirect:/admin/users";
	}
	
}
