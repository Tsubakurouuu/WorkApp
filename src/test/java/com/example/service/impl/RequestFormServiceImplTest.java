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

import com.example.model.RequestForm;
import com.example.repository.RequestFormMapper;

@ExtendWith(MockitoExtension.class)
class RequestFormServiceImplTest {
	
	@InjectMocks
	private RequestFormServiceImpl requestFormService;
	
	@Mock
	private RequestFormMapper mapper;

	@Test
	@DisplayName("申請フォーム登録登録テスト")
	void testInsertForm() {
		//モックのRequestFormを生成
		RequestForm mockRequestForm = new RequestForm();
		//テスト対象のメソッドを実行してRequestFormを登録する
		requestFormService.insertForm(mockRequestForm);
		//mapperのinsertFormが1度だけ呼び出されたことを確認
		verify(mapper, times(1)).insertForm(mockRequestForm);
	}

	@Test
	@DisplayName("申請フォーム一覧取得テスト")
	void testSelectRequestFormList() {
		//モックのRequestFormリストを生成
		List<RequestForm> mockRequestFormList = new ArrayList<>();
		//RequestFormリストが空でないことを保証
		mockRequestFormList.add(new RequestForm());
		//mapperのselectRequestFormListメソッド呼び出されたときにモックのRequestFormを返すように設定
		when(mapper.selectRequestFormList()).thenReturn(mockRequestFormList);
		//テスト対象のメソッドを実行してRequestFormのListを受け取る
		List<RequestForm> resultRequestFormList = requestFormService.selectRequestFormList();
		//モックのRequestFormが返されることを確認
		assertSame(mockRequestFormList, resultRequestFormList);
	}

	@Test
	@DisplayName("申請フォーム詳細取得テスト")
	void testSelectRequestFormDetail() {
		//ダミーデータを宣言(selectRequestFormDetailメソッドの引数用)
		Integer testId = 1;
		//モックのRequestFormを生成
		RequestForm mockRequestFormDetail = new RequestForm();
		//モックのRequestFormにダミーデータのセット
		mockRequestFormDetail.setId(testId);
		//mapperのselectWorkメソッドが呼び出されたときにモックのRequestFormを返すように設定
		when(mapper.selectRequestFormDetail(testId)).thenReturn(mockRequestFormDetail);
		//テスト対象のメソッドを実行してRequestFormを受け取る
		RequestForm resultRequestFormDetail = requestFormService.selectRequestFormDetail(testId);
		//モックのRequestFormが返されることを確認
		assertSame(mockRequestFormDetail, resultRequestFormDetail);
	}

	@Test
	@DisplayName("申請フォーム削除テスト")
	void testDeleteRequestForm() {
		//ダミーデータを宣言(deleteRequestFormメソッドの引数用)
		Integer testId = 1;
		//テスト対象のメソッドを実行してRequestFormを削除する
		requestFormService.deleteRequestForm(testId);
		//mapperのdeleteRequestFormが1度だけ呼び出されたことを確認
		verify(mapper, times(1)).deleteRequestForm(testId);
	}

}
