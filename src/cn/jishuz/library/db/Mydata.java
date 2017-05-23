package cn.jishuz.library.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Mydata extends SQLiteOpenHelper{

	// say个性签名  bookid借的书的id    borrow_date借书时间戳          dateline过期时间 (一次只能借一本书)   只能一个借阅者
	private String CREATE_USER ="create table user(" +
			"_id integer primary key autoincrement," +
			"username VARCHAR(50) NOT NULL," +
			"password VARCHAR(20) NOT NULL," +
			"sex VARCHAR(2)," +
			"phone text," +
			"say text," +
			"bookid integer,"+
			"borrow_date text," +
			"dateline text)";
	
	private String CREATE_BASKET = "create table basket(" +
			"_id integer primary key autoincrement," +
			"userid integer," +
			"bookid integer)";
	
	private String CREATE_ADMIN = "create table admin(" +
			"_id integer primary key autoincrement," +
			"username VARCHAR(50) NOT NULL," +
			"password VARCHAR(20) NOT NULL)";
	//publish 出版社              score评分   
	private String CREATE_BOOK = "create table book(" +
			"_id integer primary key autoincrement," +
			"Image text NOT NULL," +
			"name VARCHAR(100)," +
			"author VARCHAR(100)," +
			"publish CARCHAR(100)," +
			"score REAL)";
	
	//type 为类型 1为提醒 2为系统通知  people(接收者) 1为管理员 2为用户
	private String CREATE_MESSAGE = "create table message(" +
			"_id integer primary key autoincrement," +
			"people integer,"+
			"title VARCHAR(30) NOT NULL," +
			"content VARCHAR(300) NOT NULL," +
			"datetime text," +
			"type integer)";
	
	private Context mContext;
	private static String name = "library.db";
	private static int version = 1;
	
	public Mydata(Context context) {
		super(context, name, null, version);
		mContext = context;
	}
	
	//一条数据
	public Cursor queryOne(String table,String columns,String[] s){
		SQLiteDatabase db = getReadableDatabase();
		Cursor corsor;
        synchronized (db) {
    		//db.query("book", null, "id = ?", new String[]{"2"}, null, null, null);
    		corsor = db.query(table,null,columns+" = ?", s, null, null, null);
        }
		return corsor;
	}
	
	//查询表中所有数据
	public Cursor queryAll(String table){
		SQLiteDatabase db = getReadableDatabase();
		Cursor corsor;
        synchronized (db) {
    		//db.query("book", null, "id = ?", new String[]{"2"}, null, null, null);
    		corsor = db.query(table,null,null, null, null, null, null);
        }
		return corsor;
	}
	
	//插入数据
	public void insertSql(String table,ContentValues values){
		SQLiteDatabase db = getWritableDatabase();
		db.insert(table,null, values);
	}
	
	//修改数据---
	public void updateSql(String table,ContentValues values,String[] where){
		SQLiteDatabase db = getWritableDatabase();
		db.update(table, values, "_id = ?", where);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_USER);
		db.execSQL(CREATE_ADMIN);
		db.execSQL(CREATE_BOOK);
		db.execSQL(CREATE_MESSAGE);
		db.execSQL(CREATE_BASKET);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		switch (oldVersion) {
		case 1:
			db.execSQL(CREATE_USER);
			db.execSQL(CREATE_ADMIN);
			db.execSQL(CREATE_BOOK);
			db.execSQL(CREATE_MESSAGE);
			db.execSQL(CREATE_BASKET);
		case 2:
		default:
			break;
		}
	}

}
