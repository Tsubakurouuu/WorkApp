package com.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Form;
import com.example.repository.FormMapper;
import com.example.service.FormService;

@Service
public class FormServiceImpl implements FormService {
	@Autowired
	private FormMapper mapper;
	
	//申請フォーム登録
	@Override
	public void insertForm(Form form) {
		mapper.insertForm(form);
	}

}
