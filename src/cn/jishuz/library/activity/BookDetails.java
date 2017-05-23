package cn.jishuz.library.activity;

import java.io.IOException;
import java.io.InputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.jishuz.library.R;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.service.ReadService;
import cn.jishuz.library.until.ActivityCollector;
import cn.jishuz.library.until.Topbar;
import cn.jishuz.library.until.Topbar.onTitleBarClickListener;

public class BookDetails extends Activity implements onTitleBarClickListener {

	private Mydata dbHelp = new Mydata(this);
	private String bookName;
	private String bookAuthor;
	private String bookPublish;
	private String bookScore;
	private String ImageName;
	private String mController;

	// 控件
	private ImageView image;
	private TextView title;
	private RatingBar ratingBar;
	private TextView score;
	private TextView publish;
	private TextView author;
	// private Button borrow;
	private Spinner spinner;
	private int id; // 书id
	private String type;

	private Bitmap bitmap;

	private Topbar topbar;

	private int checkId = 0;
	private WindowManager mWindowManager;
	private View mNightView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bookdetails);
		ActivityCollector.addActivity(this);
		topbar = (Topbar) findViewById(R.id.topbarView);
		topbar.setClickListener(this);

		id = getIntent().getIntExtra("id", 0);
		type = getIntent().getStringExtra("type");
		if (id != 0) {
			Cursor cursor = dbHelp.queryOne("book", "_id", new String[] { String.valueOf(id) });
			if (cursor.moveToFirst()) {
				bookName = cursor.getString(cursor.getColumnIndex("name"));
				bookAuthor = cursor.getString(cursor.getColumnIndex("author"));
				bookPublish = cursor.getString(cursor.getColumnIndex("publish"));
				bookScore = cursor.getString(cursor.getColumnIndex("score"));
				ImageName = cursor.getString(cursor.getColumnIndex("Image"));
			}
			cursor.close();
			dbHelp.close();
			initView();
		} else {
			Toast.makeText(this, "页面错误", Toast.LENGTH_LONG).show();
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
		}
		// Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

		SharedPreferences pf = getSharedPreferences("setting",MODE_PRIVATE);
		Boolean open = pf.getBoolean("open", false);
		if(open){
			night();
		}else{
			day();
		}
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
	
	private void initView() {
		image = (ImageView) findViewById(R.id.bookdetails_image);
		title = (TextView) findViewById(R.id.bookdetails_title);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
		score = (TextView) findViewById(R.id.bookdetails_score);
		publish = (TextView) findViewById(R.id.bookdetails_publish);
		author = (TextView) findViewById(R.id.bookdetails_author);
		// borrow = (Button) findViewById(R.id.details_borrow_btn);
		spinner = (Spinner) findViewById(R.id.spinner);

		AssetManager assets = getAssets();
		InputStream is = null;
		try {
			is = assets.open("images/" + ImageName + ".jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		bitmap = BitmapFactory.decodeStream(is, null, options);
		image.setImageBitmap(bitmap);

		title.setText(bookName);
		score.setText("      " + bookScore + "分");
		ratingBar.setRating(Float.parseFloat(bookScore));
		publish.setText(bookPublish);
		author.setText("作者:" + bookAuthor);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
				new String[] { "3分钟(测试)", "15分钟(测试)", "31分钟(测试)", "1小时", "2小时" });
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				mController = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});

		findViewById(R.id.basket_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Cursor cursor = dbHelp.queryOne("basket","bookid",new String[]{String.valueOf(id)});
				int check = 0;
				if(cursor.moveToFirst()){
					check = cursor.getInt(cursor.getColumnIndex("_id"));
				}
				
				if(check == 0){ //這種情況就代表數據庫裏沒有這本書
					ContentValues values = new ContentValues();
					values.put("userid", 1);
					values.put("bookid", id);
					dbHelp.insertSql("basket", values);
					Toast.makeText(BookDetails.this, "加入成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(v.getContext(), "這本書已經添加過...", Toast.LENGTH_SHORT).show();
				}
			}
		});

		SQLiteDatabase db = dbHelp.getReadableDatabase();
		Cursor cursor = db.query("user", null, "_id = ?", new String[] { "1" }, null, null, null);
		if (cursor.getColumnIndex("bookid") != -1) {
			if(cursor.moveToFirst()){
				checkId = cursor.getInt(cursor.getColumnIndex("bookid"));
			}
		}
		cursor.close();

		findViewById(R.id.details_borrow_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				long time = System.currentTimeMillis() / 1000L;
				long spinner01 = time + 180L;
				long spinner02 = time + 900L;
				long spinner03 = time + 1860L;
				long spinner04 = time + 3600L;
				long spinner05 = time + 7200L;
				
				
//				Log.i("test01", "" + time + "");
//				Log.i("test02", "" + spinner01 + "");
				Log.i("BookDetails.class",checkId+"");

				if(checkId != 0){
					Toast.makeText(BookDetails.this, "《" + bookName + "》无法被借取,请先把书还了,或者加入书篮,等以后再借", Toast.LENGTH_LONG)
							.show();
				}else{
					ContentValues values = new ContentValues();
					values.put("bookid", id);
					values.put("borrow_date", Long.toString(time));
					
					AlertDialog.Builder builder = new AlertDialog.Builder(BookDetails.this);
					builder.setTitle("提示");
					builder.setMessage("恭喜你 成功借到书《" + bookName + "》");
					builder.setIcon(R.drawable.ic_launcher);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});

					if (mController.equals("3分钟(测试)")) {
						values.put("dateline", Long.toString(spinner01));
						dbHelp.updateSql("user", values, new String[] { "1" });
						builder.create().show();
						dbHelp.close();
					}

					if (mController.equals("15分钟(测试)")) {
						values.put("dateline", Long.toString(spinner02));
						dbHelp.updateSql("user", values, new String[] { "1" });
						builder.create().show();
						dbHelp.close();
					}

					if (mController.equals("31分钟(测试)")) {
						values.put("dateline", Long.toString(spinner03));
						dbHelp.updateSql("user", values, new String[] { "1" });
						builder.create().show();
						dbHelp.close();
					}

					if (mController.equals("1小时")) {
						values.put("dateline", String.valueOf(spinner04));
						dbHelp.updateSql("user", values, new String[] { "1" });
						builder.create().show();
						dbHelp.close();
					}

					if (mController.equals("2小时")) {
						values.put("dateline", Long.toString(spinner05));
						dbHelp.updateSql("user", values, new String[] { "1" });
						builder.create().show();
						dbHelp.close();
					}
				}
			}
		});
	}

	protected void onDestroy(Activity activity) {
		super.onDestroy();
		ActivityCollector.removeActivity(this);

		// 回收bitmap 预防OOM异常
		/*if (bitmap != null && !bitmap.isRecycled()) {
			// 回收并且置为null
			bitmap.recycle();
			bitmap = null;
		}*/
//		System.gc();
	}

	@Override
	public void onBackClick() {
		// TODO Auto-generated method stub
		if(type.equals("basket")){
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("bookResult", "2");
			startActivity(intent);
		}else{
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("bookResult", "1");
			startActivity(intent);
		}
	}

	@Override
	public void onRightClick() {
		// TODO Auto-generated method stub

	}
}
