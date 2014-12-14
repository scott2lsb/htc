package com.haitaocheng.fragment;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.haitaocheng.activity.MainActivity;
import com.haitaocheng.constants.HTCEventBus;
import com.haitaocheng.widget.QuickReturnListView;
import com.haitaocheng.www.R;
import com.lidroid.xutils.util.LogUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class BaseViewPagerFragment extends Fragment {
	private TextView tcAd;
	private ViewPager vpAd;
	private ImageView dot0;
	private ImageView dot1;
	private ImageView dot2;
	private ImageView dot3;
	private QuickReturnListView listView;
	private ArrayList<ImageView> dots;
	private View mView;
	private View layoutAd;
	
	protected float y;
	private boolean isADVisible;
//	private EventBus eventBus;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fragment_in_viewpager, container, false);
		findById(); //初始化控件
		dots = Lists.newArrayList(dot0,dot1,dot2,dot3); 
		//初始化下拉隐藏的listview
		initListView();
		
		return mView;
	}
	
	private void findById() {
		// TODO Auto-generated method stub
		tcAd = (TextView)mView.findViewById(R.id.textview_ad);
		vpAd = (ViewPager)mView.findViewById(R.id.viewpager_ad);
		dot0 = (ImageView)mView.findViewById(R.id.dot0);
		dot1 = (ImageView)mView.findViewById(R.id.dot1);
		dot2 = (ImageView)mView.findViewById(R.id.dot2);
		dot3 = (ImageView)mView.findViewById(R.id.dot3);
		listView = (QuickReturnListView)mView.findViewById(R.id.listview);
		layoutAd = mView.findViewById(R.id.layout_ad);
	}
	
	private void initListView() {
		String[] array = new String[] { "Android", "Android", "Android",
				"Android", "Android", "Android", "Android", "Android",
				"Android", "Android", "Android", "Android", "Android",
				"Android", "Android", "Android" };

		listView.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1, array));
		
		listView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				LogUtils.v("触摸滑动");
				event.getAction();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					y = event.getY();
					
					break;
					
				case MotionEvent.ACTION_UP:
					if (event.getY()-y>0) {
//						topTab.setVisibility(View.VISIBLE);
//						mainActivity.bottomTab.setVisibility(View.VISIBLE);
//						
//						LayoutParams layoutParams = (LayoutParams)mainActivity.buttomRoller.getLayoutParams();
//						layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//						mainActivity.buttomRoller.setLayoutParams(layoutParams);
						
						HTCEventBus.getEventBus().post(new Integer(View.VISIBLE));
						
					} else if(event.getY()-y<0){
//						topTab.setVisibility(View.GONE);
//						mainActivity.bottomTab.setVisibility(View.GONE);
						HTCEventBus.getEventBus().post(new Integer(View.GONE));
//						LayoutParams layoutParams = (LayoutParams)mainActivity.buttomRoller.getLayoutParams();
//						layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
//						layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//						mainActivity.buttomRoller.setLayoutParams(layoutParams);
					}
					break;
				}
				
				return false;
			}
		});
	}
	public void setAdVisivible(boolean isVisible){
		this.isADVisible = isVisible;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (isADVisible) {
			layoutAd.setVisibility(View.VISIBLE);
		} else {
			layoutAd.setVisibility(View.GONE);
		}
	}
}
