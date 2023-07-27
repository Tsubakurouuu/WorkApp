package com.example.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.model.MUser;
import com.example.repository.MUserMapper;
import com.example.service.MUserService;

@SpringBootTest
public class MUserServiceImplTest {

	@MockBean
	private MUserMapper mapper;
	
	@Autowired
	private MUserService userService;
	
	@Test
	@DisplayName("ユーザー一覧取得テスト")
	public void testSelectUserList() {
		//モックのMUserリストを作成
		List<MUser> mockUserList = new ArrayList<>();
		//MUserリストが空でないことを保証
		mockUserList.add(new MUser());
		//mapperのselectUserListメソッドが呼び出されたときにモックのMUserリストを返すように設定
		when(mapper.selectUserList()).thenReturn(mockUserList);
		//テスト対象のメソッドを呼び出して結果を受け取る
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
		//テスト対象のメソッドを呼び出して結果を受け取る
		MUser resultUserDetail = userService.selectUserDetail(testUserId);
		//モックのMUserが返されることを確認
		assertSame(mockUserDetail, resultUserDetail);
	}

	@Test
	public void testInsertUser() {
		fail("まだ実装されていません");
	}

	@Test
	public void testSelectUserIdList() {
		fail("まだ実装されていません");
	}

	@Test
	public void testSelectUserIdStr() {
		fail("まだ実装されていません");
	}
}

