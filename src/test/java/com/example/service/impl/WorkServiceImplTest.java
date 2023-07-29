package com.example.service.impl;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.model.Work;
import com.example.repository.WorkMapper;
import com.example.service.WorkService;

@SpringBootTest
class WorkServiceImplTest {
	
	@MockBean
	private WorkMapper mapper;
	
	@Autowired
    private WorkService workService;

	@Test
	@DisplayName("出勤時間登録テスト")
	void testInsertAttendance() {
		//モックのWorkを生成
		Work mockWork = new Work();
		//テスト対象のメソッドを実行してWorkを登録する
		workService.insertAttendance(mockWork);
		//mapperのinsertAttendanceが1度だけ呼び出されたことを確認
		verify(mapper, times(1)).insertAttendance(mockWork);
	}

	@Test
	void testUpdateLeaving() {
		fail("まだ実装されていません");
	}

	@Test
	void testSelectWork() {
		fail("まだ実装されていません");
	}

	@Test
	void testUpdateWork() {
		fail("まだ実装されていません");
	}

	@Test
	void testSelectWorkListMonth() {
		fail("まだ実装されていません");
	}

	@Test
	void testSelectWorkAttendance() {
		fail("まだ実装されていません");
	}

	@Test
	void testInsertWork() {
		fail("まだ実装されていません");
	}

	@Test
	void testSelectWorkInfoList() {
		fail("まだ実装されていません");
	}

}
