package com.example.controller.admin;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.config.AuthenticationSuccessHandler;
import com.example.model.MUser;
import com.example.model.Notification;
import com.example.model.RequestForm;
import com.example.model.Work;
import com.example.service.NotificationService;
import com.example.service.RequestFormService;

@RunWith(SpringRunner.class)
@WebMvcTest(FormIndexController.class)
@AutoConfigureMockMvc(addFilters = false)
class FormIndexControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@MockBean
	private RequestFormService requestFormService;
	
	@MockBean
	private NotificationService notificationService;
	
	@Test
	@DisplayName("申請、通知一覧画面に遷移するためのメソッドのテスト")
	void testGetAdminFormIndex() throws Exception {
	    //モックのMUserを作成(selectRequestFormListメソッド呼び出し時にテーブルを結合しているため)
	    MUser mockUser = new MUser();
	    //モックのWorkを作成(selectRequestFormListメソッド呼び出し時にテーブルを結合しているため)
	    Work mockWork = new Work();
	    //モックのRequestFormを作成
	    RequestForm mockRequestForm = new RequestForm();
	    //モックのRequestFormに結合しているテーブルをセット
	    mockRequestForm.setMUser(mockUser);
	    mockRequestForm.setWork(mockWork);
	    //モックのRequestFormリストを作成
        List<RequestForm> mockRequestFormList = new ArrayList<>();
        //RequestFormリストが空でないことを保証
        mockRequestFormList.add(mockRequestForm);
        //requestFormServiceのselectRequestFormListメソッドが呼び出されたときにモックのRequestFormListリストを返すように設定
	    when(requestFormService.selectRequestFormList()).thenReturn(mockRequestFormList);
	    //モックのNotificationを作成
	    Notification mockNotification = new Notification();
	    //モックのNotificationに結合しているテーブルをセット
	    mockNotification.setMUser(mockUser);
	    mockNotification.setWork(mockWork);
	    //モックのNotificationリストを作成
        List<Notification> mockNotificationList = new ArrayList<>();
        //Notificationリストが空でないことを保証
        mockNotificationList.add(mockNotification);
        //notificationServiceのselectNotificationListメソッドが呼び出されたときにモックのNotificationリストを返すように設定
	    when(notificationService.selectNotificationList()).thenReturn(mockNotificationList);
	    // mockMvcを使って/admin/formsにGETリクエストを送る
	    mockMvc.perform(get("/admin/forms"))
	        //HTTPステータスが200（OK）であることを確認
	        .andExpect(status().isOk())
	        //返されたビューの名前が"admin/form_index"であることを確認
	        .andExpect(view().name("admin/form_index"))
	        //Modelが"requestFormList"を持っているかの確認
	        .andExpect(model().attributeExists("requestFormList"))
	        //"requestFormList"と期待値が同じであるかどうかを確認
	        .andExpect(model().attribute("requestFormList", mockRequestFormList))
	        //Modelが"notificationList"を持っているかの確認
	        .andExpect(model().attributeExists("notificationList"))
	        //"notificationList"と期待値が同じであるかどうかを確認
	        .andExpect(model().attribute("notificationList", mockNotificationList));
	}


	@Test
	void testDeleteNotification() {
		fail("まだ実装されていません");
	}

}
