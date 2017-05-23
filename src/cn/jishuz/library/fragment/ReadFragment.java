package cn.jishuz.library.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jishuz.library.R;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.until.LibraryTool;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReadFragment extends Fragment {
	private Mydata db;
	private int bookid = 0;
	private String image, name, author, publish, dateline, borrow_date,
			username, strTime;// strTime当前系统时间
	private Context mContext;
	private MyCountDownTimer mc;
	private Bitmap bitmap;
	
	//view
	private ImageView imageTab;
	private TextView bookName;
	private TextView authorTab;
	private TextView publishTab;
	private TextView borTab;
	private LinearLayout NoData;
	private TextView tv1;
	private TextView tv2;
	private TextView sheng;
	private Button btn;
	private LinearLayout sayLL;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab01, container, false);
		imageTab = (ImageView) view.findViewById(R.id.tab01_image);
		bookName = (TextView) view.findViewById(R.id.tab01_bookName);
		authorTab = (TextView) view.findViewById(R.id.tab01_author);
		publishTab = (TextView) view.findViewById(R.id.tab01_publish);
		borTab = (TextView) view.findViewById(R.id.tab01_borrow_date);
		NoData = (LinearLayout) view.findViewById(R.id.tab01_noData);
		tv1 = (TextView) view.findViewById(R.id.tab01_tv1); // 提示 借书时间
		tv2 = (TextView) view.findViewById(R.id.tab01_tv2); // 提示 还书剩余
		sheng = (TextView) view.findViewById(R.id.tab01_sheng);
		btn = (Button) view.findViewById(R.id.tab01_btn);
		sayLL = (LinearLayout) view.findViewById(R.id.tab01_say);
		
		getData();
		setView();
		return view;
	}
	
	private void setView(){
		// 当前系统时间
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日 HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());
		strTime = formatter.format(curDate);

		if (bookid != 0) {
			// 设置图片
			AssetManager assets = getActivity().getAssets();
			InputStream is = null;
			try {
				is = assets.open("images/" + image + ".jpg");
			} catch (IOException e) {
				e.printStackTrace();
			}
			BitmapFactory.Options options = new BitmapFactory.Options();
			bitmap = BitmapFactory.decodeStream(is, null, options);
			imageTab.setImageBitmap(bitmap);
//			imageTab.setAlpha(220);// 设置图片透明 取值范围为0~255，数值越小越透明  -----------------不启用 因为已经过时了 然后我在XML里面有设置透明

			bookName.setText("书名：" + name);
			authorTab.setText("作者：" + author);
			publishTab.setText("出版社：" + publish);
			borTab.setText(LibraryTool.getDateToString(borrow_date));

			// 开始倒计时
			long total = Long.parseLong(dateline)*1000L - (System
					.currentTimeMillis());
			mc = new MyCountDownTimer(total, 1000L); // 总时间,间隔时间
			mc.start();

			// 点击我要还书 处理事件
			btn.setOnClickListener(new OnClickListener() {
				@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
				@SuppressLint("NewApi")
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					db = new Mydata(mContext);
					ContentValues values = new ContentValues();
					values.put("people", 1);
					values.put("title", "借阅者还书提醒");
					values.put("content", username + "在" + strTime
							+ "时提交了还书请求,请尽快完成相应操作");
					values.put("datetime", strTime);
					values.put("type", 1);
					db.insertSql("message", values);

					// 使用系统通知
					NotificationManager manager = (NotificationManager) getActivity()
							.getSystemService(mContext.NOTIFICATION_SERVICE);

					android.app.Notification.Builder builder = new Notification.Builder(
							getActivity());
					builder.setSmallIcon(R.drawable.logo);// 设置图标
					builder.setTicker("hello");// 手机状态栏的提示；
					builder.setWhen(System.currentTimeMillis());// 设置时间
					builder.setContentTitle("操作通知");// 设置标题
					builder.setContentText("请等待管理员确认(如果你是管理员 请登录账号进行操作)...");// 设置通知内容
					
					builder.setDefaults(Notification.DEFAULT_SOUND);// 设置提示声音
					builder.setDefaults(Notification.DEFAULT_VIBRATE);// 设置指示灯 =
					Notification notification = builder.build();// 4.1以上
					// builder.getNotification();
					manager.notify(1, notification);
					// Toast.makeText(mContext,
					// "请等待管理员确认收书(如果你是管理员 请登录账号进行操作)...",
					// Toast.LENGTH_LONG).show();
				}
			});
		} else {
			NoData.setBackgroundResource(R.drawable.no);
			imageTab.setVisibility(View.GONE);
			bookName.setVisibility(View.GONE);
			authorTab.setVisibility(View.GONE);
			publishTab.setVisibility(View.GONE);
			borTab.setVisibility(View.GONE);
			tv1.setVisibility(View.GONE);
			tv2.setVisibility(View.GONE);
			sheng.setVisibility(View.GONE);
			btn.setVisibility(View.GONE);
			sayLL.setVisibility(View.GONE);
		}
	}

	// 从数据库中得到数据
	private void getData() {
		db = new Mydata(mContext);
		Cursor cursor = db.queryAll("user");
		if (cursor.moveToFirst()) {
			bookid = cursor.getInt(cursor.getColumnIndex("bookid"));
			borrow_date = cursor
					.getString(cursor.getColumnIndex("borrow_date"));
			dateline = cursor.getString(cursor.getColumnIndex("dateline"));
			username = cursor.getString(cursor.getColumnIndex("username"));
		}
		if (bookid == 0)
			return;
		cursor.close();
		String book_id = String.valueOf(bookid);
		Cursor cursor1 = db.queryOne("book", "_id", new String[] { book_id });
		if (cursor1.moveToFirst()) {
			image = cursor1.getString(cursor1.getColumnIndex("Image"));
			name = cursor1.getString(cursor1.getColumnIndex("name"));
			author = cursor1.getString(cursor1.getColumnIndex("author"));
			publish = cursor1.getString(cursor1.getColumnIndex("publish"));
		}
		cursor1.close();
		db.close();
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
	}

	public void onDestroy() {
		super.onDestroy();
		
		//回收bitmap 预防OOM异常
		if (bitmap != null && !bitmap.isRecycled()) {
			// 回收并且置为null
			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
	}

	/**
	 * 继承 CountDownTimer 防范
	 * 
	 * 重写 父类的方法 onTick() 、 onFinish()
	 */

	class MyCountDownTimer extends CountDownTimer {
		/**
		 * 
		 * @param millisInFuture
		 *            表示以毫秒为单位 倒计时的总数
		 * 
		 *            例如 millisInFuture=1000 表示1秒
		 * 
		 * @param countDownInterval
		 *            表示 间隔 多少微秒 调用一次 onTick 方法
		 * 
		 *            例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
		 * 
		 */
		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		public void onFinish() {
			sheng.setText("你现在该还书了");
		}

		public void onTick(long millisUntilFinished) {
			sheng.setText(millisUntilFinished / 1000 + "s");
		}
	}
}
