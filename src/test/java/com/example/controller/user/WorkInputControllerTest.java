package com.example.controller.user;

import static org.junit.jupiter.api.Assertions.fail;

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
	void testGetUserWorkInput() {
		fail("まだ実装されていません");
		
	}

	@Test
	void testPostUserWorkAttendance() {
		fail("まだ実装されていません");
	}

	@Test
	void testPostUserWorkLeaving() {
		fail("まだ実装されていません");
	}

}
