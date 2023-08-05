package com.example.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkStatusServiceTest {
	
	@InjectMocks
    private WorkStatusService workStatusService;

	@Test
	@DisplayName("WorkStatusカラムのMapテスト")
	void testGetWorkStatusMap() {
		//テスト対象のメソッドを実行してworkStatusMapを受け取る
		Map<String, Integer> workStatusMap = workStatusService.getWorkStatusMap();
        //workStatusMapの総数が正しいかの確認
        assertEquals(3, workStatusMap.size());
        //workStatusMapのキーと値が正しいことを確認
        assertEquals(1, workStatusMap.get("出勤"));
        assertEquals(2, workStatusMap.get("欠勤"));
        assertEquals(3, workStatusMap.get("有休"));
	}

}
