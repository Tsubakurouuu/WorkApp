package com.example.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.common.CommonUtils;
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
	
	//★申請、通知詳細画面に遷移するためのメソッド(申請)
	@GetMapping("/form/{id}")
	public String getAdminFormDetail(Model model, @PathVariable("id") Integer id) {
		//ユーザーを1件取得
		RequestForm requestFormDetail = requestFormService.selectRequestFormDetail(id);
		//Modelに登録
		model.addAttribute("requestFormDetail", requestFormDetail);
		//admin/form_detail.htmlを呼び出す
		return "admin/form_detail";
	}
	
	
	//★申請内容を反映するボタン押下時のメソッド
	@PostMapping("/form/update")
	public String postAdminFormUpdate(Integer id, RedirectAttributes redirectAttributes) {
		//ユーザーを1件取得
		RequestForm requestFormDetail = requestFormService.selectRequestFormDetail(id);
		//Workオブジェクトの生成
		Work work = new Work();
		//RequestFormのユーザーID,年月日をWorkに詰め替える
		work.setUserId(requestFormDetail.getUserId());
		work.setYear(requestFormDetail.getYear());
		work.setMonth(requestFormDetail.getMonth());
		work.setDate(requestFormDetail.getDate());
		//RequestFormに出勤時間(時)がnullかどうかで分岐処理を行う(有休申請の場合はnullになる)
		if(requestFormDetail.getAttendanceHour() != null) {
			//出勤時間(時)がnullでなければRequestFormの出勤時間、退勤時間、休憩時間の値ををWorkに詰め替える
			work.setAttendanceHour(requestFormDetail.getAttendanceHour());
			work.setAttendanceMinute(requestFormDetail.getAttendanceMinute());
			work.setLeavingHour(requestFormDetail.getLeavingHour());
			work.setLeavingMinute(requestFormDetail.getLeavingMinute());
			work.setRestHour(requestFormDetail.getRestHour());
			work.setRestMinute(requestFormDetail.getRestMinute());
			//出勤時間、退勤時間、休憩時間から就業時間と残業時間を計算するメソッド
			Integer[] calcWorkingOver = CommonUtils.calcWorkingOver(requestFormDetail.getAttendanceHour(), requestFormDetail.getAttendanceMinute(), requestFormDetail.getLeavingHour(), requestFormDetail.getLeavingMinute(), requestFormDetail.getRestHour(), requestFormDetail.getRestMinute());
			//上記の結果から就業時間、残業時間をセット
			work.setWorkingTimeHour(calcWorkingOver[0]);
			work.setWorkingTimeMinute(calcWorkingOver[1]);
			work.setOverTimeHour(calcWorkingOver[2]);
			work.setOverTimeMinute(calcWorkingOver[3]);
		}
		//出勤ステータスをセット
		work.setWorkStatus(requestFormDetail.getWorkStatus());
		//RequestFormの申請年月日と同日の勤怠情報が登録されているかどうかで処理を分岐
		if(requestFormDetail.getWorkId() != null) {
			//登録されていれば該当日の勤怠IDをセットして更新
			work.setId(requestFormDetail.getWork().getId());
			//勤怠情報更新（申請フォーム）
			workService.updateWork(work);
		} else {
			//登録がなければ勤怠情報を新規登録
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
