package com.example.controller.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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
	@DisplayName("新規登録ボタン押下時のメソッドのテスト1(新規登録成功時)")
	void testPostAdminUserNew1() throws Exception {
	    //ダミーデータを宣言(insertUserメソッドの引数用)
	    String testUserId = "testUserId";
	    String testPassword = "password";
	    String testLastName = "ゆにっと";
	    String testFirstName = "てすと";
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
	
	@Test
	@DisplayName("新規登録ボタン押下時のメソッドのテスト2(新規登録失敗時(ValidGroup1))")
	void testPostAdminUserNew2() throws Exception {
	    //ダミーデータを宣言
	    String testUserId = " ";
	    String testPassword = "パスワード";
	    String testLastName = " ";
	    String testFirstName = " ";
	    String testBirthday = null;
	    //mockMvcを使って"/admin/user/new"にPOSTリクエストを送る
	    MvcResult mvcResult = mockMvc.perform(post("/admin/user/new")
	    		//HTTPリクエストのデータを設定
	            .param("userId", testUserId)
	            .param("password", testPassword)
	            .param("lastName", testLastName)
	            .param("firstName", testFirstName)
	            .param("birthday", testBirthday))
	        //HTTPステータスが200（OK）であることを確認
	        .andExpect(status().isOk())
	        //返されたビューの名前が"admin/user_new"であることを確認
	        .andExpect(view().name("admin/user_new"))
	        //mvcResultオブジェクトを返す
	        .andReturn();
	    //userServiceのinsertUserが呼び出されていないことを確認
	    verify(userService, times(0)).insertUser(any(MUser.class));
	    //バリデーションエラーの詳細情報を取得
	    BindingResult bindingResult = (BindingResult) mvcResult.getModelAndView().getModel().get("org.springframework.validation.BindingResult.userNewForm");
	    //バリデーションエラーが発生していることを確認
	    assertTrue(bindingResult.hasErrors());
	    //バリデーションエラーの数が５つあることを確認
	    assertEquals(5, bindingResult.getErrorCount());
	    //バリデーションエラーメッセージをListに格納
	    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
	    //Listに格納したバリデーションエラーメッセージを順番に抜き出す
	    for(FieldError error : fieldErrors) {
	    	//エラーの発生したカラムを取得
	    	String fieldName = error.getField();
	    	//エラーメッセージの取得
	    	String errorMessage = error.getDefaultMessage();
	    	//userIdの比較
	    	if("userId".equals(fieldName)) {
	    		//エラーメッセージと期待値が同一であることを確認
	    		assertEquals("must not be blank", errorMessage);
	    	}
	    	//passwordの比較
	    	if("password".equals(fieldName)) {
	    		//エラーメッセージと期待値が同一であることを確認
	    		assertEquals("must match \"^[a-zA-Z0-9]+$\"", errorMessage);
	    	}
	    	//lastNameの比較
	    	if("lastName".equals(fieldName)) {
	    		//エラーメッセージと期待値が同一であることを確認
	    		assertEquals("must not be blank", errorMessage);
	    	}
	    	//firstNameの比較
	    	if("firstName".equals(fieldName)) {
	    		//エラーメッセージと期待値が同一であることを確認
	    		assertEquals("must not be blank", errorMessage);
	    	}
	    	//birthdayの比較
	    	if("birthday".equals(fieldName)) {
	    		//エラーメッセージと期待値が同一であることを確認
	    		assertEquals("must not be null", errorMessage);
	    	}
	    }
	}
	
	@Test
	@DisplayName("新規登録ボタン押下時のメソッドのテスト3(新規登録失敗時(ValidGroup2))")
	void testPostAdminUserNew3() throws Exception {
		//ダミーデータを宣言(insertUserメソッドの引数用)
	    String testUserId = "test";
	    String testPassword = "passw";
	    String testLastName = "ゆにっと";
	    String testFirstName = "てすと";
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	    Date changeDate = sdf.parse("2000/01/01");
	    String testBirthday = sdf.format(changeDate);
	    //mockMvcを使って"/admin/user/new"にPOSTリクエストを送る
	    MvcResult mvcResult = mockMvc.perform(post("/admin/user/new")
	    		//HTTPリクエストのデータを設定
	            .param("userId", testUserId)
	            .param("password", testPassword)
	            .param("lastName", testLastName)
	            .param("firstName", testFirstName)
	            .param("birthday", testBirthday))
	        //HTTPステータスが200（OK）であることを確認
	        .andExpect(status().isOk())
	        //返されたビューの名前が"admin/user_new"であることを確認
	        .andExpect(view().name("admin/user_new"))
	        //mvcResultオブジェクトを返す
	        .andReturn();
	    //userServiceのinsertUserが呼び出されていないことを確認
	    verify(userService, times(0)).insertUser(any(MUser.class));
	    //バリデーションエラーの詳細情報を取得
	    BindingResult bindingResult = (BindingResult) mvcResult.getModelAndView().getModel().get("org.springframework.validation.BindingResult.userNewForm");
	    //バリデーションエラーが発生していることを確認
	    assertTrue(bindingResult.hasErrors());
	    //バリデーションエラーの数が2つあることを確認
	    assertEquals(2, bindingResult.getErrorCount());
	    //バリデーションエラーメッセージをListに格納
	    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
	    //Listに格納したバリデーションエラーメッセージを順番に抜き出す
	    for(FieldError error : fieldErrors) {
	    	//エラーの発生したカラムを取得
	    	String fieldName = error.getField();
	    	//エラーメッセージの取得
	    	String errorMessage = error.getDefaultMessage();
	    	//userIdの比較
	    	if("userId".equals(fieldName)) {
	    		//エラーメッセージと期待値が同一であることを確認
	    		assertEquals("length must be between 5 and 2147483647", errorMessage);
	    	}
	    	//passwordの比較
	    	if("password".equals(fieldName)) {
	    		//エラーメッセージと期待値が同一であることを確認
	    		assertEquals("length must be between 6 and 2147483647", errorMessage);
	    	}
	    }
	}
	
	@Test
	@DisplayName("新規登録ボタン押下時のメソッドのテスト4(新規登録失敗時(@DateTimeFormat))")
	void testPostAdminUserNew4() throws Exception {
		//ダミーデータを宣言(insertUserメソッドの引数用)
	    String testUserId = "testUserId";
	    String testPassword = "password";
	    String testLastName = "ゆにっと";
	    String testFirstName = "てすと";
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date changeDate = sdf.parse("2000-01-01");
	    String testBirthday = sdf.format(changeDate);
	    //mockMvcを使って"/admin/user/new"にPOSTリクエストを送る
	    MvcResult mvcResult = mockMvc.perform(post("/admin/user/new")
	    		//HTTPリクエストのデータを設定
	            .param("userId", testUserId)
	            .param("password", testPassword)
	            .param("lastName", testLastName)
	            .param("firstName", testFirstName)
	            .param("birthday", testBirthday))
	        //HTTPステータスが200（OK）であることを確認
	        .andExpect(status().isOk())
	        //返されたビューの名前が"admin/user_new"であることを確認
	        .andExpect(view().name("admin/user_new"))
	        //mvcResultオブジェクトを返す
	        .andReturn();
	    //userServiceのinsertUserが呼び出されていないことを確認
	    verify(userService, times(0)).insertUser(any(MUser.class));
	    //バリデーションエラーの詳細情報を取得
	    BindingResult bindingResult = (BindingResult) mvcResult.getModelAndView().getModel().get("org.springframework.validation.BindingResult.userNewForm");
	    //バリデーションエラーが発生していることを確認
	    assertTrue(bindingResult.hasErrors());
	    //バリデーションエラーの数が1つあることを確認
	    assertEquals(1, bindingResult.getErrorCount());
	    //バリデーションエラーメッセージをListに格納
	    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
	    //Listに格納したバリデーションエラーメッセージを順番に抜き出す
	    for(FieldError error : fieldErrors) {
	    	//エラーの発生したカラムを取得
	    	String fieldName = error.getField();
	    	//birthdayの比較
	    	if("birthday".equals(fieldName)) {
	    		//エラーメッセージと期待値が同一であることを確認
	    	    assertEquals("typeMismatch", error.getCode());
	    	}
	    }
	}

	
}
