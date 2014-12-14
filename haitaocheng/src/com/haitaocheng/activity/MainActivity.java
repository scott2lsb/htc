package com.haitaocheng.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.haitaocheng.adapter.HTCFragmentAdapter;
import com.haitaocheng.constants.Cons;
import com.haitaocheng.constants.HTCEventBus;
import com.haitaocheng.fragment.DiscountFragment;
import com.haitaocheng.fragment.FindFragment;
import com.haitaocheng.fragment.LogisticsFragment;
import com.haitaocheng.fragment.PlatformRecommendFragment;
import com.haitaocheng.fragment.RightMenuFragment;
import com.haitaocheng.tools.ScreenUtil;
import com.haitaocheng.tools.ViewPagerFragmentUtil;
import com.haitaocheng.widget.ControlScrollViewPager;
import com.haitaocheng.www.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


public class MainActivity extends SlidingFragmentActivity {
	private ImageButton btShoppingCart;
	private ImageButton btMenu;
	private EditText etSearch;
	private Button btDiscountInfo;
	private Button btPlatformRecommend;
	private Button btLogistcs;
	private Button btFind;
	public ImageView buttomRoller;
	public View bottomTab;
	private SlidingMenu slidingMenu;
	private ControlScrollViewPager viewPager;
	
	
	private DiscountFragment discountFragment;
	private PlatformRecommendFragment platformRecommendFragment;
	private LogisticsFragment logisticsFragment;
	private FindFragment findFragment;
	private int one;
	private int currentIndex; //当前索引
	private ArrayList<Fragment> fragmentList;
	public static EventBus eventBus;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//设置布局
		setContentView(R.layout.activity_base); 
		//注入视图
		findViewById();
		LogUtils.customTagPrefix = "haitaocheng";
		
		//初始化fragment+viewpager;
		initViewPager();
		
		
		
		//初始化侧边栏
		initSlidingMenu();
//		//初始化滚动条，获取一个页卡距离
//		one = ScreenUtil.initRoller(this, buttomRoller, R.drawable.roller, 3);
		eventBus =HTCEventBus.getEventBus();
		eventBus.register(MainActivity.this);
	}
	
	private void findViewById() {
		// TODO Auto-generated method stub
		btShoppingCart = (ImageButton) findViewById(R.id.button_ShoppingCart);
		btMenu = (ImageButton) findViewById(R.id.button_Menu);
		etSearch = (EditText) findViewById(R.id.EditText_Search);
		btDiscountInfo = (Button) findViewById(R.id.button_DiscountInfo);
		btPlatformRecommend = (Button) findViewById(R.id.button_platform_recommend);
		btLogistcs = (Button) findViewById(R.id.button_logistics);
		btFind = (Button)findViewById(R.id.button_find);
		buttomRoller = (ImageView) findViewById(R.id.imageview_bottomRoller);
		bottomTab = findViewById(R.id.layout_bottomTap);
		viewPager = (ControlScrollViewPager) findViewById(R.id.viewpager_activity);
	}

	private void initViewPager() {
		// TODO Auto-generated method stub
		discountFragment = new DiscountFragment();
		platformRecommendFragment = new PlatformRecommendFragment();
		logisticsFragment = new LogisticsFragment();
		findFragment = new FindFragment();
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(discountFragment);
		fragmentList.add(platformRecommendFragment);
		fragmentList.add(logisticsFragment);
		fragmentList.add(findFragment);
		ViewPagerFragmentUtil viewPagerUtil = new ViewPagerFragmentUtil(this, viewPager, Lists.newArrayList(btDiscountInfo,btPlatformRecommend,btLogistcs,btFind), fragmentList, buttomRoller);
		viewPagerUtil.setPagerAdapter(new HTCFragmentAdapter(getSupportFragmentManager(),fragmentList));
		// 设置缓存页面，当前页面的相邻N个页面都会被缓存
		viewPagerUtil.setOffscreenPageLimit(1);
		viewPagerUtil.onViewPager();
	}

	public void initSlidingMenu() {
		// TODO Auto-generated method stub
		slidingMenu = getSlidingMenu();
		// 设置侧滑菜单的位置
		slidingMenu.setMode(SlidingMenu.RIGHT);
		// SlidingMenu.TOUCHMODE_MARGIN代表 边缘拖出来，
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
		// 设置划出的主页面显示的剩余宽度
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		
		// 设置滑动的时候 的渐变程度， 渐入渐出的效果
		slidingMenu.setFadeDegree(0.5f);
		
		
		setBehindContentView(R.layout.slidingmenu_container);//设置主容器, id是 left_menu_layout
		RightMenuFragment rightMenuFragment = new RightMenuFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.sliding_container, rightMenuFragment).commit();
		
	}
//	//点击tab切换fragment
//	@OnClick(R.id.button_DiscountInfo)
//	public void changeFragment0(View v) {
//		getSupportFragmentManager().beginTransaction().replace(R.id.frgment_layout, discountFragment).commit();
//		btDiscountInfo.setTextColor(Cons.YELLOW);
//		btPlatformRecommend.setTextColor(Cons.BLACK);
//		btLogistcs.setTextColor(Cons.BLACK);
//		onPageSelected(0);
//	}
//	@OnClick(R.id.button_platform_recommend)
//	public void changeFragment1(View v) {
//		getSupportFragmentManager().beginTransaction().replace(R.id.frgment_layout, platformRecommendFragment).commit();
//		btDiscountInfo.setTextColor(Cons.BLACK);
//		btPlatformRecommend.setTextColor(Cons.YELLOW);
//		btLogistcs.setTextColor(Cons.BLACK);
//		onPageSelected(1);
//	}
//	@OnClick(R.id.button_logistics)
//	public void changeFragment2(View v) {
//		getSupportFragmentManager().beginTransaction().replace(R.id.frgment_layout, logisticsFragment).commit();
//		btDiscountInfo.setTextColor(Cons.BLACK);
//		btPlatformRecommend.setTextColor(Cons.BLACK);
//		btLogistcs.setTextColor(Cons.YELLOW);
//		onPageSelected(2);
//	}
//	
//	public void onPageSelected(int position) {
//		Animation animation = new TranslateAnimation(one*currentIndex, one*position, 0, 0);
//		currentIndex = position;
//		animation.setFillAfter(true);
//		animation.setDuration(300);
//		buttomRoller.startAnimation(animation);
//	}
	@Subscribe 
	public void hideTab(Integer event) {
		LayoutParams layoutParams = (LayoutParams)buttomRoller.getLayoutParams();
		LogUtils.v("EventReceived "+event);
		switch (event) {
		case View.VISIBLE:
			bottomTab.setVisibility(View.VISIBLE);
			
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			buttomRoller.setLayoutParams(layoutParams);
			
			break;

		case View.GONE:
			bottomTab.setVisibility(View.GONE);
			
			layoutParams = (LayoutParams)buttomRoller.getLayoutParams();
			layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			buttomRoller.setLayoutParams(layoutParams);
			break;
		}
	}
	
	
}
