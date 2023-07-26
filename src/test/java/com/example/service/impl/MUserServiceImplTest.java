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
	@DisplayName("ユーザーリスト取得テスト")
	public void testSelectUserList() {
		
		List<MUser> mockUserList = new ArrayList<>();
		mockUserList.add(new MUser());
		
		when(mapper.selectUserList()).thenReturn(mockUserList);
		
		List<MUser> resultList = userService.selectUserList();
		
		assertSame(mockUserList, resultList);
	}

	@Test
	public void testSelectUserDetail() {
		fail("まだ実装されていません");
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

