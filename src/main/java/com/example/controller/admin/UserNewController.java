package com.example.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class UserNewController {
	
	@Autowired
	private MUserService userService;
	
	/*--ユーザー登録画面のメソッド一覧--*/
	
	//ユーザー登録画面に遷移するための処理
	@GetMapping("/user/new")
	public String getAdminUserNew(@ModelAttribute UserNewForm form) {
		//admin/user_new.htmlを呼び出す
		return "admin/user_new";
	}
	
	//新規登録ボタン押下時の処理
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
		userService.insertUser(user);
		//ユーザー一覧画面にリダイレクト
		return "redirect:/admin/users";
	}
	
	/*----------------------------*/
	
}
