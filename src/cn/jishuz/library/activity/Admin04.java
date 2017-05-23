package cn.jishuz.library.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import cn.jishuz.library.R;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.until.ActivityCollector;
import cn.jishuz.library.until.LibraryTool;

public class Admin04 extends Activity {

	private TextView username;
	private TextView phone;
	private TextView sex;
	private TextView say;
	private TextView bookName;
	private TextView borrow_date;
	private TextView dateline;
	private Mydata dbHelp;

	private String name;
	private String ph;
	private String se;
	private String sa;
	private String bName;
	private String bor_date;
	private String dline;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.admin_user);
		ActivityCollector.addActivity(this);
		initView();
		setData();
	}

	private void setData() {
		// TODO Auto-generated method stub
		dbHelp = new Mydata(this);
		SQLiteDatabase db = dbHelp.getReadableDatabase();
		Cursor cursor = db.query("user", null, "_id = ?", new String[] { "1" }, null, null, null);
		if (cursor.moveToFirst()) {
			name = cursor.getString(cursor.getColumnIndex("username"));
			ph = cursor.getString(cursor.getColumnIndex("phone"));
			se = cursor.getString(cursor.getColumnIndex("sex"));
			sa = cursor.getString(cursor.getColumnIndex("say"));
			bor_date = cursor.getString(cursor.getColumnIndex("borrow_date"));
			dline = cursor.getString(cursor.getColumnIndex("dateline"));
			int bookid = cursor.getInt(cursor.getColumnIndex("bookid"));
			Cursor cu = db.query("book", null, "_id = ?", new String[] { String.valueOf(bookid) }, null, null, null);
			if (cu.moveToFirst()) {
				bName = cu.getString(cu.getColumnIndex("name"));
				bookName.setText(bName);
			}
			username.setText(name);
			phone.setText(ph);
			sex.setText(se);
			say.setText(sa);
			borrow_date.setText(LibraryTool.getDateToString(bor_date));
			dateline.setText(LibraryTool.getDateToString(dline));
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		username = (TextView) findViewById(R.id.admin04_username);
		phone = (TextView) findViewById(R.id.admin04_phone);
		sex = (TextView) findViewById(R.id.admin04_sex);
		say = (TextView) findViewById(R.id.admin04_say);
		bookName = (TextView) findViewById(R.id.admin04_bookName);
		borrow_date = (TextView) findViewById(R.id.admin04_borrow_date);
		dateline = (TextView) findViewById(R.id.admin04_dateline);
	}
	
	public void onDestroy(){
		super.onDestroy();
		ActivityCollector.removeActivity(this);
		
	}

}
