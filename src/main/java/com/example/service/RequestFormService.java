package com.example.service;

import java.util.List;

import com.example.model.RequestForm;

public interface RequestFormService {
	
	//申請フォーム登録
	public void insertForm(RequestForm form);
	
	//申請フォーム一覧取得
	public List<RequestForm> selectRequestFormList();
	
	//申請フォーム詳細取得
	public RequestForm selectRequestFormDetail(Integer id);
	
	//申請フォーム削除
	public void deleteRequestForm(Integer id);
	
}
