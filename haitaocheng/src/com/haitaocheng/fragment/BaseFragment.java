package com.haitaocheng.fragment;


import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.haitaocheng.activity.MainActivity;
import com.haitaocheng.adapter.HTCFragmentAdapter;
import com.haitaocheng.constants.Cons;
import com.haitaocheng.constants.HTCEventBus;
import com.haitaocheng.tools.ScreenUtil;
import com.haitaocheng.tools.ViewPagerFragmentUtil;
import com.haitaocheng.widget.ControlScrollViewPager;
import com.haitaocheng.widget.QuickReturnListView;
import com.haitaocheng.www.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class BaseFragment extends Fragment {
	private View mView;
	public  Button button0;
	private Button button1;
	private Button button2;
	private Button button3;
	
	private ViewPagerFragment0 fragment0;
	private ViewPagerFragment1 fragment1;
	private ViewPagerFragment2 fragment2;
	private ViewPagerFragment3 fragment3;
	
	
	private ImageView topRoller;
	private View topTab;
	private Button btArrow; 
	
	private ArrayList<Button> buttons;
	private ArrayList<Fragment> fragmentList;

	protected MainActivity mainActivity;
	private int one;
	private int currentIndex;
	
	private int tabHeight;

	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;
	private int mScrollY;
	private int mMinRawY = 0;

	private TranslateAnimation anim;
	
	private ListView lvPopupList;
	protected PopupWindow pwMyPopWindow;
	private int NUM_OF_VISIBLE_LIST_ROWS = 5;
	private ControlScrollViewPager viewPager;
	private boolean isADVisible = true;
	
	private ArrowClickListener arrowClickListener;
	protected LayoutInflater inflater;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_base, container, false);
		mainActivity = (MainActivity)getActivity();
		findById(); //初始化控件
		buttons = Lists.newArrayList(button0,button1,button2,button3);
		
//		//初始化滚动条，获取一个页卡距离
//		one = ScreenUtil.initRoller(getActivity(), topRoller, R.drawable.roller, 4);
		initViewPager();
		
//		//设置点击监听器
//		for (int i = 0; i < buttons.size(); i++) {
//			buttons.get(i).setOnClickListener(new TabClickListener(i));
//		}
		
		//初始化popwindow
		iniPopupWindow();
		
		btArrow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				onClickArrow();
			}
		});
		
		HTCEventBus.getEventBus().register(this);
		
		return mView;
	}
	
	public void onClickArrow() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (pwMyPopWindow.isShowing()) {

			pwMyPopWindow.dismiss();// 关闭popwidow
		} else {

			pwMyPopWindow.showAsDropDown(btArrow);// 显示
		}
	}

	private void initViewPager() {
		// TODO Auto-generated method stub
		fragment0 = new ViewPagerFragment0();
		fragment1 = new ViewPagerFragment1();
		fragment2 = new ViewPagerFragment2();
		fragment3 = new ViewPagerFragment3();
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(fragment0);
		fragmentList.add(fragment1);
		fragmentList.add(fragment2);
		fragmentList.add(fragment3);
		
		ViewPagerFragmentUtil viewPagerUtil = new ViewPagerFragmentUtil(getActivity(), viewPager, buttons, fragmentList, topRoller);
		viewPagerUtil.setPagerAdapter(new HTCFragmentAdapter(getChildFragmentManager(),fragmentList));
		// 设置缓存页面，当前页面的相邻N个页面都会被缓存
		viewPagerUtil.setOffscreenPageLimit(1);
		viewPagerUtil.onViewPager();
		
		for (int i = 0; i < fragmentList.size(); i++) {
			((BaseViewPagerFragment)fragmentList.get(i)).setAdVisivible(isADVisible);
		}
        
	}

	private void iniPopupWindow() {
		inflater = (LayoutInflater) mainActivity
				.getSystemService(mainActivity.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.task_detail_popupwindow, null);
		lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
		pwMyPopWindow = new PopupWindow(layout);
		// 加上这个popupwindow中的ListView才可以接收点击事件
		pwMyPopWindow.setFocusable(true);

		lvPopupList.setAdapter(new SimpleAdapter(mainActivity, Lists.newArrayList(ImmutableMap.of("share_key1", "服饰", "share_key2", "化妆品", "share_key3", "鞋帽")), R.layout.list_item_popupwindow, new String[]{"share_key1","share_key2","share_key3"}, new int[]{R.id.tv_list_item}));
		lvPopupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				button3.setText(((TextView)view.findViewById(R.id.tv_list_item)).getText());
				onChange(3);
				
			}
		});

		// 控制popupwindow的宽度和高度自适应
		lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth());
		pwMyPopWindow.setHeight((lvPopupList.getMeasuredHeight() + 20)
				* NUM_OF_VISIBLE_LIST_ROWS );

		// 控制popupwindow点击屏幕其他地方消失
		pwMyPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
		pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
	}



	private void findById() {
		// TODO Auto-generated method stub
		topRoller = (ImageView)mView.findViewById(R.id.imageview_topRoller);
		button0 = (Button)mView.findViewById(R.id.button0);
		button1 = (Button)mView.findViewById(R.id.button1);
		button2 = (Button)mView.findViewById(R.id.button2);
		button3 = (Button)mView.findViewById(R.id.button3);
		topTab = mView.findViewById(R.id.layout_topTab);
		btArrow = (Button)mView.findViewById(R.id.button_arrow);
		viewPager = (ControlScrollViewPager)mView.findViewById(R.id.viewpager_fragment);
	}

	public void setTabText(ArrayList<String> strs) {
		int size = buttons.size();
		for (int i = 0; i < size; i++) {
			buttons.get(i).setText(strs.get(i));
		}
	}
	//按钮点击事件
	class ArrowClickListener implements OnClickListener{
		   
		public ArrowClickListener() {
			
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if (pwMyPopWindow.isShowing()) {

				pwMyPopWindow.dismiss();// 关闭popwidow
			} else {

				pwMyPopWindow.showAsDropDown(btArrow);// 显示
			}
			
		}
		
	}
	
	
	
	public void onChange(int position) {
		
		int size = buttons.size();
		for (int i = 0; i < size; i++) {
			buttons.get(i).setTextColor(Cons.BLACK);
			if (i==position) {
				buttons.get(i).setTextColor(Cons.YELLOW);
			}
		}
		
		Animation animation = new TranslateAnimation(one*currentIndex, one*position, 0, 0);
		currentIndex = position;
		animation.setFillAfter(true);
		animation.setDuration(300);
		topRoller.startAnimation(animation);
	}
	
	@Subscribe 
	public void hideTab(Integer event) {
		switch (event) {
		case View.VISIBLE:
			topTab.setVisibility(View.VISIBLE);
			
			break;

		case View.GONE:
			topTab.setVisibility(View.GONE);
			break;
		}
	}
	public void setAdVisivible(boolean isVisible){
		this.isADVisible = isVisible;
	}
	
}
