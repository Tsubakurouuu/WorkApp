package com.example.application.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class WorkStatusService {
	//WorkStatusのMapを作成する
	public Map<String, Integer> getWorkStatusMap() {
		Map<String, Integer> workStatusMap = new LinkedHashMap<>();
		workStatusMap.put("出勤", 1);
		workStatusMap.put("欠勤", 2);
		workStatusMap.put("有休", 3);
		workStatusMap.put("半休", 4);
		return workStatusMap;
	}
}
