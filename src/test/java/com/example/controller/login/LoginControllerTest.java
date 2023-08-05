package com.example.controller.login;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.config.AuthenticationSuccessHandler;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
class LoginControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AuthenticationSuccessHandler authenticationSuccessHandler;	
	
	@Test
	@DisplayName("ログイン画面に遷移するためのメソッドのテスト")
	void testGetLogin() throws Exception {
		//mockMvcを使って/loginにGETリクエストを送る
		mockMvc.perform(get("/login"))
			//HTTPステータスが200（OK）であることを検証
			.andExpect(status().isOk())
			//返されたビューの名前が"login/login"であることを検証
			.andExpect(view().name("login/login"));
	}

}
