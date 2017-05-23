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

//��δ������ϰ������ ��ҪĿ���ǲ��ظ������� ����ԱӦ��Ҫѧ��
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
		// TypedArray��һ�������������ڴ������ֵ
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Topbar);

		int count = ta.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = ta.getIndex(i);
			switch (attr) {
			case R.styleable.Topbar_titleText:
				titleStr = ta.getString(R.styleable.Topbar_titleText);
				break;
			case R.styleable.Topbar_titleColor:
				// Ĭ����ɫ����Ϊ��ɫ
				strColor = ta.getColor(attr, Color.BLACK);
				break;
			case R.styleable.Topbar_titleSize:
				// Ĭ������Ϊ16sp��TypeValueҲ���԰�spת��Ϊpx
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

		// ������ػ�������
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
	 * ���ð�ť��������ӿ�
	 * @param callback
	 */
	public void setClickListener(onTitleBarClickListener listener) {
		this.onMyClickListener = listener;
	}

	/**
	 * ��������������ӿ�
	 */
	public static interface onTitleBarClickListener {
		/**
		 * ������ذ�ť�ص�
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
