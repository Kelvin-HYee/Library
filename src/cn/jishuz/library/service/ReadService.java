package cn.jishuz.library.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.R;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.receiver.ReadReceiver;

public class ReadService extends Service {

	private String strTime;
	private Mydata dbHelp = new Mydata(this);
	private String dateline;

	private int bookid = 0;// bookid为0代表没有借到书
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void creatNTF() { // 分为两种情况 1 还有半小时要还书 2 时间到了 要还书
		// 当前系统时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());
		strTime = formatter.format(curDate);
		
		Cursor cursor = dbHelp.queryAll("user");
		if(cursor.moveToFirst()){
			dateline = cursor.getString(cursor.getColumnIndex("dateline"));
			bookid = cursor.getInt(cursor.getColumnIndex("bookid"));
		}
		if (bookid != 0) {
			int sendBook = (int) (Long.valueOf(dateline) - (System.currentTimeMillis()/1000L));
//			Log.i("Service", sendBook+"");
			
			if(sendBook == 60*29){//时间戳是秒为单位 所以这里是半小时(服务没分钟执行一次) --- 给也许会看到我项目源码的学弟学妹们解释下(其实我的代码都不是很习惯写太多注释的 哈哈哈)
				ContentValues values = new ContentValues();
				values.put("people", "2");
				values.put("title", "呀,愉快的阅读时光就要结束了");
				values.put("content", "你还剩余半个小时就要还书了,请尽快看完前往图书馆还书,谢谢合作~");
				values.put("datetime", strTime);
				values.put("type", 2);
				dbHelp.insertSql("message", values);
				
				NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				Notification notification = new Notification(R.drawable.stat_notify_call_mute, "图书管理系统",
						System.currentTimeMillis());
				notification.setLatestEventInfo(this, "后台服务通知", "你还剩半小时就得把书还回图书馆了哦", null);
				notification.defaults = Notification.DEFAULT_ALL;
				manager.notify(1, notification);
			}else if(sendBook == 1){
				ContentValues values = new ContentValues();
				values.put("people", "2");
				values.put("title", "怕是要捶你胸口咯,还不把书还给人家");
				values.put("content", "你的借阅时间到了,请前往图书馆还书吧,谢谢合作~");
				values.put("datetime", strTime);
				values.put("type", 2);
				dbHelp.insertSql("message", values);
				
				NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				Notification notification = new Notification(R.drawable.stat_notify_call_mute, "图书管理系统",
						System.currentTimeMillis());
				notification.setLatestEventInfo(this, "后台服务通知", "你的借阅时间到了,请前往图书馆还书吧", null);
				notification.defaults = Notification.DEFAULT_ALL;
				manager.notify(1, notification);
			}
		}
		
		dbHelp.close();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				Log.i("test","Service exec");
				creatNTF();
			}
		}).start();

		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anM = 1 * 1000; // 这是一秒的毫秒数
		long triggerAtTime = SystemClock.elapsedRealtime() + anM;
		Intent i = new Intent(this, ReadReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

		return super.onStartCommand(intent, flags, startId);
	}
	
	public void onDestroy() {
		super.onDestroy();
	}
}
