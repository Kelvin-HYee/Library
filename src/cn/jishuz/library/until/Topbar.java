package cn.jishuz.library.until;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jishuz.library.R;
import android.view.View.OnClickListener;

//这段代码网上搬过来的 主要目的是不重复造轮子 程序员应该要学会
public class Topbar extends RelativeLayout implements OnClickListener{

	private ImageView backView;
	private ImageView rightView;
	private TextView titleView;
	private String titleStr;
	private int strSize;
	private int strColor;
	private Drawable leftDraw;
	private Drawable rightDraw;

	private onTitleBarClickListener onMyClickListener;

	public Topbar(Context context) {
		this(context, null);
	}

	public Topbar(Context context, AttributeSet attrs) {
		this(context, attrs, R.style.AppTheme);
	}

	public Topbar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		getConfig(context, attrs);
		initView(context);
	}

	private void getConfig(Context context, AttributeSet attrs) {
		// TypedArray是一个数组容器用于存放属性值
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Topbar);

		int count = ta.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = ta.getIndex(i);
			switch (attr) {
			case R.styleable.Topbar_titleText:
				titleStr = ta.getString(R.styleable.Topbar_titleText);
				break;
			case R.styleable.Topbar_titleColor:
				// 默认颜色设置为黑色
				strColor = ta.getColor(attr, Color.BLACK);
				break;
			case R.styleable.Topbar_titleSize:
				// 默认设置为16sp，TypeValue也可以把sp转化为px
				strSize = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
						getResources().getDisplayMetrics()));
				break;

			case R.styleable.Topbar_leftBtn:

				leftDraw = ta.getDrawable(R.styleable.Topbar_leftBtn);
				break;
			case R.styleable.Topbar_rightBtn:
				rightDraw = ta.getDrawable(R.styleable.Topbar_rightBtn);
				break;
			}
		}

		// 用完务必回收容器
		ta.recycle();

	}

	private void initView(Context context) {
		View layout = LayoutInflater.from(context).inflate(R.layout.topbar, this, true);

		backView = (ImageView) layout.findViewById(R.id.back_image);
		titleView = (TextView) layout.findViewById(R.id.text_title);
		rightView = (ImageView) layout.findViewById(R.id.right_image);
		backView.setOnClickListener(this);
		rightView.setOnClickListener(this);

		if (null != leftDraw)
			backView.setImageDrawable(leftDraw);
		if (null != rightDraw)
			rightView.setImageDrawable(rightDraw);
		if (null != titleStr) {
			titleView.setText(titleStr);
			titleView.setTextSize(strSize);
			titleView.setTextColor(strColor);
			titleView.setTypeface(Typeface.DEFAULT);
		}
	}


	/**
	 * 设置按钮点击监听接口
	 * @param callback
	 */
	public void setClickListener(onTitleBarClickListener listener) {
		this.onMyClickListener = listener;
	}

	/**
	 * 导航栏点击监听接口
	 */
	public static interface onTitleBarClickListener {
		/**
		 * 点击返回按钮回调
		 */
		void onBackClick();

		void onRightClick();
	}

	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back_image:
			if (null != onMyClickListener)
				onMyClickListener.onBackClick();
			break;
		case R.id.right_image:
			if (null != onMyClickListener)
				onMyClickListener.onRightClick();
			break;
		}
	}

	
}
