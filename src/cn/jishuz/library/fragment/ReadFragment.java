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
			username, strTime;// strTime��ǰϵͳʱ��
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
		tv1 = (TextView) view.findViewById(R.id.tab01_tv1); // ��ʾ ����ʱ��
		tv2 = (TextView) view.findViewById(R.id.tab01_tv2); // ��ʾ ����ʣ��
		sheng = (TextView) view.findViewById(R.id.tab01_sheng);
		btn = (Button) view.findViewById(R.id.tab01_btn);
		sayLL = (LinearLayout) view.findViewById(R.id.tab01_say);
		
		getData();
		setView();
		return view;
	}
	
	private void setView(){
		// ��ǰϵͳʱ��
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy��MM��dd�� HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());
		strTime = formatter.format(curDate);

		if (bookid != 0) {
			// ����ͼƬ
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
//			imageTab.setAlpha(220);// ����ͼƬ͸�� ȡֵ��ΧΪ0~255����ֵԽСԽ͸��  -----------------������ ��Ϊ�Ѿ���ʱ�� Ȼ������XML����������͸��

			bookName.setText("������" + name);
			authorTab.setText("���ߣ�" + author);
			publishTab.setText("�����磺" + publish);
			borTab.setText(LibraryTool.getDateToString(borrow_date));

			// ��ʼ����ʱ
			long total = Long.parseLong(dateline)*1000L - (System
					.currentTimeMillis());
			mc = new MyCountDownTimer(total, 1000L); // ��ʱ��,���ʱ��
			mc.start();

			// �����Ҫ���� �����¼�
			btn.setOnClickListener(new OnClickListener() {
				@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
				@SuppressLint("NewApi")
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					db = new Mydata(mContext);
					ContentValues values = new ContentValues();
					values.put("people", 1);
					values.put("title", "�����߻�������");
					values.put("content", username + "��" + strTime
							+ "ʱ�ύ�˻�������,�뾡�������Ӧ����");
					values.put("datetime", strTime);
					values.put("type", 1);
					db.insertSql("message", values);

					// ʹ��ϵͳ֪ͨ
					NotificationManager manager = (NotificationManager) getActivity()
							.getSystemService(mContext.NOTIFICATION_SERVICE);

					android.app.Notification.Builder builder = new Notification.Builder(
							getActivity());
					builder.setSmallIcon(R.drawable.logo);// ����ͼ��
					builder.setTicker("hello");// �ֻ�״̬������ʾ��
					builder.setWhen(System.currentTimeMillis());// ����ʱ��
					builder.setContentTitle("����֪ͨ");// ���ñ���
					builder.setContentText("��ȴ�����Աȷ��(������ǹ���Ա ���¼�˺Ž��в���)...");// ����֪ͨ����
					
					builder.setDefaults(Notification.DEFAULT_SOUND);// ������ʾ����
					builder.setDefaults(Notification.DEFAULT_VIBRATE);// ����ָʾ�� =
					Notification notification = builder.build();// 4.1����
					// builder.getNotification();
					manager.notify(1, notification);
					// Toast.makeText(mContext,
					// "��ȴ�����Աȷ������(������ǹ���Ա ���¼�˺Ž��в���)...",
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

	// �����ݿ��еõ�����
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
		
		//����bitmap Ԥ��OOM�쳣
		if (bitmap != null && !bitmap.isRecycled()) {
			// ���ղ�����Ϊnull
			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
	}

	/**
	 * �̳� CountDownTimer ����
	 * 
	 * ��д ����ķ��� onTick() �� onFinish()
	 */

	class MyCountDownTimer extends CountDownTimer {
		/**
		 * 
		 * @param millisInFuture
		 *            ��ʾ�Ժ���Ϊ��λ ����ʱ������
		 * 
		 *            ���� millisInFuture=1000 ��ʾ1��
		 * 
		 * @param countDownInterval
		 *            ��ʾ ��� ����΢�� ����һ�� onTick ����
		 * 
		 *            ����: countDownInterval =1000 ; ��ʾÿ1000�������һ��onTick()
		 * 
		 */
		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		public void onFinish() {
			sheng.setText("�����ڸû�����");
		}

		public void onTick(long millisUntilFinished) {
			sheng.setText(millisUntilFinished / 1000 + "s");
		}
	}
}
