package com.example.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.model.MUser;
import com.example.repository.MUserMapper;
import com.example.service.MUserService;

@SpringBootTest
public class MUserServiceImplTest {

	@MockBean
	private MUserMapper mapper;
	
	@MockBean
    private PasswordEncoder encoder;
	
	@Autowired
	private MUserService userService;
	
	@Test
	@DisplayName("ユーザー一覧取得テスト")
	public void testSelectUserList() {
		//モックのMUserリストを生成
		List<MUser> mockUserList = new ArrayList<>();
		//MUserリストが空でないことを保証
		mockUserList.add(new MUser());
		//mapperのselectUserListメソッドが呼び出されたときにモックのMUserリストを返すように設定
		when(mapper.selectUserList()).thenReturn(mockUserList);
		//テスト対象のメソッドを実行してMUserのListを受け取る
		List<MUser> resultUserList = userService.selectUserList();
		//モックのMUserリストが返されることを確認
		assertSame(mockUserList, resultUserList);
	}

	@Test
	@DisplayName("ユーザー詳細取得テスト")
	public void testSelectUserDetail() {
		//ダミーデータを宣言(selectUserDetailメソッドの引数用)
		String testUserId = "testUser";
		//モックのMUserインスタンスの生成
		MUser mockUserDetail = new MUser();
		//モックのMUserにダミーデータのセット
		mockUserDetail.setUserId(testUserId);
		//mapperのselectUserDetailメソッド呼び出されたときにモックのMUserを返すように設定
		when(mapper.selectUserDetail(testUserId)).thenReturn(mockUserDetail);
		//テスト対象のメソッドを実行してMUserを受け取る
		MUser resultUserDetail = userService.selectUserDetail(testUserId);
		//モックのMUserが返されることを確認
		assertSame(mockUserDetail, resultUserDetail);
	}

	@Test
	@DisplayName("ユーザー新規登録テスト")
	public void testInsertUser() {
		//モックのMUserインスタンスの生成
		MUser mockUser = new MUser();
		//モックのMUserに初期パスワードを設定
		mockUser.setPassword("rawPassword");
		//パスワードエンコーダーが任意の文字列をエンコードした場合、"encodedPassword"を返すように設定
		when(encoder.encode(anyString())).thenReturn("encodedPassword");
		//テスト対象のメソッドを実行してMUserを登録する
		userService.insertUser(mockUser);
		//mapperのinsertUserが1度だけ呼び出されたことを確認
		verify(mapper, times(1)).insertUser(mockUser);
		//PasswordEncoderのencodeが1度だけ呼び出されたことを確認
		verify(encoder, times(1)).encode("rawPassword");
		//モックMUserのパスワードが正しくエンコードされたことを確認
        assertEquals("encodedPassword", mockUser.getPassword());
	}

	@Test
	@DisplayName("ユーザーのuserId一覧取得テスト")
	public void testSelectUserIdList() {
		//モックのStringリストを作成
		List<String> mockUserIdList = new ArrayList<>();
		//Stringリストが空でないことを保証
		mockUserIdList.add(new String());
		//mapperのselectUserIdListメソッドが呼び出されたときにモックのStringリストを返すように設定
		when(mapper.selectUserIdList()).thenReturn(mockUserIdList);
		//テスト対象のメソッドを実行してStringのListを受け取る
		List<String> resultUserIdList = userService.selectUserIdList();
		//モックのStringリストが返されることを確認
		assertSame(mockUserIdList, resultUserIdList);
		
	}

	@Test
	@DisplayName("ユーザーのuserId(Str)詳細取得テスト")
	public void testSelectUserIdStr() {
		//ダミーデータを宣言(selectUserIdStrメソッドの引数用)
		Integer testId = 1;
		//モックのMUserインスタンスの生成
		MUser mockUserDetail = new MUser();
		//モックのMUserにダミーデータのセット
		mockUserDetail.setId(testId);
		//mapperのselectUserIdStrメソッド呼び出されたときにモックのMUserを返すように設定
		when(mapper.selectUserIdStr(testId)).thenReturn(mockUserDetail);
		//テスト対象のメソッドを実行してMUserを受け取る
		MUser resultUserDetail = userService.selectUserIdStr(testId);
		//モックのMUserが返されることを確認
		assertSame(mockUserDetail, resultUserDetail);
	}
}

