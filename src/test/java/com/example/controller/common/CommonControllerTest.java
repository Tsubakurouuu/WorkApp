package com.example.controller.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommonControllerTest {

	@Test
	@DisplayName("就業時間と残業時間を計算するメソッドのテスト1(残業時間が発生している場合)")
	void testCalcWorkingOver1() {
		//ダミーデータを宣言(calcWorkingOverメソッドの引数用)
        Integer testAttendanceHour = 9;
        Integer testAttendanceMinute = 20;
        Integer teestLeavingHour = 20;
        Integer testLeavingMinute = 50;
        Integer testRestHour = 1;
        Integer testRestMinute = 10;
        //テスト対象のメソッドを実行してWorkを受け取る
        Integer[] result = CommonController.calcWorkingOver(testAttendanceHour, testAttendanceMinute, teestLeavingHour, testLeavingMinute, testRestHour, testRestMinute);
        //calcWorkingOverメソッドの期待する値
        Integer[] expected = { 10, 20, 2, 20 };
        //calcWorkingOverメソッドの実行結果と期待値が同値であることを確認
        assertArrayEquals(expected, result);
	}
	
	@Test
	@DisplayName("就業時間と残業時間を計算するメソッドのテスト2(就業時間が定時我をしている場合)")
	void testCalcWorkingOver2() {
		//ダミーデータを宣言(calcWorkingOverメソッドの引数用)
        Integer testAttendanceHour = 9;
        Integer testAttendanceMinute = 20;
        Integer teestLeavingHour = 17;
        Integer testLeavingMinute = 50;
        Integer testRestHour = 1;
        Integer testRestMinute = 10;
        //テスト対象のメソッドを実行してWorkを受け取る
        Integer[] result = CommonController.calcWorkingOver(testAttendanceHour, testAttendanceMinute, teestLeavingHour, testLeavingMinute, testRestHour, testRestMinute);
        // 期待値
        Integer[] expected = { 7, 20, 0, 0 };
        // テストの実行
        assertArrayEquals(expected, result);
	}

	@Test
	void testRoundOff() {
		fail("まだ実装されていません");
	}

	@Test
	void testFormNumbers() {
		fail("まだ実装されていません");
	}

	@Test
	void testCalcRest() {
		fail("まだ実装されていません");
	}

	@Test
	void testConfirmWorkForm() {
		fail("まだ実装されていません");
	}

	@Test
	void testFormRedirectRequestFormFormRequestFormForm() {
		fail("まだ実装されていません");
	}

	@Test
	void testFormRedirectWorkEditFormWorkEditForm() {
		fail("まだ実装されていません");
	}

}
