package cn.jishuz.library.activity;

import cn.jishuz.library.R;
import cn.jishuz.library.fragment.Mes01;
import cn.jishuz.library.fragment.Mes02;
import cn.jishuz.library.until.ActivityCollector;
import cn.jishuz.library.until.Message;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;

public class Mes extends FragmentActivity{
	
	private WindowManager mWindowManager;
	private View mNightView;

	private List<View> mViews = new ArrayList<View>();
	private ArrayList<Message> mMes = new ArrayList<Message>();
	
	private LinearLayout tx; // 提醒
	private LinearLayout tz; // 通知
	private View view01;//下面那一条线
	private View view02;
	
	private Fragment mMes01;
	private Fragment mMes02;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message);
		ActivityCollector.addActivity(this);
		SharedPreferences pf = getSharedPreferences("setting",MODE_PRIVATE);
		Boolean open = pf.getBoolean("open", false);
		if(open){
			night();
		}else{
			day();
		}
//		mesAdapter = new MessageAdapter(this, mMes);
		initView();
		initEvents();
		setSelect(0);
	}
	
	private void initEvents() {
		// TODO Auto-generated method stub
		tx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setSelect(0);
			}
		});
		
		tz.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setSelect(1);
			}
		});
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		
		tx = (LinearLayout) findViewById(R.id.message_tx);
		tz = (LinearLayout) findViewById(R.id.message_tz);
		view01 = findViewById(R.id.view_tx);
		view02 = findViewById(R.id.view_tz);
		
	}

	private void setSelect(int i){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		hidden(transaction);
		switch (i) {
		case 0:
			mMes01 = new Mes01();
			transaction.add(R.id.mes_content, mMes01);
			view01.setVisibility(View.VISIBLE);
			view02.setVisibility(View.GONE);
			break;
		case 1:
			mMes02 = new Mes02();
			transaction.add(R.id.mes_content, mMes02);
			view01.setVisibility(View.GONE);
			view02.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		transaction.commit();
	}
	
	private void hidden(FragmentTransaction transaction) {
		// TODO Auto-generated method stub
		if(mMes01 != null){
			transaction.remove(mMes01);
		}
		
		if(mMes02 != null){
			transaction.remove(mMes02);
		}
	}

	private void night() {
		LayoutParams mNightViewParam = new LayoutParams(
				LayoutParams.TYPE_APPLICATION, LayoutParams.FLAG_NOT_TOUCHABLE
						| LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSPARENT);
		mWindowManager = (WindowManager) getSystemService(
				Context.WINDOW_SERVICE);
		if(mNightView == null){ //沒實例化
			mNightView = new View(this);
			mWindowManager.addView(mNightView, mNightViewParam);
			mNightView.setBackgroundResource(R.color.night_mask);
		}
	}

	private void day() {
		try {
			if(mNightView != null){
				mWindowManager.removeView(mNightView);
				mNightView = null;
			}
		} catch (Exception ex) {
		}
	}
	
	public void onDestroy(Activity activity){
		super.onDestroy();
		ActivityCollector.removeActivity(activity);
	}

}
