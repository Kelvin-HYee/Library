package cn.jishuz.library.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.jishuz.library.R;
import cn.jishuz.library.db.InitData;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.until.ActivityCollector;

public class Admin03 extends Activity{

	private EditText name;
	private EditText author;
	private EditText publish;
	private EditText score;
	private Button submit;
	private Mydata dbHelp = new Mydata(this);;
	
	private String book_name;
	private String book_author;
	private String book_publish;
	private String book_score;
	private float s;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.admin_book);
		ActivityCollector.addActivity(this);
		
		initView();
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				book_name = name.getText().toString();
				book_author = author.getText().toString();
				book_publish = publish.getText().toString();
				book_score = score.getText().toString();
				
				try {
					if("".equals(book_score)){
						s = 0;
					}else{
						s = Float.valueOf(book_score);
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(s>0f && s<=10f){
					SQLiteDatabase db = dbHelp.getReadableDatabase();// 创建数据库
					Cursor cursor = db.query("book", null, null, null, null, null, null);
					if (cursor.getCount() == 0) {
						Toast.makeText(Admin03.this, "现在无法添加书籍 请先注册用户...", Toast.LENGTH_SHORT).show();
					}else{
						ContentValues values = new ContentValues();
						values.put("Image", "add");
						values.put("name",book_name);
						values.put("author",book_author);
						values.put("publish",book_publish);
						values.put("score", s);
						dbHelp.insertSql("book", values);
						Toast.makeText(Admin03.this, "新增成功", Toast.LENGTH_LONG).show();
						Intent i = new Intent(Admin03.this,AdminActivity.class);
						startActivity(i);
					}
				}else if(s == 0f && book_name.equals("")){
					Toast.makeText(Admin03.this, "發生錯誤...", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(Admin03.this, "评分只能在0~10之间...", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		name = (EditText) findViewById(R.id.book_name_edit);
		author = (EditText) findViewById(R.id.book_author_edit);
		publish = (EditText) findViewById(R.id.book_publish_edit);
		score = (EditText) findViewById(R.id.book_score_edit);
		submit = (Button) findViewById(R.id.admin_book_submit);
	}
	
	public void onDestroy(Activity activity){
		super.onDestroy();
		dbHelp.close();
		ActivityCollector.removeActivity(this);
	}
}
