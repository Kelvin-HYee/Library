package cn.jishuz.library.activity;

import cn.jishuz.library.R;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.until.ActivityCollector;
import cn.jishuz.library.until.Topbar;
import cn.jishuz.library.until.Topbar.onTitleBarClickListener;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MeDetails extends Activity implements onTitleBarClickListener{

	private WindowManager mWindowManager;
	private View mNightView;
	private Mydata dbHelp;
	
	private EditText username;
	private EditText phone;
	private EditText sex;
	private EditText Password;
	private Topbar topbar;
	
	private String user;
	private String ph;
	private String se;
	private String psw;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.medetails);
		ActivityCollector.addActivity(this);
		
		SharedPreferences pf = getSharedPreferences("setting",MODE_PRIVATE);
		Boolean open = pf.getBoolean("open", false);
		if(open){
			night();
		}else{
			day();
		}
		
		initView();
		
		Button logout = (Button) findViewById(R.id.exit_login);
		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new ActivityCollector().FinishAll();
				SharedPreferences.Editor edit = getSharedPreferences("login", MODE_PRIVATE).edit();
				edit.putString("username", "");
				edit.putString("password", "");
				edit.commit();
				Intent i = new Intent(MeDetails.this,LoginActivity.class);
				startActivity(i);
			}
		});
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		username = (EditText) findViewById(R.id.medetails_username);
		username.setCursorVisible(false);
		phone = (EditText) findViewById(R.id.medetails_phone);
		sex = (EditText) findViewById(R.id.medetails_sex);
		Password = (EditText) findViewById(R.id.medetails_password);
		topbar = (Topbar) findViewById(R.id.me_topbar);
		topbar.setClickListener(this);
		
		dbHelp = new Mydata(this);
		Cursor cursor = dbHelp.queryOne("user", "_id", new String[]{"1"});
		if(cursor.moveToFirst()){
			user = cursor.getString(cursor.getColumnIndex("username"));
			ph = cursor.getString(cursor.getColumnIndex("phone"));
			se = cursor.getString(cursor.getColumnIndex("sex"));
			psw = cursor.getString(cursor.getColumnIndex("password"));
		}
		cursor.close();
		dbHelp.close();

		username.setText(user);
		phone.setText(ph);
		sex.setText(se);
		Password.setText(psw);
	}

	private void night() {
//		int isVisibel = mNightView.getVisibility();
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

	@Override
	public void onBackClick() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("bookResult", "3");
		startActivity(intent);
	}

	@Override
	public void onRightClick() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.edit);
		builder.setMessage("你确定要修改信息吗？");
		builder.setTitle("提示");
		dbHelp = new Mydata(this);
		final SQLiteDatabase db = dbHelp.getReadableDatabase();
		final ContentValues values = new ContentValues();
		values.put("username", username.getText().toString());
		values.put("phone", phone.getText().toString());
		values.put("password",Password.getText().toString());
		values.put("sex", sex.getText().toString());
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				db.update("user", values, "_id = ?", new String[]{"1"});
				db.close();
				dbHelp.close();
				if(!Password.getText().toString().equals(psw)){ //表示密码被修改
					Intent i = new Intent("cn.jishuz.FORCE_OFFLINE");
					sendBroadcast(i);
				}
				Toast.makeText(MeDetails.this, "修改成功", Toast.LENGTH_SHORT).show();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				db.close();
				dbHelp.close();
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	public void onDestroy(){
		super.onDestroy();
		ActivityCollector.removeActivity(this);
		
	}
}
