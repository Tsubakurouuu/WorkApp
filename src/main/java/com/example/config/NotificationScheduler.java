package com.example.config;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.model.Notification;
import com.example.model.Work;
import com.example.service.NotificationService;
import com.example.service.WorkService;

@Component
public class NotificationScheduler {
	
	@Autowired
	private WorkService workService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Scheduled(cron = "0 59 23 * * ?") // 毎分実行
    public void checkAttendanceStatus() {
        // 退勤ボタンの押下状態を確認し、通知を送信する処理を実装する
        if (isAttendanceButtonNotPressed()) {
            sendNotificationToAdmin();
        }
    }

	private boolean isAttendanceButtonNotPressed() {
        // データベースやセッションからユーザーの退勤ボタンの状態を取得するロジックを実装する
        // 例えば、データベースの値をチェックするなど
        // このメソッドの返り値が true の場合、退勤ボタンが押されていないことを意味します
        // 必要に応じて実際のロジックを追加してください
		//カレンダークラスのオブジェクトを生成
		Calendar calendar = Calendar.getInstance();
		//現在の年を取得
        Integer year = calendar.get(Calendar.YEAR);
        //現在の月を取得
        Integer month = calendar.get(Calendar.MONTH) + 1;
        //現在の日を取得
        Integer date = calendar.get(Calendar.DATE);
        //勤怠情報を取得し不備がないかの確認
        List<Work> workList = workService.selectWorkInfoList(year, month, date);
        for(Work work : workList) {
        	if(work.getAttendanceHour() != null && work.getLeavingHour() == null) {
        		Notification notification = new Notification();
        		notification.setUserId(work.getUserId());
        		notification.setWorkId(work.getId());
        		notification.setMessage("退勤打刻が登録されていません。");
        		notificationService.insertNotification(notification);
        		return true;
        	}
        }
        return false;
    }

    private void sendNotificationToAdmin() {
        // 管理者に通知を送信する処理を実装する
        // 例えば、メール送信やプッシュ通知のAPIを呼び出すなど
        // 必要に応じて実際の通知処理を追加してください
        System.out.println("退勤ボタンが押されていません。管理者に通知します。");
    }
}
