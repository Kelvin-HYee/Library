package cn.jishuz.library.until;

import android.graphics.Bitmap;

public class BookCollector {
	private Bitmap bitmap;
	private String description; // title ºÍ score
	
	public  BookCollector(Bitmap bitmap, String description){
		this.bitmap = bitmap;
		this.description = description;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public String getDescription() {
		return description;
	}
	
}
