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

	private int bookid = 0;// bookidΪ0����û�н赽��
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void creatNTF() { // ��Ϊ������� 1 ���а�СʱҪ���� 2 ʱ�䵽�� Ҫ����
		// ��ǰϵͳʱ��
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss ");
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
			
			if(sendBook == 60*29){//ʱ�������Ϊ��λ ���������ǰ�Сʱ(����û����ִ��һ��) --- ��Ҳ��ῴ������ĿԴ���ѧ��ѧ���ǽ�����(��ʵ�ҵĴ��붼���Ǻ�ϰ��д̫��ע�͵� ������)
				ContentValues values = new ContentValues();
				values.put("people", "2");
				values.put("title", "ѽ,�����Ķ�ʱ���Ҫ������");
				values.put("content", "�㻹ʣ����Сʱ��Ҫ������,�뾡�쿴��ǰ��ͼ��ݻ���,лл����~");
				values.put("datetime", strTime);
				values.put("type", 2);
				dbHelp.insertSql("message", values);
				
				NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				Notification notification = new Notification(R.drawable.stat_notify_call_mute, "ͼ�����ϵͳ",
						System.currentTimeMillis());
				notification.setLatestEventInfo(this, "��̨����֪ͨ", "�㻹ʣ��Сʱ�͵ð��黹��ͼ�����Ŷ", null);
				notification.defaults = Notification.DEFAULT_ALL;
				manager.notify(1, notification);
			}else if(sendBook == 1){
				ContentValues values = new ContentValues();
				values.put("people", "2");
				values.put("title", "����Ҫ�����ؿڿ�,�������黹���˼�");
				values.put("content", "��Ľ���ʱ�䵽��,��ǰ��ͼ��ݻ����,лл����~");
				values.put("datetime", strTime);
				values.put("type", 2);
				dbHelp.insertSql("message", values);
				
				NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				Notification notification = new Notification(R.drawable.stat_notify_call_mute, "ͼ�����ϵͳ",
						System.currentTimeMillis());
				notification.setLatestEventInfo(this, "��̨����֪ͨ", "��Ľ���ʱ�䵽��,��ǰ��ͼ��ݻ����", null);
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
		int anM = 1 * 1000; // ����һ��ĺ�����
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
