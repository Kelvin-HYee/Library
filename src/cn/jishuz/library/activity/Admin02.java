package cn.jishuz.library.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jishuz.library.R;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.until.ActivityCollector;

public class Admin02 extends Activity{
	
	private Mydata dbHelp;
	private ImageView img;
	private TextView name;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.admin_02);
		ActivityCollector.addActivity(this);
		img = (ImageView) findViewById(R.id.admin02_img);
		name = (TextView) findViewById(R.id.admin02_bookName); 
		setData();
	}
	
	private void setData() {
		// TODO Auto-generated method stub
		dbHelp = new Mydata(this);
		SQLiteDatabase db = dbHelp.getReadableDatabase();
		Cursor cursor = db.query("message", null, "people = ?", new String[]{"1"}, null, null, "_id desc");
		if(!cursor.moveToFirst()){//没有数据
			Toast.makeText(this, "暂时没有任何还书请求!即将返回...", Toast.LENGTH_LONG).show();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					finish();
				}
			}, 3000);
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("警告");
			builder.setIcon(android.R.drawable.ic_input_delete);
			builder.setMessage("你确定要帮他还掉此书吗？");
			builder.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SQLiteDatabase db2 = dbHelp.getReadableDatabase();
					ContentValues values = new ContentValues();
					values.put("bookid", "");
					values.put("borrow_date","");
					values.put("dateline","");
					db2.update("user", values, "_id = ?", new String[]{"1"});
					db2.delete("message", null, null);
					Toast.makeText(Admin02.this, "还书成功 3s后退出...", Toast.LENGTH_SHORT).show();
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							finish(); 
						}
					}, 3000);
				}
			});
			
			builder.setNegativeButton("取消", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			builder.show();
		}
		dbHelp.close();
	}
	
	public void onDestroy(Activity activity){
		super.onDestroy();
		ActivityCollector.removeActivity(activity);
	}

}
