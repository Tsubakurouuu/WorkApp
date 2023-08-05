package com.example.config;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.model.Notification;
import com.example.model.Work;
import com.example.service.impl.NotificationServiceImpl;
import com.example.service.impl.WorkServiceImpl;

@ExtendWith(MockitoExtension.class)
class NotificationSchedulerTest {
	
	@InjectMocks
    private NotificationScheduler notificationScheduler;
	
	@Mock
    private WorkServiceImpl workService;

    @Mock
    private NotificationServiceImpl notificationService;

	@Test
	@DisplayName("出勤打刻がされていて退勤打刻がされていないときに通知テーブルの作成をするメソッドのテスト")
	void testIsAttendanceButtonNotPressed() {
		//ダミーデータを宣言(selectWorkメソッドの引数用)
		Integer testAttendanceHour = 1;
		//カレンダークラスのオブジェクトを生成
		Calendar mockCalendar = Calendar.getInstance();
		//現在の年月日を取得
        Integer testYear = mockCalendar.get(Calendar.YEAR);
        Integer testMonth = mockCalendar.get(Calendar.MONTH) + 1;
        Integer testDate = mockCalendar.get(Calendar.DATE);
        //モックのWorkを生成
        Work mockWork = new Work();
		//モックのWorkにダミーデータのセット
		mockWork.setYear(testYear);
		mockWork.setMonth(testMonth);
		mockWork.setDate(testDate);
		mockWork.setAttendanceHour(testAttendanceHour);
		mockWork.setLeavingHour(null);
		//モックのWorkリストを生成
		List<Work> mockWorkList = new ArrayList<>();
		//Workリストが空でないことを保証
		mockWorkList.add(mockWork);
        //workServiceのselectWorkInfoListメソッド呼び出されたときにモックのWorkリストを返すように設定
		when(workService.selectWorkInfoList(testYear, testMonth, testDate)).thenReturn(mockWorkList);
		//テスト対象のメソッドを実行してbooleanを受け取る
		boolean result = notificationScheduler.isAttendanceButtonNotPressed();
		//trueが返ることを確認
        assertTrue(result);
        //notificationServiceのinsertNotificationが1度だけ呼び出されたことを確認
        verify(notificationService, times(1)).insertNotification(any(Notification.class));
	}

}
