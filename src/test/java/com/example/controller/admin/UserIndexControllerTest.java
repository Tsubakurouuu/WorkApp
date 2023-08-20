package com.example.controller.admin;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.example.service.MUserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserIndexController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserIndexControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@MockBean
	private MUserService userService;

	@Test
    @DisplayName("ユーザー一覧画面に遷移するためのメソッドのテスト")
    void testGetAdminUserIndex() throws Exception {
        //モックのMUserリストを作成
        List<MUser> mockUserList = new ArrayList<>();
        //MUserリストが空でないことを保証
        mockUserList.add(new MUser());
        //userServiceのselectUserListメソッドが呼び出されたときにモックのMUserリストを返すように設定
        when(userService.selectUserList()).thenReturn(mockUserList);
        //カレンダークラスのオブジェクトを生成
  		Calendar mockCalendar = Calendar.getInstance();
  		//現在の年月を取得
  		int testYear = mockCalendar.get(Calendar.YEAR);
  		int testMonth = mockCalendar.get(Calendar.MONTH) + 1;
        //mockMvcを使って/admin/usersにGETリクエストを送る
        mockMvc.perform(get("/admin/users"))
            //HTTPステータスが200（OK）であることを確認
            .andExpect(status().isOk())
            //返されたビューの名前が"admin/user_index"であることを確認
            .andExpect(view().name("admin/user_index"))
            //Modelに正しく属性が設定されていることを確認
            .andExpect(model().attributeExists("userList"))
            .andExpect(model().attribute("userList", mockUserList))
            .andExpect(model().attributeExists("year"))
            .andExpect(model().attribute("year", testYear))
            .andExpect(model().attributeExists("month"))
            .andExpect(model().attribute("month", testMonth));
    }

}
