package com.example.common;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.example.form.RequestFormForm;
import com.example.form.WorkEditForm;

class CommonUtilsTest {

	@Test
	@DisplayName("就業時間と残業時間を計算するメソッドのテスト1(残業時間が発生している場合)")
	void testCalcWorkingOver1() {
		//ダミーデータを宣言(calcWorkingOverメソッドの引数用)
        int testAttendanceHour = 9;
        int testAttendanceMinute = 20;
        int testLeavingHour = 20;
        int testLeavingMinute = 50;
        int testRestHour = 1;
        int testRestMinute = 10;
        //テスト対象のメソッドを実行してint[]を受け取る
        int[] result = CommonUtils.calcWorkingOver(testAttendanceHour, testAttendanceMinute, testLeavingHour, testLeavingMinute, testRestHour, testRestMinute);
        //calcWorkingOverメソッドの期待する値
        int[] expected = { 10, 20, 2, 20 };
        //calcWorkingOverメソッドの実行結果と期待値が同値であることを確認
        assertArrayEquals(expected, result);
	}
	
	@Test
	@DisplayName("就業時間と残業時間を計算するメソッドのテスト2(就業時間が定時我をしている場合)")
	void testCalcWorkingOver2() {
		//ダミーデータを宣言(calcWorkingOverメソッドの引数用)
        int testAttendanceHour = 9;
        int testAttendanceMinute = 20;
        int testLeavingHour = 17;
        int testLeavingMinute = 50;
        int testRestHour = 1;
        int testRestMinute = 10;
        //テスト対象のメソッドを実行してint[]を受け取る
        int[] result = CommonUtils.calcWorkingOver(testAttendanceHour, testAttendanceMinute, testLeavingHour, testLeavingMinute, testRestHour, testRestMinute);
        //calcWorkingOverメソッドの期待する値
        int[] expected = { 7, 20, 0, 0 };
        //calcWorkingOverメソッドの実行結果と期待値が同値であることを確認
        assertArrayEquals(expected, result);
	}

	@Test
	@DisplayName("5捨6入をして現在時分を返すメソッドのテスト1(分が切り捨てられるかのテスト)")
	void testRoundOff1() {
		//ダミーデータを宣言(roundOffメソッドの引数用)
		int testHour = 5;
		int testMinute = 23;
		//テスト対象のメソッドを実行してint[]を受け取る
		int[] result = CommonUtils.roundOff(testHour, testMinute);
		//roundOffメソッドの期待する値
		int[] expected = { 5, 20 };
		//roundOffメソッドの実行結果と期待値が同値であることを確認
		assertArrayEquals(expected, result);
	}
	
	@Test
	@DisplayName("5捨6入をして現在時分を返すメソッドのテスト2(分が切り上げられるかのテスト)")
	void testRoundOff2() {
		//ダミーデータを宣言(roundOffメソッドの引数用)
		int testHour = 5;
		int testMinute = 36;
		//テスト対象のメソッドを実行してint[]を受け取る
		int[] result = CommonUtils.roundOff(testHour, testMinute);
		//roundOffメソッドの期待する値
		int[] expected = { 5, 40 };
		//roundOffメソッドの実行結果と期待値が同値であることを確認
		assertArrayEquals(expected, result);
	}
	
	@Test
	@DisplayName("5捨6入をして現在時分を返すメソッドのテスト3(時が繰り上げられるかのテスト)")
	void testRoundOff3() {
		//ダミーデータを宣言(roundOffメソッドの引数用)
		int testHour = 14;
		int testMinute = 58;
		//テスト対象のメソッドを実行してint[]を受け取る
		int[] result = CommonUtils.roundOff(testHour, testMinute);
		//roundOffメソッドの期待する値
		int[] expected = { 15, 0 };
		//roundOffメソッドの実行結果と期待値が同値であることを確認
		assertArrayEquals(expected, result);
	}

	@Test
	@DisplayName("時分フォーム入力用メソッドのテスト")
	void testFormNumbers() {
		//モックのModelを生成
		Model mockModel = new ExtendedModelMap();
		//モックのList<int>を宣言(Modelの期待値)
		List<Integer> mockHourNumbers = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
		List<Integer> mockMinuteNumbers = Arrays.asList(0, 10, 20, 30, 40, 50);
		//テスト対象のメソッドを実行する
		CommonUtils.formNumbers(mockModel);
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
	@DisplayName("休憩時間に1or0をセットするメソッドのテスト1(返り値が1の時)")
	void testCalcRest1() {
		//ダミーデータを宣言(calcRestメソッドの引数用)
		int testAttendanceHour = 9;
        int testAttendanceMinute = 20;
        int testLeavingHour = 20;
        int testLeavingMinute = 50;
        //テスト対象のメソッドを実行してintを受け取る
        int result = CommonUtils.calcRest(testAttendanceHour, testAttendanceMinute, testLeavingHour, testLeavingMinute);
        //calcRestメソッドの期待する値
        int expected = 1;
        //calcRestメソッドの実行結果と期待値が同値であることを確認
        assertEquals(expected, result);
	}
	
	@Test
	@DisplayName("休憩時間に1or0をセットするメソッドのテスト2(返り値が0の時)")
	void testCalcRest2() {
		//ダミーデータを宣言(calcRestメソッドの引数用)
		int testAttendanceHour = 9;
        int testAttendanceMinute = 20;
        int testLeavingHour = 10;
        int testLeavingMinute = 10;
        //テスト対象のメソッドを実行してintを受け取る
        int result = CommonUtils.calcRest(testAttendanceHour, testAttendanceMinute, testLeavingHour, testLeavingMinute);
        //calcRestメソッドの期待する値
        int expected = 0;
        //calcRestメソッドの実行結果と期待値が同値であることを確認
        assertEquals(expected, result);
	}

	@Test
	@DisplayName("入力された出勤時間、退勤時間、休憩時間が時間軸として正しいかどうかを判断するメソッドのテスト1(未来日付の指定)")
	void testConfirmWorkForm1() {
		//ダミーデータを宣言(confirmWorkFormメソッドの引数用)
		int testYear = 2024;
		int testMonth = 3;
		int testDate = 15;
		int testWorkStatus = 1;
		int testAttendanceHour = 9;
        int testAttendanceMinute = 20;
        int testLeavingHour = 17;
        int testLeavingMinute = 50;
        int testRestHour = 1;
        int testRestMinute = 10;
        //テスト対象のメソッドを実行してintを受け取る
        int result = CommonUtils.confirmWorkForm(testYear, testMonth, testDate, testWorkStatus, testAttendanceHour, testAttendanceMinute, testLeavingHour, testLeavingMinute, testRestHour, testRestMinute);
        //confirmWorkFormメソッドの期待する値
        int expected = 1;
        //confirmWorkFormメソッドの実行結果と期待値が同値であることを確認
        assertEquals(expected, result);
	}
	
	@Test
	@DisplayName("入力された出勤時間、退勤時間、休憩時間が時間軸として正しいかどうかを判断するメソッドのテスト2(フォームの入力値にnullがある場合)")
	void testConfirmWorkForm2() {
		//ダミーデータを宣言(confirmWorkFormメソッドの引数用)
		int testYear = 2023;
		int testMonth = 3;
		int testDate = 15;
		int testWorkStatus = 1;
		int testAttendanceHour = 9;
        int testAttendanceMinute = 20;
        int testLeavingHour = 17;
        int testLeavingMinute = 50;
        Integer testRestHour = null;
        int testRestMinute = 10;
        //テスト対象のメソッドを実行してintを受け取る
        int result = CommonUtils.confirmWorkForm(testYear, testMonth, testDate, testWorkStatus, testAttendanceHour, testAttendanceMinute, testLeavingHour, testLeavingMinute, testRestHour, testRestMinute);
        //confirmWorkFormメソッドの期待する値
        int expected = 2;
        //confirmWorkFormメソッドの実行結果と期待値が同値であることを確認
        assertEquals(expected, result);
	}
	
	@Test
	@DisplayName("入力された出勤時間、退勤時間、休憩時間が時間軸として正しいかどうかを判断するメソッドのテスト3(出勤時間が退勤時間よりも大きい場合)")
	void testConfirmWorkForm3() {
		//ダミーデータを宣言(confirmWorkFormメソッドの引数用)
		int testYear = 2023;
		int testMonth = 3;
		int testDate = 15;
		int testWorkStatus = 1;
		int testAttendanceHour = 11;
        int testAttendanceMinute = 20;
        int testLeavingHour = 9;
        int testLeavingMinute = 50;
        int testRestHour = 1;
        int testRestMinute = 10;
        //テスト対象のメソッドを実行してintを受け取る
        int result = CommonUtils.confirmWorkForm(testYear, testMonth, testDate, testWorkStatus, testAttendanceHour, testAttendanceMinute, testLeavingHour, testLeavingMinute, testRestHour, testRestMinute);
        //confirmWorkFormメソッドの期待する値
        int expected = 3;
        //confirmWorkFormメソッドの実行結果と期待値が同値であることを確認
        assertEquals(expected, result);
	}
	
	@Test
	@DisplayName("入力された出勤時間、退勤時間、休憩時間が時間軸として正しいかどうかを判断するメソッドのテスト4(休憩時間の値が就業時間の値よりも大きい場合)")
	void testConfirmWorkForm4() {
		//ダミーデータを宣言(confirmWorkFormメソッドの引数用)
		int testYear = 2023;
		int testMonth = 3;
		int testDate = 15;
		int testWorkStatus = 1;
		int testAttendanceHour = 15;
        int testAttendanceMinute = 20;
        int testLeavingHour = 17;
        int testLeavingMinute = 50;
        int testRestHour = 3;
        int testRestMinute = 30;
        //テスト対象のメソッドを実行してintを受け取る
        int result = CommonUtils.confirmWorkForm(testYear, testMonth, testDate, testWorkStatus, testAttendanceHour, testAttendanceMinute, testLeavingHour, testLeavingMinute, testRestHour, testRestMinute);
        //confirmWorkFormメソッドの期待する値
        int expected = 4;
        //confirmWorkFormメソッドの実行結果と期待値が同値であることを確認
        assertEquals(expected, result);
	}
	
	@Test
	@DisplayName("入力された出勤時間、退勤時間、休憩時間が時間軸として正しいかどうかを判断するメソッドのテスト5(出勤ステータスが出勤以外の時にフォームの値を入力)")
	void testConfirmWorkForm5() {
		//ダミーデータを宣言(confirmWorkFormメソッドの引数用)
		int testYear = 2023;
		int testMonth = 3;
		int testDate = 15;
		int testWorkStatus = 3;
		Integer testAttendanceHour = null;
        Integer testAttendanceMinute = null;
        Integer testLeavingHour = null;
        int testLeavingMinute = 50;
        Integer testRestHour = null;
        Integer testRestMinute = null;
        //テスト対象のメソッドを実行してintを受け取る
        int result = CommonUtils.confirmWorkForm(testYear, testMonth, testDate, testWorkStatus, testAttendanceHour, testAttendanceMinute, testLeavingHour, testLeavingMinute, testRestHour, testRestMinute);
        //confirmWorkFormメソッドの期待する値
        int expected = 5;
        //confirmWorkFormメソッドの実行結果と期待値が同値であることを確認
        assertEquals(expected, result);
	}
	
	@Test
	@DisplayName("入力された出勤時間、退勤時間、休憩時間が時間軸として正しいかどうかを判断するメソッドのテスト6(エラーなし(出勤))")
	void testConfirmWorkForm6() {
		//ダミーデータを宣言(confirmWorkFormメソッドの引数用)
		int testYear = 2023;
		int testMonth = 3;
		int testDate = 15;
		int testWorkStatus = 1;
		int testAttendanceHour = 9;
        int testAttendanceMinute = 20;
        int testLeavingHour = 17;
        int testLeavingMinute = 50;
        int testRestHour = 1;
        int testRestMinute = 10;
        //テスト対象のメソッドを実行してintを受け取る
        int result = CommonUtils.confirmWorkForm(testYear, testMonth, testDate, testWorkStatus, testAttendanceHour, testAttendanceMinute, testLeavingHour, testLeavingMinute, testRestHour, testRestMinute);
        //confirmWorkFormメソッドの期待する値
        int expected = 0;
        //confirmWorkFormメソッドの実行結果と期待値が同値であることを確認
        assertEquals(expected, result);
	}
	
	@Test
	@DisplayName("入力された出勤時間、退勤時間、休憩時間が時間軸として正しいかどうかを判断するメソッドのテスト7(エラーなし(有休))")
	void testConfirmWorkForm7() {
		//ダミーデータを宣言(confirmWorkFormメソッドの引数用)
		int testYear = 2023;
		int testMonth = 3;
		int testDate = 15;
		int testWorkStatus = 3;
		Integer testAttendanceHour = null;
        Integer testAttendanceMinute = null;
        Integer testLeavingHour = null;
        Integer testLeavingMinute = null;
        Integer testRestHour = null;
        Integer testRestMinute = null;
        //テスト対象のメソッドを実行してintを受け取る
        int result = CommonUtils.confirmWorkForm(testYear, testMonth, testDate, testWorkStatus, testAttendanceHour, testAttendanceMinute, testLeavingHour, testLeavingMinute, testRestHour, testRestMinute);
        //confirmWorkFormメソッドの期待する値
        int expected = 0;
        //confirmWorkFormメソッドの実行結果と期待値が同値であることを確認
        assertEquals(expected, result);
	}

	@Test
	@DisplayName("入力チェックエラー時にリダイレクトして値を保持するメソッド(出退勤申請画面)1(formRedirectがnullの場合)")
	void testFormRedirectRequestFormForm1() {
		//モックのRequestFormFormを生成
		RequestFormForm mockForm = new RequestFormForm();
		//ダミーデータを宣言(FormRedirectメソッドの引数用)
		int testWorkStatus = 1;
		int testAttendanceHour = 9;
        int testAttendanceMinute = 20;
        int testLeavingHour = 17;
        int testLeavingMinute = 50;
        int testRestHour = 1;
        int testRestMinute = 10;
        //モックのRequestFormForm(mockForm)にダミーデータのセット
        mockForm.setWorkStatus(testWorkStatus);
        mockForm.setAttendanceHour(testAttendanceHour);
        mockForm.setAttendanceMinute(testAttendanceMinute);
        mockForm.setLeavingHour(testLeavingHour);
        mockForm.setLeavingMinute(testLeavingMinute);
        mockForm.setRestHour(testRestHour);
        mockForm.setRestMinute(testRestMinute);
        //テスト対象のメソッドを実行する
	    CommonUtils.formRedirect(mockForm, null);
	    //FormRedirectメソッドの実行結果と期待値が同値であることを確認
	    assertEquals(1, mockForm.getWorkStatus());
	    assertEquals(9, mockForm.getAttendanceHour());
	    assertEquals(20, mockForm.getAttendanceMinute());
	    assertEquals(17, mockForm.getLeavingHour());
	    assertEquals(50, mockForm.getLeavingMinute());
	    assertEquals(1, mockForm.getRestHour());
	    assertEquals(10, mockForm.getRestMinute());
	}
	
	@Test
	@DisplayName("入力チェックエラー時にリダイレクトして値を保持するメソッド(出退勤申請画面)2(formRedirectがnotnullの場合)")
	void testFormRedirectRequestFormForm2() {
		//モックのRequestFormFormを生成
		RequestFormForm mockForm = new RequestFormForm();
		RequestFormForm mockFormRedirect = new RequestFormForm();
		//ダミーデータを宣言(FormRedirectメソッドの引数用)
		int testWorkStatus = 1;
		int testAttendanceHour = 9;
        int testAttendanceMinute = 20;
        int testLeavingHour = 17;
        int testLeavingMinute = 50;
        int testRestHour = 1;
        int testRestMinute = 10;
        //モックのRequestFormForm(mockFormRedirect)にダミーデータのセット
        mockFormRedirect.setWorkStatus(testWorkStatus);
        mockFormRedirect.setAttendanceHour(testAttendanceHour);
        mockFormRedirect.setAttendanceMinute(testAttendanceMinute);
        mockFormRedirect.setLeavingHour(testLeavingHour);
        mockFormRedirect.setLeavingMinute(testLeavingMinute);
        mockFormRedirect.setRestHour(testRestHour);
        mockFormRedirect.setRestMinute(testRestMinute);
        //テスト対象のメソッドを実行する
	    CommonUtils.formRedirect(mockForm, mockFormRedirect);
	    //FormRedirectメソッドの実行結果と期待値が同値であることを確認
	    assertEquals(1, mockForm.getWorkStatus());
	    assertEquals(9, mockForm.getAttendanceHour());
	    assertEquals(20, mockForm.getAttendanceMinute());
	    assertEquals(17, mockForm.getLeavingHour());
	    assertEquals(50, mockForm.getLeavingMinute());
	    assertEquals(1, mockForm.getRestHour());
	    assertEquals(10, mockForm.getRestMinute());
	}

	@Test
	@DisplayName("入力チェックエラー時にリダイレクトして値を保持するメソッド(出退勤修正画面)1(formRedirectがnullの場合)")
	void testFormRedirectWorkEditForm1() {
		//モックのWorkEditFormを生成
		WorkEditForm mockForm = new WorkEditForm();
		//ダミーデータを宣言(FormRedirectメソッドの引数用)
		int testWorkStatus = 1;
		int testAttendanceHour = 9;
        int testAttendanceMinute = 20;
        int testLeavingHour = 17;
        int testLeavingMinute = 50;
        int testRestHour = 1;
        int testRestMinute = 10;
        //モックのWorkEditForm(mockForm)にダミーデータのセット
        mockForm.setWorkStatus(testWorkStatus);
        mockForm.setAttendanceHour(testAttendanceHour);
        mockForm.setAttendanceMinute(testAttendanceMinute);
        mockForm.setLeavingHour(testLeavingHour);
        mockForm.setLeavingMinute(testLeavingMinute);
        mockForm.setRestHour(testRestHour);
        mockForm.setRestMinute(testRestMinute);
        //テスト対象のメソッドを実行する
	    CommonUtils.formRedirect(mockForm, null);
	    //FormRedirectメソッドの実行結果と期待値が同値であることを確認
	    assertEquals(1, mockForm.getWorkStatus());
	    assertEquals(9, mockForm.getAttendanceHour());
	    assertEquals(20, mockForm.getAttendanceMinute());
	    assertEquals(17, mockForm.getLeavingHour());
	    assertEquals(50, mockForm.getLeavingMinute());
	    assertEquals(1, mockForm.getRestHour());
	    assertEquals(10, mockForm.getRestMinute());
	}
	
	@Test
	@DisplayName("入力チェックエラー時にリダイレクトして値を保持するメソッド(出退勤修正画面)2(formRedirectがnotnullの場合)")
	void testFormRedirectWorkEditForm2() {
		//モックのWorkEditFormを生成
		WorkEditForm mockForm = new WorkEditForm();
		WorkEditForm mockFormRedirect = new WorkEditForm();
		//ダミーデータを宣言(FormRedirectメソッドの引数用)
		int testWorkStatus = 1;
		int testAttendanceHour = 9;
        int testAttendanceMinute = 20;
        int testLeavingHour = 17;
        int testLeavingMinute = 50;
        int testRestHour = 1;
        int testRestMinute = 10;
        //モックのWorkEditForm(mockFormRedirect)にダミーデータのセット
        mockFormRedirect.setWorkStatus(testWorkStatus);
        mockFormRedirect.setAttendanceHour(testAttendanceHour);
        mockFormRedirect.setAttendanceMinute(testAttendanceMinute);
        mockFormRedirect.setLeavingHour(testLeavingHour);
        mockFormRedirect.setLeavingMinute(testLeavingMinute);
        mockFormRedirect.setRestHour(testRestHour);
        mockFormRedirect.setRestMinute(testRestMinute);
        //テスト対象のメソッドを実行する
	    CommonUtils.formRedirect(mockForm, mockFormRedirect);
	    //FormRedirectメソッドの実行結果と期待値が同値であることを確認
	    assertEquals(1, mockForm.getWorkStatus());
	    assertEquals(9, mockForm.getAttendanceHour());
	    assertEquals(20, mockForm.getAttendanceMinute());
	    assertEquals(17, mockForm.getLeavingHour());
	    assertEquals(50, mockForm.getLeavingMinute());
	    assertEquals(1, mockForm.getRestHour());
	    assertEquals(10, mockForm.getRestMinute());
	}
	
	@Test
	@DisplayName("入力チェックエラー時にリダイレクトして値を保持するメソッド(出退勤修正画面)3(formRedirectがnotnullで、一部のフィールドがnullの場合)")
	void testFormRedirectWorkEditForm3() {
	    //モックのWorkEditFormを生成
	    WorkEditForm mockForm = new WorkEditForm();
	    WorkEditForm mockFormRedirect = new WorkEditForm();
	    //ダミーデータを宣言(FormRedirectメソッドの引数用)
	    Integer testWorkStatus = null;
	    Integer testAttendanceHour = null;
	    int testAttendanceMinute = 20;
	    int testLeavingHour = 17;
	    Integer testLeavingMinute = null;
	    int testRestHour = 1;
	    Integer testRestMinute = null;
	    //モックのWorkEditForm(mockFormRedirect)にダミーデータのセット
	    mockFormRedirect.setWorkStatus(testWorkStatus);
	    mockFormRedirect.setAttendanceHour(testAttendanceHour);
	    mockFormRedirect.setAttendanceMinute(testAttendanceMinute);
	    mockFormRedirect.setLeavingHour(testLeavingHour);
	    mockFormRedirect.setLeavingMinute(testLeavingMinute);
	    mockFormRedirect.setRestHour(testRestHour);
	    mockFormRedirect.setRestMinute(testRestMinute);
	    //テスト対象のメソッドを実行する
	    CommonUtils.formRedirect(mockForm, mockFormRedirect);
	    //FormRedirectメソッドの実行結果と期待値が同値であることを確認
	    assertNull(mockForm.getWorkStatus());
	    assertNull(mockForm.getAttendanceHour());
	    assertEquals(20, mockForm.getAttendanceMinute());
	    assertEquals(17, mockForm.getLeavingHour());
	    assertNull(mockForm.getLeavingMinute());
	    assertEquals(1, mockForm.getRestHour());
	    assertNull(mockForm.getRestMinute());
	}

}
