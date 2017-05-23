package cn.jishuz.library.fragment;

import cn.jishuz.library.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class BookFragment extends Fragment {

	// search
	private EditText searchEdit;
	private ImageButton searchBtn;

	// TextView
	private TextView cate1;
	private TextView cate2;
	private TextView cate3;
	private TextView cate4;
	private TextView cate5;
	
	//Fragment
	Fragment fragment;
	/*Fragment fragment2;
	Fragment fragment3;
	Fragment fragment4;
	Fragment fragment5;*/
	
	// ¿Ø¼þ
//	private ViewPager mViewPager;
//	private List<Fragment> mList = new ArrayList<Fragment>();
//	private FragmentPagerAdapter mAdapter;
	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.tab02, container, false);
//		mViewPager = (ViewPager) view.findViewById(R.id.tab02_viewPager);
		

		searchEdit = (EditText) view.findViewById(R.id.search_edit);
		searchBtn = (ImageButton) view.findViewById(R.id.search_btn);

		cate1 = (TextView) view.findViewById(R.id.tab02_cate1);
		cate2 = (TextView) view.findViewById(R.id.tab02_cate2);
		cate3 = (TextView) view.findViewById(R.id.tab02_cate3);
		cate4 = (TextView) view.findViewById(R.id.tab02_cate4);
		cate5 = (TextView) view.findViewById(R.id.tab02_cate5);
		
		select(0);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		cate1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select(0);
			}
		});
		cate2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select(1);
			}
		});
		cate3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select(2);
			}
		});
		cate4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select(3);
			}
		});
		cate5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select(4);
			}
		});
		searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(mContext, searchEdit.getText().toString(), Toast.LENGTH_SHORT).show();
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				hiddenFragment(transaction);
				fragment = new Cate1();
				Bundle bundle = new Bundle();
				bundle.putInt("key", 6);
				bundle.putString("search", searchEdit.getText().toString());
				
				fragment.setArguments(bundle);
				transaction.add(R.id.tab02_viewPager, fragment);
				resetTextColor();
				transaction.commit();
			}
		});
	}
	
	private void select(int i){
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		hiddenFragment(transaction);
		fragment = new Cate1();
		Bundle bundle = new Bundle();
		switch (i) {
		case 0:
			bundle.putInt("key", 1);
			fragment.setArguments(bundle);
			transaction.add(R.id.tab02_viewPager, fragment);
			resetTextColor();
			cate1.setTextColor(getResources().getColor(R.color.red));
			break;
		case 1:
			bundle.putInt("key", 2);
			fragment.setArguments(bundle);
			transaction.add(R.id.tab02_viewPager, fragment);
			resetTextColor();
			cate2.setTextColor(getResources().getColor(R.color.red));
			break;
		case 2:
			bundle.putInt("key", 3);
			fragment.setArguments(bundle);
			transaction.add(R.id.tab02_viewPager, fragment);
			resetTextColor();
			cate3.setTextColor(getResources().getColor(R.color.red));
			break;
		case 3:
			bundle.putInt("key", 4);
			fragment.setArguments(bundle);
			transaction.add(R.id.tab02_viewPager, fragment);
			resetTextColor();
			cate4.setTextColor(getResources().getColor(R.color.red));
			break;
		case 4:
			bundle.putInt("key", 5);
			fragment.setArguments(bundle);
			transaction.add(R.id.tab02_viewPager, fragment);
			resetTextColor();
			cate5.setTextColor(getResources().getColor(R.color.red));
			break;
		default:
			break;
		}
		
		transaction.commit();
	}
	
	private void hiddenFragment(FragmentTransaction transaction) {
		// TODO Auto-generated method stub
		if(fragment != null){
			transaction.hide(fragment);
		}
	}

	private void resetTextColor() {
		cate1.setTextColor(getResources().getColor(R.color.gray));
		cate2.setTextColor(getResources().getColor(R.color.gray));
		cate3.setTextColor(getResources().getColor(R.color.gray));
		cate4.setTextColor(getResources().getColor(R.color.gray));
		cate5.setTextColor(getResources().getColor(R.color.gray));
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		this.mContext = activity;
	}

}
