package com.example.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.model.RequestForm;
import com.example.service.RequestFormService;

@Controller
@RequestMapping("/admin")
public class FormIndexController {
	
	@Autowired
	private RequestFormService requestFormService;
	
	/*--申請、通知一覧画面のメソッド一覧--*/
	
	//申請、通知一覧画面に遷移するための処理
	@GetMapping("/forms")
	public String getAdminFormIndex(Model model) {
		//申請フォーム一覧取得
		List<RequestForm> requestFormList = requestFormService.selectRequestFormList();
		//Modelに登録
		model.addAttribute("requestFormList", requestFormList);
		//admin/work_index.htmlを呼び出す
		return "admin/form_index";
	}
	
	/*----------------------------*/
}
