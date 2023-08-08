package com.example.controller.admin;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import com.example.service.MUserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserNewController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserNewControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@MockBean
	private MUserService userService;

	@Test
	@DisplayName("ユーザー登録画面に遷移するためのメソッドのテスト")
	void testGetAdminUserNew() throws Exception {
		//mockMvcを使って/admin/user/newにGETリクエストを送る
	    mockMvc.perform(get("/admin/user/new"))
	        //HTTPステータスが200（OK）であることを確認
	        .andExpect(status().isOk())
	        //返されたビューの名前が"admin/form_index"であることを確認
	        .andExpect(view().name("admin/user_new"));
	}

	@Test
	void testPostAdminUserNew() {
		fail("まだ実装されていません");
	}

}
