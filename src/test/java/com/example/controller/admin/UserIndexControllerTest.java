package com.example.controller.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

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
		//モックのModelを生成
		Model mockModel = new ExtendedModelMap();
		//カレンダークラスのオブジェクトを生成
		Calendar calendar = Calendar.getInstance();
		//現在の年月を取得
        Integer testYear = calendar.get(Calendar.YEAR);
        Integer testMonth = calendar.get(Calendar.MONTH) + 1;
        //モックのModelが"hourNumbers"を持っているかの確認
  		assertTrue(mockModel.containsAttribute("testYear"));
  		//モックのModelが"minuteNumbers"を持っているかの確認
  		assertTrue(mockModel.containsAttribute("testMonth"));
  		//"testYear"と期待値が同じであるかどうかを確認
		assertEquals(testYear, mockModel.getAttribute("testYear"));
		//"testMonth"と期待値が同じであるかどうかを確認
		assertEquals(testMonth, mockModel.getAttribute("testMonth"));
		//モックのMUserリストを作成
		List<MUser> mockUserList = new ArrayList<>();
		//MUserリストが空でないことを保証
		mockUserList.add(new MUser());
		//userServiceのselectUserListメソッドが呼び出されたときにモックのMUserリストを返すように設定
		when(userService.selectUserList()).thenReturn(mockUserList);
		//テスト対象のメソッドを実行してMUserのListを受け取る
		List<MUser> resultUserList = userService.selectUserList();
		//モックのModelが"resultUserList"を持っているかの確認
  		assertTrue(mockModel.containsAttribute("resultUserList"));
  		//"resultUserList"と期待値が同じであるかどうかを確認
		assertEquals(resultUserList, mockModel.getAttribute("resultUserList"));
		//mockMvcを使って/loginにGETリクエストを送る
		mockMvc.perform(get("/admin/users"))
			//HTTPステータスが200（OK）であることを確認
			.andExpect(status().isOk())
			//返されたビューの名前が"admin/user_index"であることを確認
			.andExpect(view().name("admin/user_index"));
	}

	@Test
    @DisplayName("ユーザー一覧画面に遷移するためのメソッドのテスト1")
    void testGetAdminUserIndex1() throws Exception {
        // モックのMUserリストを作成
        List<MUser> mockUserList = new ArrayList<>();
        // MUserリストが空でないことを保証
        mockUserList.add(new MUser());
        // userServiceのselectUserListメソッドが呼び出されたときにモックのMUserリストを返すように設定
        when(userService.selectUserList()).thenReturn(mockUserList);

        // mockMvcを使って/admin/usersにGETリクエストを送る
        mockMvc.perform(get("/admin/users"))
            // HTTPステータスが200（OK）であることを確認
            .andExpect(status().isOk())
            // 返されたビューの名前が"admin/user_index"であることを確認
            .andExpect(view().name("admin/user_index"))
            // Modelに正しく属性が設定されていることを確認
            .andExpect(model().attributeExists("userList"))
            .andExpect(model().attribute("userList", mockUserList))
            .andExpect(model().attributeExists("year"))
            .andExpect(model().attributeExists("month"));
    }

}
