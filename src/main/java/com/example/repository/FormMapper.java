package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.model.Form;

@Mapper
public interface FormMapper {
	//申請フォーム登録
	public int insertForm(Form form);
}
