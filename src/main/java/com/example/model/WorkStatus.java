package com.example.model;

public enum WorkStatus {
	出勤(0),
	欠勤(1),
	有休(2),
	半休(3),
	早退(4),
	遅刻(5);
	
	private final int value;
	
	WorkStatus(int value) {
		this.value = value;
	}
	
	public int getValue() {
        return value;
    }
}
