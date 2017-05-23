package cn.jishuz.library.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import cn.jishuz.library.R;
import cn.jishuz.library.service.ReadService;
import cn.jishuz.library.until.ActivityCollector;
import cn.jishuz.library.until.LibraryTool;


/*
 * 返回键监听！！
 * 
 * */
public class AdminActivity extends Activity{ 
	
	private LinearLayout linear01;
	private LinearLayout linear02;
	private LinearLayout linear03;
	private LinearLayout linear04;
	private LinearLayout linear05;
	private LinearLayout linear06;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.admin);
		ActivityCollector.addActivity(this);
		
		initView();
		init();
		
		if(new LibraryTool().isServiceWork(this, "cn.jishuz.library.service.ReadService") == false){
			Intent i = new Intent(this, ReadService.class);
			startService(i);
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		linear01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AdminActivity.this,AdminMes.class);
				startActivity(i);
			}
		});
		
		linear02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AdminActivity.this,Admin02.class);
				startActivity(i);
			}
		});
		
		linear03.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AdminActivity.this,Admin03.class);
				startActivity(i);
			}
		});
		
		linear04.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AdminActivity.this,Admin04.class);
				startActivity(i);
			}
		});
		
		linear05.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new ActivityCollector().FinishAll();
				SharedPreferences.Editor edit = getSharedPreferences("login", MODE_PRIVATE).edit();
				edit.putString("username", "");
				edit.putString("password", "");
				edit.commit();
				Intent i = new Intent(AdminActivity.this,LoginActivity.class);
				startActivity(i);
			}
		});
		
		linear06.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
				builder.setIcon(R.drawable.me_author);
				builder.setTitle("提示");
				builder.setMessage("你确定要一键退出所有activity吗？");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						new ActivityCollector().FinishAll();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		linear01 = (LinearLayout) findViewById(R.id.linear01);
		linear02 = (LinearLayout) findViewById(R.id.linear02);
		linear03 = (LinearLayout) findViewById(R.id.linear03);
		linear04 = (LinearLayout) findViewById(R.id.linear04);
		linear05 = (LinearLayout) findViewById(R.id.linear05);
		linear06 = (LinearLayout) findViewById(R.id.linear06);
	}
	
	public void onDestroy(Activity activity){
		super.onDestroy();
		ActivityCollector.removeActivity(activity);
	}
	
}
