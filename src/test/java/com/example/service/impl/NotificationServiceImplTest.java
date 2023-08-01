package com.example.service.impl;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.model.Notification;
import com.example.repository.NotificationMapper;
import com.example.service.NotificationService;

@SpringBootTest
class NotificationServiceImplTest {
	
	@MockBean
	private  NotificationMapper mapper;
	
	@Autowired
	private NotificationService notificationService;

	@Test
	@DisplayName("通知新規登録テスト")
	void testInsertNotification() {
		//モックのNotificationを生成
		Notification mockNotification = new Notification();
		//テスト対象のメソッドを実行してNotificationを登録する
		notificationService.insertNotification(mockNotification);
		//mapperのinsertNotificationが1度だけ呼び出されたことを確認
		verify(mapper, times(1)).insertNotification(mockNotification);
	}

	@Test
	@DisplayName("通知一覧取得テスト")
	void testSelectNotificationList() {
		//モックのNotificationリストを生成
		List<Notification> mockNotificationList = new ArrayList<>();
		//Notificationリストが空でないことを保証
        mockNotificationList.add(new Notification());
        //mapperのselectNotificationListメソッドが呼び出されたときにモックのNotificationリストを返すように設定
        when(mapper.selectNotificationList()).thenReturn(mockNotificationList);
        //テスト対象のメソッドを実行してNotificationのListを受け取る
        List<Notification> resultNotificationList = notificationService.selectNotificationList();
        //モックのNotificationリストが返されることを確認
        assertSame(mockNotificationList, resultNotificationList);
	}

	@Test
	@DisplayName("通知削除テスト")
	void testDeleteNotification() {
		//ダミーデータを宣言(deleteNotificationメソッドの引数用)
		Integer testId = 1;
		//テスト対象のメソッドを実行してNotificationを削除する
		notificationService.deleteNotification(testId);
		//mapperのdeleteNotificationが1度だけ呼び出されたことを確認
		verify(mapper, times(1)).deleteNotification(testId);
	}

}
