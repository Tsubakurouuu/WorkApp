package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.RequestForm;
import com.example.repository.RequestFormMapper;
import com.example.service.RequestFormService;

@Service
public class RequestFormServiceImpl implements RequestFormService {
	@Autowired
	private RequestFormMapper mapper;
	
	//申請フォーム登録
	@Override
	public void insertForm(RequestForm form) {
		mapper.insertForm(form);
	}
	
	//申請フォーム一覧取得
	@Override
	public List<RequestForm> selectRequestFormList() {
		return mapper.selectRequestFormList();
	}
	
	//申請フォーム詳細取得
	@Override
	public RequestForm selectRequestFormDetail(Integer id) {
		return mapper.selectRequestFormDetail(id);
	}

}
