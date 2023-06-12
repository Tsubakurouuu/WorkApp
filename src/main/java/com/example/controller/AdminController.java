package com.example.controller;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.application.service.WorkStatusService;
import com.example.form.GroupOrder;
import com.example.form.UserNewForm;
import com.example.form.WorkEditForm;
import com.example.model.MUser;
import com.example.model.RequestForm;
import com.example.model.Work;
import com.example.service.MUserService;
import com.example.service.RequestFormService;
import com.example.service.WorkService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
	@Autowired
	private MUserService userService;
	
	@Autowired
	private WorkService workService;
	
	@Autowired
	private RequestFormService requestFormService;
	
	@Autowired
	private WorkStatusService workStatusService;
	
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
	
	
	
	/*--ユーザー一覧画面のメソッド一覧--*/
	
	//ユーザー一覧画面に遷移するための処理
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
		List<MUser> userList = userService.getUserList();
		//Modelに登録
		model.addAttribute("userList", userList);
		//admin/user_index.htmlを呼び出す
		return "admin/user_index";
	}
	
	/*----------------------------*/
	
	
	
	/*--ユーザー詳細画面のメソッド一覧--*/
	
	//ユーザー詳細画面に遷移するための処理
	@GetMapping("/{userId:.+}/{year}/{month}")
	public String getAdminUserDetail(@PathVariable("userId") String userId, Integer id, @PathVariable("year") Integer year, @PathVariable("month") Integer month, Model model) {
		//年と月が指定されていない場合、現在の年と月を取得
		if (year == null || month == null) {
			Calendar calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH) + 1;
	    }
		//ユーザーを1件取得
		MUser userDetail = userService.getUserDetail(userId);
		//Modelに登録
		model.addAttribute("userDetail", userDetail);
		//勤怠情報月毎取得
		List<Work> workList = workService.selectWorkListMonth(userDetail.getId(), year, month);
		//各月の最終日にちを取得
		Integer lastDateOfMonth = YearMonth.of(year, month).lengthOfMonth();
		//日付ごとの勤怠情報をMapに変換する
	    Map<Integer, Work> workMap = new HashMap<>();
	    for (Work work : workList) {
	    	workMap.put(work.getDate(), work);
	    }
	    //Modelに登録
		model.addAttribute("workMap", workMap);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("lastDateOfMonth", lastDateOfMonth);
		//admin/user_detail.htmlを呼び出す
		return "admin/user_detail";
	}
	
	//先月ボタン(◀︎)押下時の処理
	@GetMapping("/{userId}/{year}/{month}/previous")
	public String showPreviousMonthAttendance(@PathVariable("userId") String userId, @PathVariable("year") Integer year, @PathVariable("month") Integer month) {
		//1ヶ月前の年と月を計算
	    if (month == 1) {
	    	year--;
	    	month = 12;
	    } else {
	    	month--;
	    }
	    //指定した年月日画面へリダイレクト
	    return "redirect:/admin/" + userId + "/" + year + "/" + month;
	}
	
	//翌月ボタン(▶︎)押下時の処理
	@GetMapping("/{userId}/{year}/{month}/next")
	public String showNextMonthAttendance(@PathVariable("userId") String userId, @PathVariable("year") Integer year, @PathVariable("month") Integer month) {
		//1ヶ月後の年と月を計算
	    if (month == 12) {
	    	year++;
	    	month = 1;
	    } else {
	    	month++;
	    }
	    //指定した年月日画面へリダイレクト
	    return "redirect:/admin/" + userId + "/" + year + "/" + month;
	}
	
	/*----------------------------*/
	
	
	
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
		work.setId(requestFormDetail.getWork().getId());
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
		work.setWorkStatus(requestFormDetail.getWorkStatus());
		//勤怠情報更新（申請フォーム）
		workService.updateWork(work);
		//フラッシュスコープ
		redirectAttributes.addFlashAttribute("complete", "勤怠情報を修正しました。");
		//ユーザー一覧画面にリダイレクト
		return "redirect:/admin/users";
	}
	
	/*----------------------------*/
	
	
	
	/*--出退勤修正画面のメソッド一覧--*/
	
	//出退勤修正画面に遷移するための処理
	@GetMapping("/{id}/edit")
	public String getAdminUserWorkEdit(@PathVariable("id") Integer id, Model model) {
		//勤怠情報取得
		Work workDetail = workService.selectWork(id);
		//Workをformに変換
		WorkEditForm form = new WorkEditForm();
		form.setId(workDetail.getId());
		form.setUserId(workDetail.getUserId());
		form.setYear(workDetail.getYear());
		form.setMonth(workDetail.getMonth());
		form.setDate(workDetail.getDate());
		form.setAttendanceHour(workDetail.getAttendanceHour());
		form.setAttendanceMinute(workDetail.getAttendanceMinute());
		form.setLeavingHour(workDetail.getLeavingHour());
		form.setLeavingMinute(workDetail.getLeavingMinute());
		form.setRestHour(workDetail.getRestHour());
		form.setRestMinute(workDetail.getRestMinute());
		form.setWorkingTimeHour(workDetail.getWorkingTimeHour());
		form.setWorkingTimeMinute(workDetail.getWorkingTimeMinute());
		form.setOverTimeHour(workDetail.getOverTimeHour());
		form.setOverTimeMinute(workDetail.getOverTimeMinute());
		form.setWorkStatus(workDetail.getWorkStatus());
		//Modelに登録
		model.addAttribute("workEditForm", form);
		//出勤ステータスのMap
		Map<String, Integer> workStatusMap = workStatusService.getWorkStatusMap();
		//Modelに登録
		model.addAttribute("workStatusMap", workStatusMap);
		//時分フォーム入力用メソッド
		CommonController.formNumbers(model);
		//admin/user_work_edit.htmlを呼び出す
		return "admin/user_work_edit";
	}
	
	//修正ボタン押下時の処理
	@PostMapping("/edit")
	public String postAdminUserWorkEdit(@ModelAttribute @Validated(GroupOrder.class) WorkEditForm form, BindingResult bindingResult, Integer id, Model model, RedirectAttributes redirectAttributes) {
		//入力チェック結果
		if(bindingResult.hasErrors()) {
			//NGがあれば出退勤修正画面に戻る
			return "admin/user_work_edit";
		}
		//formをWorkクラスに変換
		Work work = new Work();
		work.setId(form.getId());
		work.setAttendanceHour(form.getAttendanceHour());
		work.setAttendanceMinute(form.getAttendanceMinute());
		work.setLeavingHour(form.getLeavingHour());
		work.setLeavingMinute(form.getLeavingMinute());
		work.setRestHour(form.getRestHour());
		work.setRestMinute(form.getRestMinute());
		//就業時間と残業時間を計算するメソッド
		Integer[] calcWorkingOver = CommonController.calcWorkingOver(form.getAttendanceHour(), form.getAttendanceMinute(), form.getLeavingHour(), form.getLeavingMinute(), form.getRestHour(), form.getRestMinute());
		work.setWorkingTimeHour(calcWorkingOver[0]);
		work.setWorkingTimeMinute(calcWorkingOver[1]);
		work.setOverTimeHour(calcWorkingOver[2]);
		work.setOverTimeMinute(calcWorkingOver[3]);
		work.setWorkStatus(form.getWorkStatus());
		//ログの表示
		log.info(form.toString());
		//勤怠情報更新
		workService.updateWork(work);
		//フラッシュスコープ
		redirectAttributes.addFlashAttribute("complete", "勤怠情報を修正しました。");
		//ユーザー一覧画面にリダイレクト
		return "redirect:/admin/users";
	}
	
	/*----------------------------*/
}
