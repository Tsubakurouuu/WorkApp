package com.example.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.model.MUser;
import com.example.service.MUserService;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
	
	@InjectMocks
    private UserDetailsServiceImpl userDetailsService;
	
	@Mock
    private MUserService userService;
	
	@Test
	@DisplayName("指定されたユーザーIDを使用して、ユーザーの詳細をロードするメソッドのテスト")
	void testLoadUserByUsername() {
		//ダミーデータを宣言(selectUserDetail,loadUserByUsernameの引数用)
		String testUserId = "testUserId";
		String testPassword = "testPassword";
		String testRole = "testRole";
		//モックのMUserインスタンスの生成
		MUser mockUser = new MUser();
		//モックのMUserにダミーデータのセット
		mockUser.setUserId(testUserId);
		mockUser.setPassword(testPassword);
		mockUser.setRole(testRole);
		//userServiceのselectUserDetailメソッドを呼び出したときにモックのMUserを返すように設定
		when(userService.selectUserDetail(testUserId)).thenReturn(mockUser);
		//テスト対象のメソッドを実行してUserDetailsを受け取る
		UserDetails resultUserDetails = userDetailsService.loadUserByUsername(testUserId);
		//モックのMUserのuserIdが返されることを確認
		assertEquals(mockUser.getUserId(), resultUserDetails.getUsername());
		//モックのMUserのpasswordが返されることを確認
		assertEquals(mockUser.getPassword(), resultUserDetails.getPassword());
		//boolean型変数をfalseで宣言
		boolean flg = false;
		//UserDetailsオブジェクトの権限リストをループで回す
		for (GrantedAuthority authority : resultUserDetails.getAuthorities()) {
			//権限が期待するものと一致するかチェック
		    if (authority.getAuthority().equals("testRole")) {
		    	//一致すればflgをtrueに変えてfor文から抜ける
		        flg = true;
		        break;
		    }
		}
		//モックのMUserのroleがtrueであることを確認
		assertTrue(flg);
	}


}
