package com.example.controller.admin;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.model.MUser;
import com.example.service.MUserService;

@Controller
@RequestMapping("/admin")
public class UserIndexController {
	
	@Autowired
	private MUserService userService;
	
	/*--ユーザー一覧画面のメソッド一覧--*/
	
	//★ユーザー一覧画面に遷移するためのメソッド
	@GetMapping("/users")
	public String getAdminUserIndex(Model model, Integer year, Integer month) {
		//カレンダークラスのオブジェクトを生成
		Calendar calendar = Calendar.getInstance();
		//現在の年を取得
        year = calendar.get(Calendar.YEAR);
        //現在の月を取得
        month = calendar.get(Calendar.MONTH) + 1;
        //Modelに登録
        model.addAttribute("year", year);
		model.addAttribute("month", month);	
		//ユーザー一覧取得
		List<MUser> userList = userService.selectUserList();
		//Modelに登録
		model.addAttribute("userList", userList);
		//admin/user_index.htmlを呼び出す
		return "admin/user_index";
	}
	
	/*----------------------------*/

}
