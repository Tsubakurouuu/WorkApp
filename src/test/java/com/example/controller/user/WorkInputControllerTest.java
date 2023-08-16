package com.example.controller.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Calendar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.config.AuthenticationSuccessHandler;
import com.example.model.MUser;
import com.example.model.Work;
import com.example.service.WorkService;
import com.example.service.impl.UserDetailsServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(WorkInputController.class)
@AutoConfigureMockMvc(addFilters = false)
class WorkInputControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@MockBean
	private WorkService workService;
	
	@MockBean
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Test
	@DisplayName("出退勤時間入力画面に遷移するためのメソッドのテスト")
	void testGetUserWorkInput() throws Exception {
		//カレンダークラスのオブジェクトを生成
		Calendar mockCalendar = Calendar.getInstance();
		//現在の年月日曜日を取得
        Integer testYear = mockCalendar.get(Calendar.YEAR);
        Integer testMonth = mockCalendar.get(Calendar.MONTH) + 1;
        Integer testDate = mockCalendar.get(Calendar.DATE);
        Integer testWeekInt = mockCalendar.get(Calendar.DAY_OF_WEEK);
        String testWeek;
        //曜日をModelに登録
        switch (testWeekInt) {
        case Calendar.SUNDAY:
        	testWeek = "日";
            break;
        case Calendar.MONDAY:
        	testWeek = "月";
            break;
        case Calendar.TUESDAY:
        	testWeek = "火";
            break;
        case Calendar.WEDNESDAY:
        	testWeek = "水";
            break;
        case Calendar.THURSDAY:
        	testWeek = "木";
            break;
        case Calendar.FRIDAY:
        	testWeek = "金";
            break;
        case Calendar.SATURDAY:
        	testWeek = "土";
            break;
        default:
            testWeek = "";
        }
		//mockMvcを使って/work/inputにGETリクエストを送る
	    mockMvc.perform(get("/work/input"))
	        //HTTPステータスが200（OK）であることを確認
	        .andExpect(status().isOk())
	        //返されたビューの名前が"user/work_input"であることを確認
	        .andExpect(view().name("user/work_input"))
	    	//Modelが"year"を持っているかの確認
        	.andExpect(model().attributeExists("year"))
        	//"year"と期待値が同じであるかどうかを確認
	        .andExpect(model().attribute("year", testYear))
	        //Modelが"month"を持っているかの確認
        	.andExpect(model().attributeExists("month"))
        	//"month"と期待値が同じであるかどうかを確認
	        .andExpect(model().attribute("month", testMonth))
	        //Modelが"date"を持っているかの確認
        	.andExpect(model().attributeExists("date"))
        	//"date"と期待値が同じであるかどうかを確認
	        .andExpect(model().attribute("date", testDate))
	        //Modelが"week"を持っているかの確認
        	.andExpect(model().attributeExists("week"))
	        //"week"と期待値が同じであるかどうかを確認
	        .andExpect(model().attribute("week", testWeek));
	}

	@Test
	@DisplayName("出勤ボタン押下時のメソッド1(出勤打刻成功時)")
	void testPostUserWorkAttendance1() throws Exception {
		//モックのMUserを生成
		MUser mockLoginUser = new MUser();
		//モックのCalendarを生成
		Calendar mockCalendar = Calendar.getInstance();
		//ダミーデータを宣言(insertAttendanceメソッドの引数用)
		Integer testUserId = 1;
		Integer testYear = 2023;
		Integer testMonth = 8;
		Integer testDate = 15;
		Integer testAttendanceHour = 10;
		Integer testAttendanceMinute = 40;
		//モックのMUser(mockLoginUser)にダミーデータのセット
		mockLoginUser.setId(testUserId);
		//userDetailsServiceImplのselectLoginUserメソッドが呼び出されたときにモックのMUserを返すように設定
	    when(userDetailsServiceImpl.selectLoginUser()).thenReturn(mockLoginUser);
		//mockMvcを使って"/work/attendance"にPOSTリクエストを送る
	    mockMvc.perform(post("/work/attendance")
	    		//HTTPリクエストのデータを設定
	            .param("userId", testUserId.toString())
	            .param("year", testYear.toString())
	            .param("month", testMonth.toString())
	            .param("date", testDate.toString())
	            .param("attendanceHour", testAttendanceHour.toString())
	            .param("attendanceMinute", testAttendanceMinute.toString()))
	        //HTTPステータスが302（Found）であるとをを確認
	        .andExpect(status().isFound())
	        //"/work/input"へリダイレクトされることを確認
	        .andExpect(redirectedUrl("/work/input"));
	    //ArgumentCaptorの生成(Workの引数をキャッチするため)
	    ArgumentCaptor<Work> argument = ArgumentCaptor.forClass(Work.class);
	    //workServiceのinsertAttendanceが1度だけ呼び出されたことを確認
	    verify(workService, times(1)).insertAttendance(argument.capture());
	    //ArgumentCaptorでキャッチした引数の取得
	    Work workArgument = argument.getValue();
	    //insertAttendnceメソッドの実行結果と期待値が同値であることを確認
	    assertEquals(testUserId, workArgument.getUserId());
	    assertEquals(testYear, workArgument.getYear());
	    assertEquals(testMonth, workArgument.getMonth());
	    assertEquals(testDate, workArgument.getDate());
	    assertEquals(testAttendanceHour, workArgument.getAttendanceHour());
	    assertEquals(testAttendanceMinute, workArgument.getAttendanceMinute());
	}

	@Test
	void testPostUserWorkLeaving() {
		fail("まだ実装されていません");
	}

}
