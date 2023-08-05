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
	
	//★毎日23時59分0秒に起動するメソッド
	@Scheduled(cron = "0 59 23 * * ?")
    public void checkAttendanceStatus() {
		//出勤打刻がされていて退勤打刻がされていないときに通知テーブルの作成をするメソッド
		isAttendanceButtonNotPressed();
    }
	
	//出勤打刻がされていて退勤打刻がされていないときに通知テーブルの作成をするメソッド
	public boolean isAttendanceButtonNotPressed() {
		//カレンダークラスのオブジェクトを生成
		Calendar calendar = Calendar.getInstance();
		//現在の年を取得
        Integer year = calendar.get(Calendar.YEAR);
        //現在の月を取得
        Integer month = calendar.get(Calendar.MONTH) + 1;
        //現在の日を取得
        Integer date = calendar.get(Calendar.DATE);
        //勤怠情報を取得し不備がないかの確認(今日日付の勤怠リストを取得)
        List<Work> workList = workService.selectWorkInfoList(year, month, date);
        //取得したデータで繰り返し分
        for(Work work : workList) {
        	//出勤時間が登録されていて退勤時間が登録されていなければ(退勤打刻の押しわすれ)処理を実行
        	if(work.getAttendanceHour() != null && work.getLeavingHour() == null) {
        		//Notificationインスタンスの生成
        		Notification notification = new Notification();
        		notification.setUserId(work.getUserId());
        		notification.setWorkId(work.getId());
        		notification.setMessage("退勤打刻が登録されていません。");
        		//通知新規登録
        		notificationService.insertNotification(notification);
        		//trueを返し、処理が実行される
        		return true;
        	}
        }
        //取得したデータが全て問題いなければfalseをかえし処理は実行されない
        return false;
    }
	
}
