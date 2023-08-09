package com.example.controller.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.example.repository.MUserMapper;
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
	
	@MockBean
	private MUserMapper mapper;

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
	@DisplayName("新規登録ボタン押下時のメソッドのテスト")
	void testPostAdminUserNew() throws Exception {
	    //ダミーデータを宣言
	    String testUserId = "testUserId";
	    String testPassword = "password";
	    String testLastName = "田中";
	    String testFirstName = "太郎";
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	    Date changeDate = sdf.parse("2000/01/01");
	    String testBirthday = sdf.format(changeDate);
	    //mockMvcを使って"/admin/user/new"にPOSTリクエストを送る
	    mockMvc.perform(post("/admin/user/new")
	    		//HTTPリクエストのデータを設定
	            .param("userId", testUserId)
	            .param("password", testPassword)
	            .param("lastName", testLastName)
	            .param("firstName", testFirstName)
	            .param("birthday", testBirthday))
	        //HTTPステータスが302（Found）であることを確認
	        .andExpect(status().isFound())
	        //"/admin/users"へリダイレクトされることを確認
	        .andExpect(redirectedUrl("/admin/users"));
	    //ArgumentCaptorの生成(MUserの引数をキャッチするため)
	    ArgumentCaptor<MUser> argument = ArgumentCaptor.forClass(MUser.class);
	    //userServiceのinsertUserが1度だけ呼び出されたことを確認
	    verify(userService, times(1)).insertUser(argument.capture());
	    //ArgumentCaptorでキャッチした引数の取得
	    MUser userArgument = argument.getValue();
	    //insertUserメソッドの実行結果と期待値が同値であることを確認
	    assertEquals(testUserId, userArgument.getUserId());
	    assertEquals(testPassword, userArgument.getPassword());
	    assertEquals(testLastName, userArgument.getLastName());
	    assertEquals(testFirstName, userArgument.getFirstName());
	    assertEquals(changeDate, userArgument.getBirthday());
	}


}
