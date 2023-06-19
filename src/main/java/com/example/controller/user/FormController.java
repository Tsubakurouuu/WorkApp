package com.example.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.application.service.WorkStatusService;
import com.example.controller.common.CommonController;
import com.example.form.GroupOrder;
import com.example.form.RequestFormForm;
import com.example.model.MUser;
import com.example.model.Work;
import com.example.service.WorkService;

@Controller
public class FormController {
	
	@Autowired
	private WorkService workService;
	
	@Autowired
	private WorkStatusService workStatusService;
	
	/*--出退勤申請画面のメソッド一覧--*/	
	
	//出退勤申請画面に遷移するための処理(勤怠情報登録時)
	@GetMapping("/form/{id}")
	public String getUserForm(@ModelAttribute RequestFormForm form, @PathVariable("id") Integer id, Model model, MUser loginUser) {
		//勤怠情報取得
		Work workDetail = workService.selectWork(id);
		//Modelに登録
		model.addAttribute("workDetail", workDetail);
		//RequestFormのworkIdカラムに値をセット
		form.setWorkId(id);
		//出勤ステータスのMap
		Map<String, Integer> workStatusMap = workStatusService.getWorkStatusMap();
		//Modelに登録
		model.addAttribute("workStatusMap", workStatusMap);
		model.addAttribute("year", workDetail.getYear());
		model.addAttribute("month", workDetail.getMonth());
		model.addAttribute("date", workDetail.getDate());
		//時分フォーム入力用メソッド
		CommonController.formNumbers(model);
		//user/form.htmlを呼び出す
		return "user/form";
	}
	
	//出退勤申請画面に遷移するための処理(勤怠情報未登録時)
	@GetMapping("/form/{year}/{month}/{date}")
	public String getUserForm(@ModelAttribute RequestFormForm form, @PathVariable("year") Integer year, @PathVariable("month") Integer month, @PathVariable("date") Integer date, Model model, MUser loginUser) {
		//出勤ステータスのMap
		Map<String, Integer> workStatusMap = workStatusService.getWorkStatusMap();
		//Modelに登録
		model.addAttribute("workStatusMap", workStatusMap);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("date", date);
		//時分フォーム入力用メソッド
		CommonController.formNumbers(model);
		//user/form.htmlを呼び出す
		return "user/form";
	}
	
	//確認画面へボタン押下時の処理
	@PostMapping("/form/confirm")
	public String postUserFormConfirm(@ModelAttribute @Validated(GroupOrder.class) RequestFormForm form, BindingResult bindingResult, Integer id, Model model, RedirectAttributes redirectAttributes, Integer year, Integer month, Integer date) {
		//勤怠情報取得
		Work workDetail = workService.selectWork(id);
		if(workDetail != null) {
			//年月日をformにセット
			form.setYear(workDetail.getYear());
			form.setMonth(workDetail.getMonth());
			form.setDate(workDetail.getDate());
			//Modelに登録
			model.addAttribute("workDetail", workDetail);
			model.addAttribute("year", workDetail.getYear());
			model.addAttribute("month", workDetail.getMonth());
			model.addAttribute("date", workDetail.getDate());
		}
		if(workDetail == null) {
			model.addAttribute("year", year);
			model.addAttribute("month", month);
			model.addAttribute("date", date);
		}
		//出勤ステータスのMap
		Map<String, Integer> workStatusMap = workStatusService.getWorkStatusMap();
		//Modelに登録
		model.addAttribute("workStatusMap", workStatusMap);
		//入力チェック結果
		if(bindingResult.hasErrors()) {
			//時分フォーム入力用メソッド
			CommonController.formNumbers(model);
			//NGがあれば出退勤申請画面に戻る
			return "user/form";
		}
		//user/form_confirm.htmlを呼び出す
		return "user/form_confirm";
	}
	
	/*----------------------------*/
	

}