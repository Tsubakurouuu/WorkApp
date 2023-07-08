package com.example.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.Notification;
import com.example.model.RequestForm;
import com.example.service.NotificationService;
import com.example.service.RequestFormService;

@Controller
@RequestMapping("/admin")
public class FormIndexController {
	
	@Autowired
	private RequestFormService requestFormService;
	
	@Autowired
	private NotificationService notificationService;
	
	/*--申請、通知一覧画面のメソッド一覧--*/
	
	//★申請、通知一覧画面に遷移するためのメソッド
	@GetMapping("/forms")
	public String getAdminFormIndex(Model model) {
		//申請フォーム一覧取得
		List<RequestForm> requestFormList = requestFormService.selectRequestFormList();
		//Modelに登録
		model.addAttribute("requestFormList", requestFormList);
		//通知一覧取得
		List<Notification> notificationList = notificationService.selectNotificationList();
		//Modelに登録
		model.addAttribute("notificationList", notificationList);
		//admin/work_index.htmlを呼び出す
		return "admin/form_index";
	}
	
	//★削除ボタン押下時のメソッド
	@PostMapping("/notification/delete")
	public String deleteNotification(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
	    notificationService.deleteNotification(id);
	    redirectAttributes.addFlashAttribute("complete", "アラートを削除しました。");
	    return "redirect:/admin/forms";
	}
	
	/*----------------------------*/
}
