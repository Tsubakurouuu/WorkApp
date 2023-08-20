package com.example.service.impl;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.model.Work;
import com.example.repository.WorkMapper;

@ExtendWith(MockitoExtension.class)
class WorkServiceImplTest {
	
	@InjectMocks
	private WorkServiceImpl workService;
	
	@Mock
	private WorkMapper mapper;

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
	@DisplayName("退勤時間登録（更新）テスト")
	void testUpdateLeaving() {
		//モックのWorkを生成
		Work mockWork = new Work();
		//テスト対象のメソッドを実行してWorkを更新する
		workService.updateLeaving(mockWork);
		//mapperのupdateLeavingが1度だけ呼び出されたことを確認
		verify(mapper, times(1)).updateLeaving(mockWork);
	}

	@Test
	@DisplayName("勤怠情報取得テスト")
	void testSelectWork() {
		//ダミーデータを宣言(selectWorkメソッドの引数用)
		int testId = 1;
		//モックのWorkインスタンスの生成
		Work mockWorkDetail = new Work();
		//モックのWorkにダミーデータのセット
		mockWorkDetail.setId(testId);
		//mapperのselectWorkメソッド呼び出されたときにモックのWorkを返すように設定
		when(mapper.selectWork(testId)).thenReturn(mockWorkDetail);
		//テスト対象のメソッドを実行してWorkを受け取る
		Work resultWorkDetail = workService.selectWork(testId);
		//モックのWorkが返されることを確認
		assertSame(mockWorkDetail, resultWorkDetail);
	}

	@Test
	@DisplayName("勤怠情報更新テスト")
	void testUpdateWork() {
		//モックのWorkを生成
		Work mockWork = new Work();
		//テスト対象のメソッドを実行してWorkを更新する
		workService.updateWork(mockWork);
		//mapperのupdateWorkが1度だけ呼び出されたことを確認
		verify(mapper, times(1)).updateWork(mockWork);
	}

	@Test
	@DisplayName("勤怠情報月毎取得テスト")
	void testSelectWorkListMonth() {
		//ダミーデータを宣言(selectWorkListMonthメソッドの引数用)
		int testUserId = 1;
		int testYear = 1;
		int testMonth = 1;
		//モックのWorkリストを生成
		List<Work> mockWorkList = new ArrayList<>();
		//Workリストが空でないことを保証
		mockWorkList.add(new Work());
		//mapperのselectWorkListMonthメソッド呼び出されたときにモックのWorkを返すように設定
		when(mapper.selectWorkListMonth(testUserId, testYear, testMonth)).thenReturn(mockWorkList);
		//テスト対象のメソッドを実行してWorkのListを受け取る
		List<Work> resultWorkList = workService.selectWorkListMonth(testUserId, testYear, testMonth);
		//モックのWorkが返されることを確認
		assertSame(mockWorkList, resultWorkList);
		
	}

	@Test
	@DisplayName("同日勤怠情報取得（退勤ボタン押下時）テスト")
	void testSelectWorkAttendance() {
		//ダミーデータを宣言(selectWorkAttendanceメソッドの引数用)
		int testUserId = 1;
		int testYear = 1;
		int testMonth = 1;
		int testDate = 1;
		//モックのWorkを生成
		Work mockWorkDetail = new Work();
		//モックのWorkにダミーデータのセット
		mockWorkDetail.setUserId(testUserId);
		mockWorkDetail.setYear(testYear);
		mockWorkDetail.setMonth(testMonth);
		mockWorkDetail.setDate(testDate);
		//mapperのselectWorkAttendanceメソッド呼び出されたときにモックのMUserを返すように設定
		when(mapper.selectWorkAttendance(testUserId, testYear, testMonth, testDate)).thenReturn(mockWorkDetail);
		//テスト対象のメソッドを実行してWorkを受け取る
		Work resultWorkDetail = workService.selectWorkAttendance(testUserId, testYear, testMonth, testDate);
		//モックのWorkが返されることを確認
		assertSame(mockWorkDetail, resultWorkDetail);
	}

	@Test
	@DisplayName("勤怠情報新規登録テスト")
	void testInsertWork() {
		//モックのWorkインスタンスの生成
		Work mockWork = new Work();
		//テスト対象のメソッドを実行してWorkを登録する
		workService.insertWork(mockWork);
		//mapperのinsertAttendanceが1度だけ呼び出されたことを確認
		verify(mapper, times(1)).insertWork(mockWork);
	}

	@Test
	@DisplayName("勤怠情報取得テスト")
	void testSelectWorkInfoList() {
		//ダミーデータを宣言(selectWorkInfoListメソッドの引数用)
		int testYear = 1;
		int testMonth = 1;
		int testDate = 1;
		//モックのWorkリストを生成
		List<Work> mockWorkList = new ArrayList<>();
		//Workリストが空でないことを保証
		mockWorkList.add(new Work());
		//mapperのselectWorkInfoListメソッド呼び出されたときにモックのWorkを返すように設定
		when(mapper.selectWorkInfoList(testYear, testMonth, testDate)).thenReturn(mockWorkList);
		//テスト対象のメソッドを実行してWorkのListを受け取る
		List<Work> resultWorkList = workService.selectWorkInfoList(testYear, testMonth, testDate);
		//モックのWorkが返されることを確認
		assertSame(mockWorkList, resultWorkList);
	}

}
