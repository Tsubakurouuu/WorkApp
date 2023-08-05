package com.example.controller.common;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

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
	@DisplayName("時分フォーム入力用メソッドのテスト")
	void testFormNumbers() {
		//モックのModelを生成
		Model mockModel = new ExtendedModelMap();
		//モックのList<Integer>を宣言(Modelの期待値)
		List<Integer> mockHourNumbers = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
		List<Integer> mockMinuteNumbers = Arrays.asList(0, 10, 20, 30, 40, 50);
		//テスト対象のメソッドを実行する
		CommonController.formNumbers(mockModel);
		//モックのModelが"hourNumbers"を持っているかの確認
		assertTrue(mockModel.containsAttribute("hourNumbers"));
		//モックのModelが"minuteNumbers"を持っているかの確認
		assertTrue(mockModel.containsAttribute("minuteNumbers"));
		//"hourNumbers"と期待値が同じであるかどうかを確認
		assertEquals(mockHourNumbers, mockModel.getAttribute("hourNumbers"));
		//"minuteNumbers"と期待値が同じであるかどうかを確認
		assertEquals(mockMinuteNumbers, mockModel.getAttribute("minuteNumbers"));
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
