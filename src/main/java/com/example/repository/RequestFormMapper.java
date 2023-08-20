package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.model.RequestForm;

@Mapper
public interface RequestFormMapper {
	
	//申請フォーム登録
	public int insertForm(RequestForm form);
	
	//申請フォーム一覧取得
	public List<RequestForm> selectRequestFormList();
	
	//申請フォーム詳細取得
	public RequestForm selectRequestFormDetail(int id);
	
	//申請フォーム削除
	public void deleteRequestForm(@Param("id") int id);
	
}
