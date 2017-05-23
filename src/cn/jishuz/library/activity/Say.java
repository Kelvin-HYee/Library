package cn.jishuz.library.activity;

import cn.jishuz.library.R;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.until.ActivityCollector;
import cn.jishuz.library.until.Topbar;
import cn.jishuz.library.until.Topbar.onTitleBarClickListener;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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

public class Say extends Activity implements onTitleBarClickListener{

	private WindowManager mWindowManager;
	private View mNightView;
	
	private Topbar topbar;
	private EditText edit;
	private Button btn;
	
	private Mydata dbHelp;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.say);	
		ActivityCollector.addActivity(this);
		
		SharedPreferences pf = getSharedPreferences("setting",MODE_PRIVATE);
		Boolean open = pf.getBoolean("open", false);
		if(open){
			night();
		}else{
			day();
		}
		
		init();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		topbar = (Topbar) findViewById(R.id.say_topbar);
		topbar.setClickListener(this);
		
		edit = (EditText) findViewById(R.id.say_edit);
		btn = (Button) findViewById(R.id.say_btn);
		
		dbHelp = new Mydata(this);
		Cursor cursor = dbHelp.queryOne("user", "_id", new String[]{"1"});
		if(cursor.moveToFirst()){
			edit.setText(cursor.getString(cursor.getColumnIndex("say")));
		}
		cursor.close();
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String say = edit.getText().toString();
				SQLiteDatabase db = dbHelp.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("say", say);
				db.update("user", values, "_id = ?", new String[]{"1"});
				Toast.makeText(Say.this, "修改成功", Toast.LENGTH_SHORT).show();
			}
		});
		
		
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
		
	}

	public void onDestroy(){
		super.onDestroy();
		dbHelp.close();
		ActivityCollector.removeActivity(this);
	}
}
