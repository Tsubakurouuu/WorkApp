package com.example.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.controller.common.CommonController;
import com.example.model.RequestForm;
import com.example.model.Work;
import com.example.service.RequestFormService;
import com.example.service.WorkService;

@Controller
@RequestMapping("/admin")
public class FormDetailController {
	
	@Autowired
	private WorkService workService;
	
	@Autowired
	private RequestFormService requestFormService;
	
	/*--申請、通知詳細画面のメソッド一覧--*/
	
	//申請、通知詳細画面に遷移するための処理
	@GetMapping("/form/{id}")
	public String getAdminFormDetail(Model model, @PathVariable("id") Integer id) {
		//ユーザーを1件取得
		RequestForm requestFormDetail = requestFormService.selectRequestFormDetail(id);
		//Modelに登録
		model.addAttribute("requestFormDetail", requestFormDetail);
		//admin/form_detail.htmlを呼び出す
		return "admin/form_detail";
	}
	
	//申請内容を反映するボタン押下時の処理
	@PostMapping("/form/update")
	public String postAdminFormUpdate(Integer id, RedirectAttributes redirectAttributes) {
		//ユーザーを1件取得
		RequestForm requestFormDetail = requestFormService.selectRequestFormDetail(id);
		//RequestFormの内容をWorkに反映させる処理
		Work work = new Work();
		work.setUserId(requestFormDetail.getUserId());
		work.setYear(requestFormDetail.getYear());
		work.setMonth(requestFormDetail.getMonth());
		work.setDate(requestFormDetail.getDate());
		if(requestFormDetail.getAttendanceHour() != null) {
			work.setAttendanceHour(requestFormDetail.getAttendanceHour());
			work.setAttendanceMinute(requestFormDetail.getAttendanceMinute());
			work.setLeavingHour(requestFormDetail.getLeavingHour());
			work.setLeavingMinute(requestFormDetail.getLeavingMinute());
			work.setRestHour(requestFormDetail.getRestHour());
			work.setRestMinute(requestFormDetail.getRestMinute());
			//就業時間と残業時間を計算するメソッド
			Integer[] calcWorkingOver = CommonController.calcWorkingOver(requestFormDetail.getAttendanceHour(), requestFormDetail.getAttendanceMinute(), requestFormDetail.getLeavingHour(), requestFormDetail.getLeavingMinute(), requestFormDetail.getRestHour(), requestFormDetail.getRestMinute());
			work.setWorkingTimeHour(calcWorkingOver[0]);
			work.setWorkingTimeMinute(calcWorkingOver[1]);
			work.setOverTimeHour(calcWorkingOver[2]);
			work.setOverTimeMinute(calcWorkingOver[3]);
		}
		work.setWorkStatus(requestFormDetail.getWorkStatus());
		if(requestFormDetail.getWorkId() != null) {
			work.setId(requestFormDetail.getWork().getId());
			//勤怠情報更新（申請フォーム）
			workService.updateWork(work);
		} else {
			//打刻を忘れた際の登録、有休申請登録
			workService.insertWork(work);
		}
		//登録、更新後に申請フォームを削除
		requestFormService.deleteRequestForm(id);
		//フラッシュスコープ
		redirectAttributes.addFlashAttribute("complete", "勤怠情報を修正しました。");
		//ユーザー一覧画面にリダイレクト
		return "redirect:/admin/users";
	}
	
	/*----------------------------*/
}
