package com.example.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.application.service.WorkStatusService;
import com.example.controller.common.CommonController;
import com.example.form.GroupOrder;
import com.example.form.RequestFormForm;
import com.example.model.Work;
import com.example.service.WorkService;

@Controller
public class FormController {
	
	@Autowired
	private WorkService workService;
	
	@Autowired
	private WorkStatusService workStatusService;
	
	/*--出退勤申請画面のメソッド一覧--*/	
	
	//★出退勤申請画面に遷移するためのメソッド(勤怠情報登録時)
	@GetMapping("/form/{id}")
	public String getUserForm(@ModelAttribute RequestFormForm form, @PathVariable("id") Integer id, Model model, @ModelAttribute("model") ModelMap modelMap) {
		//エラー時にリダイレクトされてきた値を受け取る
		RequestFormForm formRedirect = (RequestFormForm) modelMap.get("form");
		//formRedirectメソッドの呼び出し
		CommonController.formRedirect(form, formRedirect);
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
	
	//★出退勤申請画面に遷移するためのメソッド(勤怠情報未登録時)
	@GetMapping("/form/{year}/{month}/{date}")
	public String getUserForm(@ModelAttribute RequestFormForm form, @PathVariable("year") Integer year, @PathVariable("month") Integer month, @PathVariable("date") Integer date, Model model, @ModelAttribute("model") ModelMap modelMap) {
		//エラー時にリダイレクトされてきた値を受け取る
		RequestFormForm formRedirect = (RequestFormForm) modelMap.get("form");
		//formRedirectメソッドの呼び出し
		CommonController.formRedirect(form, formRedirect);
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
	
	//★確認画面へボタン押下時のメソッド
	@PostMapping("/form/confirm")
	public String postUserFormConfirm(@ModelAttribute @Validated(GroupOrder.class) RequestFormForm form, BindingResult bindingResult, Integer id, Model model, Integer year, Integer month, Integer date, RedirectAttributes redirectAttributes, @RequestParam(name = "workStatus", required = false) String workStatus, @RequestParam("attendanceHour") String attendanceHour, @RequestParam("attendanceMinute") String attendanceMinute, @RequestParam("leavingHour") String leavingHour, @RequestParam("leavingMinute") String leavingMinute, @RequestParam("restHour") String restHour, @RequestParam("restMinute") String restMinute) {
		//ModelMapインスタンスを生成
		ModelMap modelMap = new ModelMap();
		//エラー時にリダイレクトされてきた値をModelMapに格納する
		modelMap.addAttribute("form", form);
		//エラー時にリダイレクト先に値を渡すためのModelをセット
		redirectAttributes.addFlashAttribute("model", modelMap);
		//勤怠情報取得
		Work workDetail = workService.selectWork(id);
		//勤怠情報登録時(not null)の時の処理
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
		//勤怠情報未登録時(null)の時の処理
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
		//入力された出勤時間、退勤時間、休憩時間が時間軸として正しいかどうかを判断するswitch文
		switch (CommonController.confirmWorkForm(year, month, date, form.getWorkStatus(), form.getAttendanceHour(), form.getAttendanceMinute(), form.getLeavingHour(), form.getLeavingMinute(), form.getRestHour(), form.getRestMinute())) {
		case 5:
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "出勤の場合、未来日付の指定はできません");
			//時分フォーム入力用メソッド
			CommonController.formNumbers(model);
			//出勤情報があるかどうかでリダイレクト先URLが異なる
			if(workDetail != null) {
				return "redirect:/form/" + id;
			}
			return "redirect:/form/" + year + "/" + month + "/" + date;
		case 1:
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "出勤の場合はフォームを全て入力してください。");
			//時分フォーム入力用メソッド
			CommonController.formNumbers(model);
			//出勤情報があるかどうかでリダイレクト先URLが異なる
			if(workDetail != null) {
				return "redirect:/form/" + id;
			}
			return "redirect:/form/" + year + "/" + month + "/" + date;
		case 2:
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "出勤以外の場合はフォームを全て入力しないでください。");
			//時分フォーム入力用メソッド
			CommonController.formNumbers(model);
			//出勤情報があるかどうかでリダイレクト先URLが異なる
			if(workDetail != null) {
				return "redirect:/form/" + id;
			}
			return "redirect:/form/" + year + "/" + month + "/" + date;
		case 3:
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "出勤時間が退勤時間よりも大きい値になっています。");
			//時分フォーム入力用メソッド
			CommonController.formNumbers(model);
			//出勤情報があるかどうかでリダイレクト先URLが異なる
			if(workDetail != null) {
				return "redirect:/form/" + id;
			}
			return "redirect:/form/" + year + "/" + month + "/" + date;
		case 4:
			//フラッシュスコープ
			redirectAttributes.addFlashAttribute("error", "休憩時間の値を修正してください。");
			//時分フォーム入力用メソッド
			CommonController.formNumbers(model);
			//出勤情報があるかどうかでリダイレクト先URLが異なる
			if(workDetail != null) {
				return "redirect:/form/" + id;
			}
			return "redirect:/form/" + year + "/" + month + "/" + date;
		}
		//問題がなければ確認画面へ
		return "user/form_confirm";
	}
	
	/*----------------------------*/
	
	
}














