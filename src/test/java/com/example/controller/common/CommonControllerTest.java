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
        Integer testLeavingHour = 20;
        Integer testLeavingMinute = 50;
        Integer testRestHour = 1;
        Integer testRestMinute = 10;
        //テスト対象のメソッドを実行してInteger[]を受け取る
        Integer[] result = CommonController.calcWorkingOver(testAttendanceHour, testAttendanceMinute, testLeavingHour, testLeavingMinute, testRestHour, testRestMinute);
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
        Integer testLeavingHour = 17;
        Integer testLeavingMinute = 50;
        Integer testRestHour = 1;
        Integer testRestMinute = 10;
        //テスト対象のメソッドを実行してInteger[]を受け取る
        Integer[] result = CommonController.calcWorkingOver(testAttendanceHour, testAttendanceMinute, testLeavingHour, testLeavingMinute, testRestHour, testRestMinute);
        //calcWorkingOverメソッドの期待する値
        Integer[] expected = { 7, 20, 0, 0 };
        //calcWorkingOverメソッドの実行結果と期待値が同値であることを確認
        assertArrayEquals(expected, result);
	}

	@Test
	@DisplayName("5捨6入をして現在時分を返すメソッドのテスト1(分が切り捨てられるかのテスト)")
	void testRoundOff1() {
		//ダミーデータを宣言(roundOffメソッドの引数用)
		Integer testHour = 5;
		Integer testMinute = 23;
		//テスト対象のメソッドを実行してInteger[]を受け取る
		Integer[] result = CommonController.roundOff(testHour, testMinute);
		//roundOffメソッドの期待する値
		Integer[] expected = { 5, 20 };
		//roundOffメソッドの実行結果と期待値が同値であることを確認
		assertArrayEquals(expected, result);
	}
	
	@Test
	@DisplayName("5捨6入をして現在時分を返すメソッドのテスト2(分が切り上げられるかのテスト)")
	void testRoundOff2() {
		//ダミーデータを宣言(roundOffメソッドの引数用)
		Integer testHour = 5;
		Integer testMinute = 36;
		//テスト対象のメソッドを実行してInteger[]を受け取る
		Integer[] result = CommonController.roundOff(testHour, testMinute);
		//roundOffメソッドの期待する値
		Integer[] expected = { 5, 40 };
		//roundOffメソッドの実行結果と期待値が同値であることを確認
		assertArrayEquals(expected, result);
	}
	
	@Test
	@DisplayName("5捨6入をして現在時分を返すメソッドのテスト3(時が繰り上げられるかのテスト)")
	void testRoundOff3() {
		//ダミーデータを宣言(roundOffメソッドの引数用)
		Integer testHour = 14;
		Integer testMinute = 58;
		//テスト対象のメソッドを実行してInteger[]を受け取る
		Integer[] result = CommonController.roundOff(testHour, testMinute);
		//roundOffメソッドの期待する値
		Integer[] expected = { 15, 0 };
		//roundOffメソッドの実行結果と期待値が同値であることを確認
		assertArrayEquals(expected, result);
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
