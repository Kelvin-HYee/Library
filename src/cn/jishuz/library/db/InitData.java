package cn.jishuz.library.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class InitData {
	
	private static Context mContext;
	
	public InitData(Context context){
		mContext = context;
	}
	
	public void init(){
		Mydata my =new Mydata(mContext);
		SQLiteDatabase db = my.getWritableDatabase();
		db.execSQL("insert into admin VALUES(1,'admin','admin')");
		
		db.execSQL("insert into book VALUES(1,'dadandson','父与子','埃-奥-卜劳恩（E.O.Plauen）','浙江文艺出版社',9.0)");
		
		db.execSQL("insert into book VALUES(2,'dragon','恐龙百科全书','【英】哈蒙德','人民邮电出版社',9.8)");
		
		db.execSQL("insert into book VALUES(3,'maitian','麦田里的守望者','杰罗姆·大卫·塞林格','交院出版社',10.0)");
		
		db.execSQL("insert into book VALUES(4,'meet_me','遇见未知的自己','张德芬','华夏出版社',7.5)");
		
		db.execSQL("insert into book VALUES(5,'darkstar','每次告别都有一颗星熄灭','张皓宸','译林出版社',8.0)");
		
		db.execSQL("insert into book VALUES(6,'fbi','FBI教你读心术(钻石升级版)','乔•纳瓦罗','吉林文史出版社',9.0)");
		
		db.execSQL("insert into book VALUES(7,'girl100','提升女人气质的100个细节','戴尔·卡耐基 (Dale Carnegie)','黑龙江科学技术出版社',6.0)");
		
		db.execSQL("insert into book VALUES(8,'hard_work','不上班的23种活法','唐华山','北京出版社',9.0)");
		
		db.execSQL("insert into book VALUES(9,'love','谈恋爱的窍门','晓川山','云南人民出版社',9.5)");
		
		db.execSQL("insert into book VALUES(10,'girl1life','20几岁决定女人的一生','薛兰','远方出版社',8.5)");
		
		db.execSQL("insert into book VALUES(11,'weilaijs','未来简史','尤瓦尔•赫拉利','中信出版社',9.5)");
		
		db.execSQL("insert into book VALUES(12,'xijinping','平易近人:习近平的语言力量 ','丁晓萍','上海交通大学出版社',5.0)");
		
		db.execSQL("insert into book VALUES(13,'yuzoule','小金鱼逃走了','五味太郎','新星出版社',10.0)");
		
		db.execSQL("insert into book VALUES(14,'we3','我们仨','杨绛','生活•读书•新知三联书店',7.5)");
		
		db.execSQL("insert into book VALUES(15,'zhuifengz','追风筝的人','卡勒德•胡赛尼 (Khaled Hosseini) ','上海人民出版社',9.5)");

		db.close();
		my.close();
	}
}
