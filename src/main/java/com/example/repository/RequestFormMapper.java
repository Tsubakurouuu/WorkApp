package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.model.RequestForm;

@Mapper
public interface RequestFormMapper {
	//申請フォーム登録
	public int insertForm(RequestForm form);
}
