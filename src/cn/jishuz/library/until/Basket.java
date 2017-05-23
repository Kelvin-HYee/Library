package cn.jishuz.library.until;

import android.graphics.Bitmap;

public class Basket {
	
	public Bitmap mBookImage;
	public String mBookTitile;
	public String mBookAuthor;
	public float core_r;
	public float core;
	
	public Basket(Bitmap mBookImage,String mBookTitile,String mBookAuthor,float core_r,float core){
		this.mBookImage = mBookImage;
		this.mBookTitile = mBookTitile;
		this.mBookAuthor = mBookAuthor;
		this.core_r = core_r;
		this.core = core;
	}
}
