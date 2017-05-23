package cn.jishuz.library.activity;

import cn.jishuz.library.R;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.fragment.RegisterFragment;
import cn.jishuz.library.fragment.RegisterFragment.RegListener;
import cn.jishuz.library.service.ReadService;
import cn.jishuz.library.until.ActivityCollector;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.logging.MemoryHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements RegListener{

	private EditText username;
	private EditText password;
	private Button login;
	private Button forgive;
	private Button register;
	private String strName;
	private String strPwd;
	private String sqlName;
	private String sqlPwd;
	private Mydata dbHelp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		ActivityCollector.addActivity(this);

		String username = "hello";
		String password = "world";
		dbHelp = new Mydata(this);
		SQLiteDatabase db = dbHelp.getReadableDatabase();// 创建数据库
		Cursor cursor = db.query("user", null, "_id = ?", new String[] { "1" }, null, null, null);
		if (cursor.moveToFirst()) {
			username = cursor.getString(cursor.getColumnIndex("username"));
			password = cursor.getString(cursor.getColumnIndex("password"));
		}
		SharedPreferences pf = getSharedPreferences("login", MODE_PRIVATE);
		String pfName = pf.getString("username", "H");
		String pfPwd = pf.getString("password", "W");
		if(pfName.equals("admin") && pfPwd.equals("admin")){
			Intent i = new Intent(LoginActivity.this,AdminActivity.class);
			startActivity(i);
			finish();
		}else if(pfName.equals(username) && pfPwd.equals(password)){
			Intent i = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(i);
			finish();
		}
		handle();
		

		Intent i = new Intent(this, ReadService.class);
		startService(i);
	}

	private void handle() {
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);

		login = (Button) findViewById(R.id.login);
		forgive = (Button) findViewById(R.id.forgive_pwd);
		register = (Button) findViewById(R.id.register);

		dbHelp = new Mydata(this);
        Cursor cursor = dbHelp.queryOne("user", "_id", new String[] { "1" });
		if (cursor.moveToFirst()) {
			sqlName = cursor.getString(cursor.getColumnIndex("username"));
			sqlPwd = cursor.getString(cursor.getColumnIndex("password"));
		}
		cursor.close();
		dbHelp.close();

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strName = username.getText().toString();
				strPwd = password.getText().toString();
				if (strName.equals("") || strPwd.equals("")) {
					Toast.makeText(LoginActivity.this, "用户名或密码不能为空...", Toast.LENGTH_SHORT).show();
				} else {
					if(strName.equals("admin") && strPwd.equals("admin")){
						SharedPreferences.Editor edit = getSharedPreferences("login", MODE_PRIVATE).edit();
						edit.putString("username", strName);
						edit.putString("password", strPwd);
						edit.commit();

						startActivity(new Intent(LoginActivity.this, AdminActivity.class));
						overridePendingTransition(R.drawable.fade, R.drawable.hold);
						finish();
					}else if (strName.equals(sqlName) && strPwd.equals(sqlPwd)) {
						SharedPreferences.Editor edit = getSharedPreferences("login", MODE_PRIVATE).edit();
						edit.putString("username", strName);
						edit.putString("password", strPwd);
						edit.commit();

						startActivity(new Intent(LoginActivity.this, MainActivity.class));
						overridePendingTransition(R.drawable.fade, R.drawable.hold);
						finish();
					} else {
						username.setText("");
						password.setText("");
						Toast.makeText(LoginActivity.this, "用户名或密码错误...", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		forgive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
				dialogBuilder.setTitle("偷偷告诉你吧");
				dialogBuilder.setMessage("借阅者\n用户名:" + sqlName + "\n密码:" + sqlPwd + "\n———————\n管理员\nadmin---admin");
				AlertDialog alertDialog = dialogBuilder.create();
				alertDialog.show();
			}
		});
		
		register.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dbHelp = new Mydata(LoginActivity.this);
				Cursor cursor = dbHelp.queryOne("user", "_id", new String[] { "1" });
				String check = "1";
				if (cursor.moveToFirst()) {
					check = cursor.getString(cursor.getColumnIndex("username"));
				}
				if(check.equals("1")){
					RegisterFragment reg = new RegisterFragment();
					reg.show(getFragmentManager(), "Register Dialog");
				}else{
					Toast.makeText(LoginActivity.this, "你已经注册过了", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	@Override
	public void onLoginInputComplete(String username, String password) {
		// TODO Auto-generated method stub
		handle();
		Toast.makeText(this, "谢谢你注册！请记好你的用户名---"+username+"\n密码---"+password, Toast.LENGTH_LONG).show();
	}
}
