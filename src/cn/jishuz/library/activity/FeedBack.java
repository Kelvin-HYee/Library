package cn.jishuz.library.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.jishuz.library.R;
import cn.jishuz.library.until.ActivityCollector;

public class FeedBack extends Activity {

	private Button test;
	private EditText edit;
	private EditText ed;
	
	private WindowManager mWindowManager;
	private View mNightView;

	private Handler mHandler = new Handler();
	
	Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            mHandler.postDelayed(this, 1000);
            if (!TextUtils.isEmpty(edit.getText().toString().trim())) {
				test.setEnabled(true);
			}else{
				test.setEnabled(false);
			}
        }
    };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.feedback);
		ActivityCollector.addActivity(this);
		
		SharedPreferences pf = getSharedPreferences("setting",MODE_PRIVATE);
		Boolean open = pf.getBoolean("open", false);
		if(open){
			night();
		}else{
			day();
		}
		
		test = (Button) findViewById(R.id.btn_test);
		edit = (EditText) findViewById(R.id.edit_test);
		ed = (EditText) findViewById(R.id.ed_test);
		
		test.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(FeedBack.this, "提交并不成功..没为什么", Toast.LENGTH_SHORT).show();
				edit.setText("");
				ed.setText("");
			}
		});
		mHandler.postDelayed(runnable, 1000);
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

	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}
