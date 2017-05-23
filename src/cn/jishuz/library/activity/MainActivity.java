package cn.jishuz.library.activity;

import cn.jishuz.library.R;
import cn.jishuz.library.db.InitData;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.fragment.BasketFragment;
import cn.jishuz.library.fragment.BookFragment;
import cn.jishuz.library.fragment.MeFragment;
import cn.jishuz.library.fragment.MeFragment.OpenListener;
import cn.jishuz.library.fragment.ReadFragment;
import cn.jishuz.library.service.ReadService;
import cn.jishuz.library.until.ActivityCollector;
import cn.jishuz.library.until.LibraryTool;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener,OpenListener{

	// layout
	private LinearLayout read_layout;
	private LinearLayout bookshelves_layout;
	private LinearLayout basket_layout;
	private LinearLayout me_layout;

	// ImageButton
	private ImageButton read_btn;
	private ImageButton bookshelves_btn;
	private ImageButton basket_btn;
	private ImageButton me_btn;

	// TextView
	private TextView read_tv;
	private TextView bookshelves_tv;
	private TextView basket_tv;
	private TextView me_tv;

	private Fragment mTab01;
	private Fragment mTab02;
	private Fragment mTab03;
	private Fragment mTab04;

	private Mydata data;
	private int bookid = 0;// bookid为0代表没有借到书
	private String bookDetails;

	private WindowManager mWindowManager;
	private View mNightView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		ActivityCollector.addActivity(this);
		initView();
		initEvents();
		bookDetails = getIntent().getStringExtra("bookResult");
		if (bookDetails == null || bookDetails.length() <= 0) {
			setSelect(0);
		} else if (bookDetails.equals("1")) {
			setSelect(1);
		} else if (bookDetails.equals("2")) {
			setSelect(2);
		} else if(bookDetails.equals("3")){
			setSelect(3);
		}
		data = new Mydata(this);
		SQLiteDatabase db = data.getReadableDatabase();// 创建数据库
		Cursor cursor = db.query("book", null, null, null, null, null, null);
		if (cursor.getCount() == 0) {
			InitData initData = new InitData(this);
			initData.init(); // 初始化数据库表
		}
		cursor.close();
		// 借到书的情况下 执行后台服务 定时查看还书时间
		/*Cursor cursor1 = data.queryAll("user");
		if (cursor1.moveToFirst()) {
			bookid = cursor1.getInt(cursor1.getColumnIndex("bookid"));
		}
		if (bookid != 0) {
			Intent i = new Intent(this, ReadService.class);
			startService(i);
		}*/
		data.close();

		SharedPreferences pf = getSharedPreferences("setting",MODE_PRIVATE);
		Boolean open = pf.getBoolean("open", false);
		if(open){
			night();
		}else{
			day();
		}
		
		if(new LibraryTool().isServiceWork(this, "cn.jishuz.library.service.ReadService") == false){
			Intent i = new Intent(this, ReadService.class);
			startService(i);
		}
	}

	private void initEvents() {
		read_layout.setOnClickListener(this);
		bookshelves_layout.setOnClickListener(this);
		basket_layout.setOnClickListener(this);
		me_layout.setOnClickListener(this);
	}
	
	private void initView() {
		// TODO Auto-generated method stub

		// layout
		read_layout = (LinearLayout) findViewById(R.id.read_Layout);
		bookshelves_layout = (LinearLayout) findViewById(R.id.bookshelves_layout);
		basket_layout = (LinearLayout) findViewById(R.id.basket_layout);
		me_layout = (LinearLayout) findViewById(R.id.me_layout);

		// ImageButton
		read_btn = (ImageButton) findViewById(R.id.read_btn);
		bookshelves_btn = (ImageButton) findViewById(R.id.bookshelves_btn);
		basket_btn = (ImageButton) findViewById(R.id.basket_btn);
		me_btn = (ImageButton) findViewById(R.id.me_btn);

		// TextView
		read_tv = (TextView) findViewById(R.id.read_tv);
		bookshelves_tv = (TextView) findViewById(R.id.bookshelves_tv);
		basket_tv = (TextView) findViewById(R.id.basket_tv);
		me_tv = (TextView) findViewById(R.id.me_tv);
	}

	private void setSelect(int i) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();// 开始事务
		hiddenFragment(transaction);
		switch (i) {
		case 0:
			/*
			 * if(mTab01 == null){ mTab01 = new ReadFragment();
			 * transaction.add(R.id.content,mTab01); }else{
			 * transaction.show(mTab01); }
			 */
			mTab01 = new ReadFragment();
			transaction.add(R.id.content, mTab01);
			read_btn.setImageResource(R.drawable.read_press);
			read_tv.setTextColor(Color.RED);
			break;
		case 1:
			/*
			 * if(mTab02 == null){ mTab02 = new BookFragment();
			 * transaction.add(R.id.content, mTab02); }else{
			 * transaction.show(mTab02); }
			 */
			mTab02 = new BookFragment();
			transaction.add(R.id.content, mTab02);
			bookshelves_btn.setImageResource(R.drawable.bookshelves_press);
			bookshelves_tv.setTextColor(Color.RED);
			break;
		case 2:
			/*
			 * if(mTab03 == null){ mTab03 = new BasketFragment();
			 * transaction.add(R.id.content, mTab03); }else{
			 * transaction.show(mTab03); }
			 */
			mTab03 = new BasketFragment();
			transaction.add(R.id.content, mTab03);
			basket_btn.setImageResource(R.drawable.basket_press);
			basket_tv.setTextColor(Color.RED);
			break;
		case 3:
			/*
			 * if(mTab04 == null){ mTab04 = new MeFragment();
			 * transaction.add(R.id.content,mTab04); }else{
			 * transaction.show(mTab04); }
			 */
			mTab04 = new MeFragment();
			transaction.add(R.id.content, mTab04);
			me_btn.setImageResource(R.drawable.me_press);
			me_tv.setTextColor(Color.RED);
			break;
		default:
			break;
		}
		transaction.commit();
	}

	private void hiddenFragment(FragmentTransaction transaction) {
		// TODO Auto-generated method stub
		if (mTab01 != null) {
			// transaction.hide(mTab01);
			transaction.remove(mTab01);
		}

		if (mTab02 != null) {
			transaction.remove(mTab02);
		}

		if (mTab03 != null) {
			transaction.remove(mTab03);
		}

		if (mTab04 != null) {
			transaction.remove(mTab04);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		resetMenu();
		switch (v.getId()) {
		case R.id.read_Layout:
			setSelect(0);
			break;
		case R.id.bookshelves_layout:
			setSelect(1);
			break;
		case R.id.basket_layout:
			setSelect(2);
			break;
		case R.id.me_layout:
			setSelect(3);
			break;
		default:
			break;
		}
	}

	public void resetMenu() {
		read_btn.setImageResource(R.drawable.read_normal);
		bookshelves_btn.setImageResource(R.drawable.bookshelves_normal);
		basket_btn.setImageResource(R.drawable.basket_normal);
		me_btn.setImageResource(R.drawable.me_normal);

		read_tv.setTextColor(this.getResources().getColor(R.color.gray));
		bookshelves_tv.setTextColor(this.getResources().getColor(R.color.gray));
		basket_tv.setTextColor(this.getResources().getColor(R.color.gray));
		me_tv.setTextColor(this.getResources().getColor(R.color.gray));
	}

	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
		day();
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
	public void onOpenComplete(Boolean open) {
		// TODO Auto-generated method stub
		if(open){
			night();
		}else{
			day();
		}
	}
}
