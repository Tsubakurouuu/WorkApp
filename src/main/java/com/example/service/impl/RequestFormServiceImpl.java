package com.example.service.impl;

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

}
